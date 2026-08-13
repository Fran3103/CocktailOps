package com.cocktailops.CocktailOps.service.impl;

import com.cocktailops.CocktailOps.dto.orderDto.*;
import com.cocktailops.CocktailOps.entitie.*;
import com.cocktailops.CocktailOps.exception.BadRequestException;
import com.cocktailops.CocktailOps.exception.BusinessRuleException;
import com.cocktailops.CocktailOps.exception.ResourceNotFoundException;
import com.cocktailops.CocktailOps.repository.ICocktailRepository;
import com.cocktailops.CocktailOps.repository.IOrderRepository;
import com.cocktailops.CocktailOps.repository.IProductRepository;
import com.cocktailops.CocktailOps.security.CurrentUserService;
import com.cocktailops.CocktailOps.service.IOrderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements IOrderService {

    private final IOrderRepository orderRepository;

    private final IProductRepository productRepository;

    private final ICocktailRepository cocktailRepository;

    private final CurrentUserService currentUserService;

    private static final BigDecimal OZ_TO_ML = new BigDecimal("29.5735");

    private static final BigDecimal OZ_TO_G  = new BigDecimal("28.3495");

    @Value("${order.drinksPerPersonPerHour:1}")
    private int defaultDrinksPerPersonPerHour;
    @Override
    public OrderResponseDto getOrderById(Long id) {
        Optional<Order> order = orderRepository.findById(id);
        if (order.isEmpty()) {
                log.warn("Order with id {} not found", id);
                throw new ResourceNotFoundException("Order not found: " + id);
        }

        Order o = order.get();
        log.debug("Order with id {} found: guests={}, durationHours={}, cocktails={}, items={}",
                id,
                o.getGuests(),
                o.getDurationHours(),
                o.getCocktails() != null ? o.getCocktails().size() : 0,
                o.getOrderItems() != null ? o.getOrderItems().size() : 0
            );
        validateOrderAccess(o);
        return toResponse(o);
    }

    @Override
    public List<OrderResponseDto> getAllOrders() {
        return orderRepository.findAll().stream()
                .map(this::toResponse)
                .toList();
    }


    @Override
    @Transactional
    public OrderResponseDto createOrder(OrderRequestDto dto) {
        int cocktailsCount = dto.cocktails() != null ? dto.cocktails().size() : 0;
        log.info(
                "createOrder started: guests={}, durationHours={}, cocktails={}",
                dto.guests(),
                dto.durationHours(),
                cocktailsCount
        );

        Order order = buildTimeOrder(dto, true);

        Order savedOrder = orderRepository.save(order);
        return toResponse(savedOrder);
    }

    @Override
    @Transactional
    public OrderResponseDto createOrderByDrinks(OrderByDrinksRequestDto dto) {
        log.info(
                "createOrderByDrinks started: totalDrinks={}, cocktails={}",
                dto.totalDrinks(),
                dto.cocktails()
        );

        Order order = buildDrinksOrder(dto, true);

        Order savedOrder = orderRepository.save(order);

        log.info(
                "Order created by drinks with id={}, totalDrinks={}, cocktails={}, items={}",
                savedOrder.getId(),
                savedOrder.getTotalDrinks(),
                savedOrder.getCocktails().size(),
                savedOrder.getOrderItems() != null ? savedOrder.getOrderItems().size() : 0
        );

        return toResponse(savedOrder);
    }

    @Override
    @Transactional(readOnly = true)
    public OrderResponseDto previewOrder(OrderRequestDto dto) {
        log.info(
                "previewOrder started: guests={}, durationHours={}, cocktails={}",
                dto.guests(),
                dto.durationHours(),
                dto.cocktails() != null ? dto.cocktails().size() : 0
        );

        Order order = buildTimeOrder(dto, false);

        log.info(
                "previewOrder calculated: totalDrinks={}, cocktails={}, items={}",
                order.getTotalDrinks(),
                order.getCocktails().size(),
                order.getOrderItems().size()
        );

        return toResponse(order);
    }

    @Override
    @Transactional(readOnly = true)
    public OrderResponseDto previewOrderByDrinks(OrderByDrinksRequestDto dto) {
        log.info(
                "previewOrderByDrinks started: totalDrinks={}, cocktails={}",
                dto.totalDrinks(),
                dto.cocktails()
        );

        Order order = buildDrinksOrder(dto, false);

        log.info(
                "previewOrderByDrinks calculated: totalDrinks={}, cocktails={}, items={}",
                order.getTotalDrinks(),
                order.getCocktails().size(),
                order.getOrderItems().size()
        );

        return toResponse(order);
    }

    private Map<Long, Integer> distributeByWeights(int totalDrinks, Map<Long, Integer> weightsById) {

        int sumWeights = weightsById.values().stream().mapToInt(Integer::intValue).sum();
        if (sumWeights <= 0) throw new BadRequestException("Sum of weights must be > 0");

        record Remainder(Long id, BigDecimal frac) {}

        Map<Long, Integer> drinks = new LinkedHashMap<>();
        List<Remainder> remainders = new ArrayList<>();

        int assigned = 0;

        for (var e : weightsById.entrySet()) {
            Long id = e.getKey();
            int w = e.getValue();

            BigDecimal exact = BigDecimal.valueOf(totalDrinks)
                    .multiply(BigDecimal.valueOf(w))
                    .divide(BigDecimal.valueOf(sumWeights), 12, RoundingMode.DOWN);

            int base = exact.intValue();
            drinks.put(id, base);
            assigned += base;

            remainders.add(new Remainder(id, exact.subtract(BigDecimal.valueOf(base))));
        }

        int remaining = totalDrinks - assigned;

        remainders.sort(
                Comparator.<Remainder, BigDecimal>comparing(Remainder::frac).reversed()
                        .thenComparing(Remainder::id)
        );

        for (int i = 0; i < remaining; i++) {
            Long id = remainders.get(i % remainders.size()).id();
            drinks.merge(id, 1, Integer::sum);
        }

        return drinks;
    }



    public List<OrderResponseDto> getMyOrders() {
        User currentUser = currentUserService.getCurrentUserOptional().orElse(null);

        return orderRepository.findByUserId(currentUser != null ? currentUser.getId() : null)
                .stream()
                .map(this::toResponse)
                .toList();
    }


// ---------- Utils ----------

    private void validateOrderAccess(Order order) {
        User currentUser = currentUserService.getCurrentUserOptional().orElse(null);

        assert currentUser != null;
        boolean isAdmin = currentUser.getRole() == Role.ADMIN;

        boolean isOwner = order.getUser() != null
                && order.getUser().getId().equals(currentUser.getId());

        if (!isAdmin && !isOwner) {
            throw new AccessDeniedException("You do not have permission to access this order");
        }
    }

    private BigDecimal toProductUnit(BigDecimal amount, MeasureUnit ingUnit, String productUnit) {
        MeasureUnit pu = switch (productUnit.toLowerCase()) {
            case "ml" -> MeasureUnit.ML;
            case "gr" -> MeasureUnit.GR;
            case "unid" -> MeasureUnit.UNID;
            default -> throw new BusinessRuleException("Unsupported product unit: " + productUnit);
        };

        if (ingUnit == pu) return amount;

        if (ingUnit == MeasureUnit.OZ && pu == MeasureUnit.ML) return amount.multiply(OZ_TO_ML);
        if (ingUnit == MeasureUnit.OZ && pu == MeasureUnit.GR) return amount.multiply(OZ_TO_G);

        throw new BusinessRuleException("Cannot convert " + ingUnit + " to product unit " + pu);
    }

    private int packsToBuy(Product product, BigDecimal requiredAmount) {
        if (product.getUnitSize() == null || product.getUnitSize().compareTo(BigDecimal.ZERO) <= 0) {
            throw new BusinessRuleException("Product unitSize missing/invalid for product: " + product.getId());
        }

        return requiredAmount
                .divide(product.getUnitSize(), 0, RoundingMode.CEILING)
                .intValue();
    }


// Convert Order entity to OrderResponseDto
private OrderResponseDto toResponse(Order order) {
    List<OrderCocktailResponseDto> cocktails = order.getCocktails().stream()
            .map(oc -> new OrderCocktailResponseDto(
                    oc.getCocktail().getId(),
                    oc.getCocktail().getName(),
                    oc.getDrinks()
            ))
            .toList();

    List<OrderItemsResponseDto> items = order.getOrderItems().stream()
            .map(oi -> new OrderItemsResponseDto(
                    oi.getProduct().getId(),
                    oi.getProduct().getName(),
                    oi.getQuantity(),              // packsToBuy
                    oi.getProduct().getUnitSize(),  // packSize
                    oi.getProduct().getUnit()       // ml/gr/unid
            ))
            .toList();

    Long userId = order.getUser() != null ? order.getUser().getId() : null;

    return new OrderResponseDto(
            order.getId(),
            order.getMode().toString(),
            order.getCreatedAt(),
            order.getGuests(),
            order.getDrinksPerPerson(),
            order.getDurationHours(),
            order.getStatus(),
            items,
            cocktails,
            userId
    );
    }

    private Order buildTimeOrder(OrderRequestDto dto, boolean associateCurrentUser) {

        if (dto.guests() == null || dto.guests() <= 0) {
            throw new BadRequestException("Guests must be greater than 0");
        }

        if (dto.durationHours() == null || dto.durationHours() <= 0) {
            throw new BadRequestException("Duration hours must be greater than 0");
        }

        if (dto.cocktails() == null || dto.cocktails().isEmpty()) {
            throw new BadRequestException("At least one cocktail must be included in the order");
        }

        boolean invalidWeight = dto.cocktails().stream()
                .anyMatch(c -> c.cocktailId() == null || (c.weight() != null && c.weight() <= 0));

        if (invalidWeight) {
            throw new BadRequestException("cocktailId is required and weight must be > 0");
        }

        int drinksPerPerson = defaultDrinksPerPersonPerHour;
        int totalDrinks = dto.guests() * drinksPerPerson * dto.durationHours();

        Order order = new Order();

        if (associateCurrentUser) {
            currentUserService.getCurrentUserOptional().ifPresent(order::setUser);
        }

        order.setGuests(dto.guests());
        order.setDrinksPerPerson(drinksPerPerson);
        order.setDurationHours(dto.durationHours());
        order.setStatus("Draft");
        order.setTotalDrinks(totalDrinks);
        order.setMode(OrderMode.TIME);
        order.setCocktails(new ArrayList<>());
        order.setOrderItems(new ArrayList<>());

        Map<Long, BigDecimal> requiredByProductId = new HashMap<>();
        Map<Long, Product> productCache = new HashMap<>();

        Map<Long, Integer> weightsById = dto.cocktails().stream()
                .collect(Collectors.toMap(
                        OrderCocktailsWeightDto::cocktailId,
                        c -> c.weight() == null ? 1 : c.weight(),
                        Integer::sum,
                        LinkedHashMap::new
                ));

        Map<Long, Integer> drinksById = distributeByWeights(totalDrinks, weightsById);

        int assignedTotal = drinksById.values().stream()
                .mapToInt(Integer::intValue)
                .sum();

        order.setTotalDrinks(assignedTotal);

        for (Map.Entry<Long, Integer> e : drinksById.entrySet()) {

            Long cocktailId = e.getKey();
            int drinksForThisCocktail = e.getValue();

            if (drinksForThisCocktail <= 0) {
                continue;
            }

            Cocktail cocktail = cocktailRepository.findByWithIngredients(cocktailId)
                    .orElseThrow(() -> new ResourceNotFoundException(
                            "Cocktail with id " + cocktailId + " not found"
                    ));

            OrderCocktail orderCocktail = new OrderCocktail();
            orderCocktail.setOrder(order);
            orderCocktail.setCocktail(cocktail);
            orderCocktail.setDrinks(drinksForThisCocktail);

            order.getCocktails().add(orderCocktail);

            for (CocktailIngredient ing : cocktail.getIngredients()) {

                Product product = ing.getProduct();
                productCache.putIfAbsent(product.getId(), product);

                BigDecimal perDrinkInProductUnit =
                        toProductUnit(ing.getAmount(), ing.getUnit(), product.getUnit());

                BigDecimal totalRequired =
                        perDrinkInProductUnit.multiply(BigDecimal.valueOf(drinksForThisCocktail));

                requiredByProductId.merge(product.getId(), totalRequired, BigDecimal::add);
            }
        }

        addOrderItems(order, requiredByProductId, productCache);

        return order;
    }

    private Order buildDrinksOrder(OrderByDrinksRequestDto dto, boolean associateCurrentUser) {

        if (dto.totalDrinks() == null || dto.totalDrinks() <= 0) {
            throw new BadRequestException("total drinks must be greater than 0");
        }

        if (dto.cocktails() == null || dto.cocktails().isEmpty()) {
            throw new BadRequestException("cocktails must be greater than 0");
        }

        boolean invalid = dto.cocktails().stream()
                .anyMatch(c -> c.cocktailId() == null || c.quantity() == null || c.quantity() <= 0);

        if (invalid) {
            throw new BadRequestException("cocktails must be greater than 0");
        }

        int sum = dto.cocktails().stream()
                .mapToInt(OrderCocktailQuantityDto::quantity)
                .sum();

        if (sum != dto.totalDrinks()) {
            throw new BadRequestException(
                    "sum of cocktails quantity must equal total drinks (" + dto.totalDrinks() + ")"
            );
        }

        Order order = new Order();

        if (associateCurrentUser) {
            currentUserService.getCurrentUserOptional().ifPresent(order::setUser);
        }

        order.setMode(OrderMode.DRINKS);
        order.setTotalDrinks(dto.totalDrinks());
        order.setStatus("Draft");
        order.setCocktails(new ArrayList<>());
        order.setOrderItems(new ArrayList<>());

        Map<Long, BigDecimal> requiredByProductId = new HashMap<>();
        Map<Long, Product> productCache = new HashMap<>();

        for (OrderCocktailQuantityDto cDto : dto.cocktails()) {

            Cocktail cocktail = cocktailRepository.findByWithIngredients(cDto.cocktailId())
                    .orElseThrow(() -> new ResourceNotFoundException(
                            "Cocktail with id " + cDto.cocktailId() + " not found"
                    ));

            int drinksForThisCocktail = cDto.quantity();

            OrderCocktail orderCocktail = new OrderCocktail();
            orderCocktail.setOrder(order);
            orderCocktail.setCocktail(cocktail);
            orderCocktail.setDrinks(drinksForThisCocktail);

            order.getCocktails().add(orderCocktail);

            for (CocktailIngredient ing : cocktail.getIngredients()) {

                Product product = ing.getProduct();
                productCache.putIfAbsent(product.getId(), product);

                BigDecimal perDrinkInProductUnit =
                        toProductUnit(ing.getAmount(), ing.getUnit(), product.getUnit());

                BigDecimal totalRequired =
                        perDrinkInProductUnit.multiply(BigDecimal.valueOf(drinksForThisCocktail));

                requiredByProductId.merge(product.getId(), totalRequired, BigDecimal::add);
            }
        }

        addOrderItems(order, requiredByProductId, productCache);

        return order;
    }

    private void addOrderItems(
            Order order,
            Map<Long, BigDecimal> requiredByProductId,
            Map<Long, Product> productCache
    ) {
        for (Map.Entry<Long, BigDecimal> entry : requiredByProductId.entrySet()) {

            Long productId = entry.getKey();
            BigDecimal requiredAmountInProductUnit = entry.getValue();

            Product product = productCache.get(productId);

            if (product == null) {
                product = productRepository.findById(productId)
                        .orElseThrow(() -> new ResourceNotFoundException(
                                "Product with id " + productId + " not found"
                        ));
            }

            int packsToBuy = packsToBuy(product, requiredAmountInProductUnit);

            OrderItem orderItem = new OrderItem();
            orderItem.setOrder(order);
            orderItem.setProduct(product);
            orderItem.setQuantity(packsToBuy);
            orderItem.setUnit(product.getUnit());

            order.getOrderItems().add(orderItem);
        }
    }
    }