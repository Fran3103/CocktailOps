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
import com.cocktailops.CocktailOps.exception.RateLimitExceededException;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * -Servicio principal de órdenes.
 * Este archivo concentra el cálculo más importante de CocktailOps:
 * 1. Recibe una orden por tiempo/personas o por cantidad fija de tragos.
 * 2. Calcula cuántos tragos corresponden a cada cóctel.
 * 3. Busca las recetas de los cócteles.
 * 4. Suma los ingredientes por producto.
 * 5. Convierte unidades cuando hace falta.
 * 6. Calcula cuántas botellas/paquetes/unidades comprar.
 * 7. Devuelve una respuesta lista para frontend/PDF.

 * Importante:
 * - Los métodos créate... guardan en base de datos.
 * - Los métodos preview... calculan, pero NO guardan.
 * - Estar logueado NO es lo que decide si se guarda o no.
 * - Estar logueado solo permite asociar la orden a un usuario.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements IOrderService {

    private final IOrderRepository orderRepository;
    private final IProductRepository productRepository;
    private final ICocktailRepository cocktailRepository;
    private final CurrentUserService currentUserService;

    // Factores de conversión usados cuando la receta está cargada en onzas.
    private static final BigDecimal OZ_TO_ML = new BigDecimal("29.5735");
    private static final BigDecimal OZ_TO_G = new BigDecimal("28.3495");

    private static final int LARGE_EVENT_GUEST_THRESHOLD = 60;
    private static final int LARGE_COCKTAIL_SELECTION_THRESHOLD = 8;
    private static final int LARGE_SELECTION_DRINKS_PER_PERSON_PER_HOUR = 2;

    private static final int MAX_ORDERS_PER_24_HOURS = 25;

    /**
     * Cantidad estimada de tragos por persona por hora.

     * Se puede configurar en application-local.properties:
     * order.drinksPerPersonPerHour=1

     * Si no existe la propiedad, el valor por defecto será 1.
     */
    @Value("${order.drinksPerPersonPerHour:1}")
    private int defaultDrinksPerPersonPerHour;

    /**
     * Busca una orden por ID.

     * Regla de seguridad:
     * - ADMIN puede ver cualquier orden.
     * - USER solo puede ver sus propias órdenes.
     */
    @Override
    @Transactional(readOnly = true)
    public OrderResponseDto getOrderById(Long id) {
        Optional<Order> optionalOrder = orderRepository.findById(id);

        if (optionalOrder.isEmpty()) {
            log.warn("Order with id {} not found", id);
            throw new ResourceNotFoundException("Order not found: " + id);
        }

        Order order = optionalOrder.get();

        log.debug(
                "Order with id {} found: guests={}, durationHours={}, cocktails={}, items={}",
                id,
                order.getGuests(),
                order.getDurationHours(),
                order.getCocktails() != null ? order.getCocktails().size() : 0,
                order.getOrderItems() != null ? order.getOrderItems().size() : 0
        );

        validateOrderAccess(order);

        return toResponse(order);
    }

    /**
     * Devuelve todas las órdenes.

     * Este método debe estar protegido desde SecurityConfig para que solo lo use ADMIN.
     */
    @Override
    @Transactional(readOnly = true)
    public List<OrderResponseDto> getAllOrders() {
        List<Order> orders = orderRepository.findAll();
        List<OrderResponseDto> response = new ArrayList<>();

        for (Order order : orders) {
            response.add(toResponse(order));
        }

        return response;
    }

    /**
     * Crea una orden por tiempo/personas y la guarda en base de datos.

     * Ejemplo:
     * guests = 100
     * durationHours = 5
     * drinksPerPersonPerHour = 1
     * totalDrinks = 100 * 5 * 1 = 500

     * Este método siempre intenta guardar porque llama a orderRepository.save(...).
     * Si hay usuario logueado, la orden queda asociada a ese usuario.
     * Si no hay usuario logueado y el endpoint lo permite, se guardaría como orden anónima.
     */
    @Override
    @Transactional
    public OrderResponseDto createOrder(OrderRequestDto dto) {
        validateTimeOrderRequest(dto);
        validateDailyOrderLimit();
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

    /**
     * Crea una orden por cantidad exacta de tragos y la guarda en base de datos.

     * En este modo el cliente ya decide cuántos tragos quiere de cada cóctel.
     * La suma de todos los cócteles debe coincidir con totalDrinks.
     */
    @Override
    @Transactional
    public OrderResponseDto createOrderByDrinks(OrderByDrinksRequestDto dto) {
        validateDrinksOrderRequest(dto);
        validateDailyOrderLimit();
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

    /**
     * Calcula una orden por tiempo/personas, pero NO la guarda.

     * Este método sirve para preview/PDF de invitado o para calcular antes de confirmar.
     */
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

    /**
     * Calcula una orden por cantidad exacta de tragos, pero NO la guarda.
     */
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

    /**
     * Devuelve las órdenes del usuario logueado.
     */
    @Override
    @Transactional(readOnly = true)
    public List<OrderResponseDto> getMyOrders() {
        User currentUser = currentUserService.getCurrentUserOptional().orElse(null);
        List<Order> orders = orderRepository.findByUserId(currentUser != null ? currentUser.getId() : null);
        List<OrderResponseDto> response = new ArrayList<>();

        for (Order order : orders) {
            response.add(toResponse(order));
        }

        return response;
    }

    // --------------------------------------------------------------------------------------------
    // CÁLCULO PRINCIPAL - MODO TIME
    // --------------------------------------------------------------------------------------------

    /**
     * Arma una orden por tiempo/personas.

     * Este método NO guarda. Solo construye el objeto Order en memoria.
     * El guardado ocurre solamente en createOrder(...).
     */
    private Order buildTimeOrder(OrderRequestDto dto, boolean associateCurrentUser) {
        validateTimeOrderRequest(dto);

        int selectedCocktailCount = dto.cocktails().size();

        int drinksPerPerson = calculateDrinksPerPersonPerHour(
                dto.guests(),
                selectedCocktailCount
        );

        int totalDrinks = dto.guests() * drinksPerPerson * dto.durationHours();

        Order order = new Order();
        order.setCreatedAt(Instant.now());
        if (associateCurrentUser) {
            associateCurrentUserIfPresent(order);
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

        Map<Long, Integer> weightsByCocktailId = buildWeightsByCocktailId(dto.cocktails());
        Map<Long, Integer> drinksByCocktailId = distributeByWeights(totalDrinks, weightsByCocktailId);

        int assignedTotal = sumAssignedDrinks(drinksByCocktailId);
        order.setTotalDrinks(assignedTotal);

        for (Map.Entry<Long, Integer> entry : drinksByCocktailId.entrySet()) {
            Long cocktailId = entry.getKey();
            int drinksForThisCocktail = entry.getValue();

            if (drinksForThisCocktail <= 0) {
                continue;
            }

            addCocktailAndIngredientsToOrder(
                    order,
                    cocktailId,
                    drinksForThisCocktail,
                    requiredByProductId,
                    productCache
            );
        }

        addOrderItems(order, requiredByProductId, productCache);

        return order;
    }


    private int calculateDrinksPerPersonPerHour(int guests, int selectedCocktailCount) {
        boolean isLargeEvent = guests >= LARGE_EVENT_GUEST_THRESHOLD;
        boolean hasLargeCocktailSelection = selectedCocktailCount >= LARGE_COCKTAIL_SELECTION_THRESHOLD;

        // Solo usamos una estimación reforzada cuando el evento es grande
        // y además el usuario seleccionó muchas opciones de cócteles.
        if (isLargeEvent && hasLargeCocktailSelection) {
            return LARGE_SELECTION_DRINKS_PER_PERSON_PER_HOUR;
        }

        return defaultDrinksPerPersonPerHour;
    }
    /**
     * Válida los datos mínimos para una orden por tiempo/personas.
     */
    private void validateTimeOrderRequest(OrderRequestDto dto) {
        if (dto.guests() == null || dto.guests() <= 0) {
            throw new BadRequestException("Guests must be greater than 0");
        }

        if (dto.durationHours() == null || dto.durationHours() <= 0) {
            throw new BadRequestException("Duration hours must be greater than 0");
        }

        if (dto.cocktails() == null || dto.cocktails().isEmpty()) {
            throw new BadRequestException("At least one cocktail must be included in the order");
        }

        for (OrderCocktailsWeightDto cocktail : dto.cocktails()) {
            if (cocktail.cocktailId() == null) {
                throw new BadRequestException("cocktailId is required and weight must be > 0");
            }

            if (cocktail.weight() != null && cocktail.weight() <= 0) {
                throw new BadRequestException("cocktailId is required and weight must be > 0");
            }
        }
    }

    /**
     * Construye un mapa cocktailId -> weight.

     * Si el usuario no manda weight, se usa 1.
     * Si el mismo cocktailId aparece más de una vez, se suman los pesos.
     */
    private Map<Long, Integer> buildWeightsByCocktailId(List<OrderCocktailsWeightDto> cocktails) {
        Map<Long, Integer> weightsByCocktailId = new LinkedHashMap<>();

        for (OrderCocktailsWeightDto cocktail : cocktails) {
            Long cocktailId = cocktail.cocktailId();
            int weight = cocktail.weight() == null ? 1 : cocktail.weight();

            // Si el mismo cóctel aparece más de una vez,
            // acumulamos su peso en lugar de reemplazarlo.
            weightsByCocktailId.merge(cocktailId, weight, Integer::sum);
        }

        return weightsByCocktailId;
    }

    /**
     * Reparte el total de tragos entre los cócteles según su peso.

     * Ejemplo:
     * totalDrinks = 100
     * Mojito weight = 1
     * Daiquiri weight = 1
     * Gin Tonic weight = 2

     * Resultado:
     * Mojito = 25
     * Daiquiri = 25
     * Gin Tonic = 50
     */
    private Map<Long, Integer> distributeByWeights(int totalDrinks, Map<Long, Integer> weightsById) {
        int sumWeights = 0;

        for (Integer weight : weightsById.values()) {
            sumWeights += weight;
        }

        if (sumWeights <= 0) {
            throw new BadRequestException("Sum of weights must be > 0");
        }

        record Remainder(Long id, BigDecimal fraction) {}

        Map<Long, Integer> drinksByCocktailId = new LinkedHashMap<>();
        List<Remainder> remainders = new ArrayList<>();
        int assignedDrinks = 0;

        for (Map.Entry<Long, Integer> entry : weightsById.entrySet()) {
            Long cocktailId = entry.getKey();
            int weight = entry.getValue();

            BigDecimal exactDrinks = BigDecimal.valueOf(totalDrinks)
                    .multiply(BigDecimal.valueOf(weight))
                    .divide(BigDecimal.valueOf(sumWeights), 12, RoundingMode.DOWN);

            int baseDrinks = exactDrinks.intValue();

            drinksByCocktailId.put(cocktailId, baseDrinks);
            assignedDrinks += baseDrinks;

            BigDecimal fraction = exactDrinks.subtract(BigDecimal.valueOf(baseDrinks));
            remainders.add(new Remainder(cocktailId, fraction));
        }

        int remainingDrinks = totalDrinks - assignedDrinks;

        remainders.sort(
                Comparator.<Remainder, BigDecimal>comparing(Remainder::fraction).reversed()
                        .thenComparing(Remainder::id)
        );

        for (int i = 0; i < remainingDrinks; i++) {
            Long cocktailId = remainders.get(i % remainders.size()).id();
            Integer currentDrinks = drinksByCocktailId.get(cocktailId);
            drinksByCocktailId.put(cocktailId, currentDrinks + 1);
        }

        return drinksByCocktailId;
    }

    // --------------------------------------------------------------------------------------------
    // CÁLCULO PRINCIPAL - MODO DRINKS
    // --------------------------------------------------------------------------------------------

    /**
     * Arma una orden por cantidad exacta de tragos.

     * Este método NO calcula el total a partir de invitados y horas.
     * Usa directamente las cantidades indicadas por el cliente.
     */
    private Order buildDrinksOrder(OrderByDrinksRequestDto dto, boolean associateCurrentUser) {
        validateDrinksOrderRequest(dto);

        Order order = new Order();
        order.setCreatedAt(Instant.now());
        if (associateCurrentUser) {
            associateCurrentUserIfPresent(order);
        }

        order.setMode(OrderMode.DRINKS);
        order.setTotalDrinks(dto.totalDrinks());
        order.setStatus("Draft");
        order.setCocktails(new ArrayList<>());
        order.setOrderItems(new ArrayList<>());

        Map<Long, BigDecimal> requiredByProductId = new HashMap<>();
        Map<Long, Product> productCache = new HashMap<>();

        for (OrderCocktailQuantityDto cocktailDto : dto.cocktails()) {
            addCocktailAndIngredientsToOrder(
                    order,
                    cocktailDto.cocktailId(),
                    cocktailDto.quantity(),
                    requiredByProductId,
                    productCache
            );
        }

        addOrderItems(order, requiredByProductId, productCache);

        return order;
    }

    /**
     * Válida los datos mínimos para una orden por cantidad exacta de tragos.
     */
    private void validateDrinksOrderRequest(OrderByDrinksRequestDto dto) {
        if (dto.totalDrinks() == null || dto.totalDrinks() <= 0) {
            throw new BadRequestException("total drinks must be greater than 0");
        }

        if (dto.cocktails() == null || dto.cocktails().isEmpty()) {
            throw new BadRequestException("cocktails must be greater than 0");
        }

        int sum = 0;

        for (OrderCocktailQuantityDto cocktail : dto.cocktails()) {
            if (cocktail.cocktailId() == null || cocktail.quantity() == null || cocktail.quantity() <= 0) {
                throw new BadRequestException("cocktails must be greater than 0");
            }

            sum += cocktail.quantity();
        }

        if (sum != dto.totalDrinks()) {
            throw new BadRequestException(
                    "sum of cocktails quantity must equal total drinks (" + dto.totalDrinks() + ")"
            );
        }
    }

    // --------------------------------------------------------------------------------------------
    // CÁLCULO COMPARTIDO ENTRE TIME Y DRINKS
    // --------------------------------------------------------------------------------------------

    /**
     * Agrega un cóctel a la orden y acumula sus ingredientes en el mapa requiredByProductId.

     * Este método es compartido por TIME y DRINKS.
     */
    private void addCocktailAndIngredientsToOrder(
            Order order,
            Long cocktailId,
            int drinksForThisCocktail,
            Map<Long, BigDecimal> requiredByProductId,
            Map<Long, Product> productCache
    ) {
        Cocktail cocktail = cocktailRepository.findByWithIngredients(cocktailId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Cocktail with id " + cocktailId + " not found"
                ));

        OrderCocktail orderCocktail = new OrderCocktail();
        orderCocktail.setOrder(order);
        orderCocktail.setCocktail(cocktail);
        orderCocktail.setDrinks(drinksForThisCocktail);

        order.getCocktails().add(orderCocktail);

        for (CocktailIngredient ingredient : cocktail.getIngredients()) {
            Product product = ingredient.getProduct();
            productCache.putIfAbsent(product.getId(), product);

            BigDecimal amountPerDrink = toProductUnit(
                    ingredient.getAmount(),
                    ingredient.getUnit(),
                    product.getUnit()
            );

            BigDecimal totalRequiredForThisCocktail = amountPerDrink
                    .multiply(BigDecimal.valueOf(drinksForThisCocktail));

            /*
             * Esta es una de las líneas más importantes del proyecto.
             *
             * Si Mojito y Daiquiri usan Ron, ambos suman sobre el mismo productId.
             * El sistema NO calcula botellas de ron por separado para cada cóctel.
             * Primero acumula la cantidad total de ron y recién al final calcula botellas.
             */
            BigDecimal currentRequiredAmount = requiredByProductId.get(product.getId());

            if (currentRequiredAmount == null) {
                requiredByProductId.put(product.getId(), totalRequiredForThisCocktail);
            } else {
                requiredByProductId.put(
                        product.getId(),
                        currentRequiredAmount.add(totalRequiredForThisCocktail)
                );
            }
        }
    }

    /**
     * Convierte el mapa de cantidades requeridas por producto en OrderItems.

     * Ejemplo:
     * requiredByProductId dice: Ron -> 1600 ml.
     * El producto Ron tiene unitSize 750 ml.
     * packsToBuy calcula: 1600 / 750 = 2.13 -> 3 botellas.
     */
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

    /**
     * Suma todos los tragos asignados a cócteles.
     */
    private int sumAssignedDrinks(Map<Long, Integer> drinksByCocktailId) {
        int total = 0;

        for (Integer drinks : drinksByCocktailId.values()) {
            total += drinks;
        }

        return total;
    }

    // --------------------------------------------------------------------------------------------
    // Unidades, PACKS Y Respuestas
    // --------------------------------------------------------------------------------------------

    /**
     * Convierte la unidad usada en la receta a la unidad de compra del producto.
     */
    private BigDecimal toProductUnit(BigDecimal amount, MeasureUnit ingredientUnit, String productUnit) {
        MeasureUnit productMeasureUnit = convertProductUnit(productUnit);

        if (ingredientUnit == productMeasureUnit) {
            return amount;
        }

        if (ingredientUnit == MeasureUnit.OZ && productMeasureUnit == MeasureUnit.ML) {
            return amount.multiply(OZ_TO_ML);
        }

        if (ingredientUnit == MeasureUnit.OZ && productMeasureUnit == MeasureUnit.GR) {
            return amount.multiply(OZ_TO_G);
        }

        throw new BusinessRuleException("Cannot convert " + ingredientUnit + " to product unit " + productMeasureUnit);
    }

    /**
     * Convierte el String guardado en Product.unit a un enum MeasureUnit.
     */
    private MeasureUnit convertProductUnit(String productUnit) {
        return switch (productUnit.toLowerCase()) {
            case "ml" -> MeasureUnit.ML;
            case "gr","g" ,"GR"-> MeasureUnit.GR;
            case "unid","UNID", "unit", "UNIT" -> MeasureUnit.UNID;
            default -> throw new BusinessRuleException("Unsupported product unit: " + productUnit);
        };
    }

    /**
     * Calcula cuántas unidades de compra se necesitan.

     * Usa RoundingMode. CEILING porque no se puede comprar media botella.
     */
    private int packsToBuy(Product product, BigDecimal requiredAmount) {
        if (product.getUnitSize() == null || product.getUnitSize().compareTo(BigDecimal.ZERO) <= 0) {
            throw new BusinessRuleException("Product unitSize missing/invalid for product: " + product.getId());
        }

        return requiredAmount
                .divide(product.getUnitSize(), 0, RoundingMode.CEILING)
                .intValue();
    }

    /**
     * Transforma la entidad Order en el DTO que consume el frontend/PDF.
     */
    private OrderResponseDto toResponse(Order order) {
        List<OrderCocktailResponseDto> cocktails = new ArrayList<>();

        for (OrderCocktail orderCocktail : order.getCocktails()) {
            cocktails.add(new OrderCocktailResponseDto(
                    orderCocktail.getCocktail().getId(),
                    orderCocktail.getCocktail().getName(),
                    orderCocktail.getDrinks()
            ));
        }

        List<OrderItemsResponseDto> items = new ArrayList<>();

        for (OrderItem orderItem : order.getOrderItems()) {
            items.add(new OrderItemsResponseDto(
                    orderItem.getProduct().getId(),
                    orderItem.getProduct().getName(),
                    orderItem.getQuantity(),
                    orderItem.getProduct().getUnitSize(),
                    orderItem.getProduct().getUnit()
            ));
        }

        Long userId = null;

        if (order.getUser() != null) {
            userId = order.getUser().getId();
        }

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

    /**
     * Válida si el usuario actual puede ver una orden.
     */
    private void validateOrderAccess(Order order) {
        User currentUser = currentUserService.getCurrentUserOptional().orElse(null);

        boolean isAdmin = currentUser != null && currentUser.getRole() == Role.ADMIN;

        boolean isOwner = currentUser != null && order.getUser() != null
                && order.getUser().getId().equals(currentUser.getId());

        if (!isAdmin && !isOwner) {
            throw new AccessDeniedException("You do not have permission to access this order");
        }
    }

    /**
     * Si hay usuario autenticado, lo asocia a la orden.
     * Si no hay usuario, no rompe: la orden queda sin user.
     */
    private void associateCurrentUserIfPresent(Order order) {
        currentUserService.getCurrentUserOptional().ifPresent(order::setUser);
    }

    /**
     * limite de ordenes por usuario, 25 ordenes cada 24 horas evitando el abuso de ordenes
     */

    private void validateDailyOrderLimit() {

        User currentUser = currentUserService.getCurrentUserOptional()
                .orElseThrow(() -> new IllegalStateException(
                        "Authenticated user not found"
                ));

        Instant since = Instant.now().minusSeconds(24 * 60 * 60);

        long ordersInLast24Hours =
                orderRepository.countByUserIdAndCreatedAtGreaterThanEqual(
                        currentUser.getId(),
                        since
                );

        if (ordersInLast24Hours >= MAX_ORDERS_PER_24_HOURS) {
            log.warn(
                    "Order limit exceeded for userId={}: {} orders in last 24 hours",
                    currentUser.getId(),
                    ordersInLast24Hours
            );

            throw new RateLimitExceededException(
                    "You reached the limit of 25 saved orders within 24 hours."
            );
        }
    }
}