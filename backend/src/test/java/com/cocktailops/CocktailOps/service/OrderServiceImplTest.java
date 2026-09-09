package com.cocktailops.CocktailOps.service;


import com.cocktailops.CocktailOps.dto.orderDto.OrderCocktailsWeightDto;
import com.cocktailops.CocktailOps.dto.orderDto.OrderRequestDto;
import com.cocktailops.CocktailOps.dto.orderDto.OrderResponseDto;
import com.cocktailops.CocktailOps.entitie.Order;
import com.cocktailops.CocktailOps.entitie.OrderMode;
import com.cocktailops.CocktailOps.entitie.Role;
import com.cocktailops.CocktailOps.entitie.User;
import com.cocktailops.CocktailOps.exception.BadRequestException;
import com.cocktailops.CocktailOps.exception.ResourceNotFoundException;
import com.cocktailops.CocktailOps.repository.ICocktailRepository;
import com.cocktailops.CocktailOps.repository.IOrderRepository;
import com.cocktailops.CocktailOps.repository.IPreparedProductRecipeRepository;
import com.cocktailops.CocktailOps.repository.IProductRepository;
import com.cocktailops.CocktailOps.security.CurrentUserService;
import com.cocktailops.CocktailOps.service.impl.OrderServiceImpl;
import com.cocktailops.CocktailOps.exception.RateLimitExceededException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import static com.cocktailops.CocktailOps.testutil.TestDataFactory.*;
import org.springframework.test.util.ReflectionTestUtils;
import com.cocktailops.CocktailOps.entitie.CocktailIngredient;
import com.cocktailops.CocktailOps.entitie.MeasureUnit;
import com.cocktailops.CocktailOps.entitie.PreparedProductRecipe;
import com.cocktailops.CocktailOps.entitie.PreparedProductRecipeIngredient;

import com.cocktailops.CocktailOps.dto.orderDto.OrderByDrinksRequestDto;
import com.cocktailops.CocktailOps.dto.orderDto.OrderCocktailQuantityDto;
import com.cocktailops.CocktailOps.entitie.Cocktail;
import com.cocktailops.CocktailOps.entitie.Product;

import java.math.BigDecimal;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
public class OrderServiceImplTest {

    @Mock
    private IProductRepository productRepository;

    @Mock
    private IOrderRepository orderRepository;

    @Mock
    private ICocktailRepository cocktailRepository;

    @Mock
    private CurrentUserService currentUserService;

    @InjectMocks
    private OrderServiceImpl orderServiceImpl;

    @Mock
    private IPreparedProductRecipeRepository preparedProductRecipeRepository;


    @Test
    void getOrderById_whenOrderExist_returnOrderResponseDto(){

        Long orderId = 1L;

        Order order = new Order();

        order.setId(orderId);
        order.setMode(OrderMode.TIME);
        order.setGuests(50);
        order.setDrinksPerPerson(2);
        order.setDurationHours(5);
        order.setStatus("Draft");
        order.setOrderItems(new ArrayList<>());
        order.setCocktails(new ArrayList<>());

        User user = new User();
        user.setId(1L);
        user.setRole(Role.USER);

        order.setUser(user);

        when(currentUserService.getCurrentUserOptional()).thenReturn(Optional.of(user));



        when(orderRepository.findById(orderId)).thenReturn(Optional.of(order));


        OrderResponseDto result = orderServiceImpl.getOrderById(orderId);

        assertNotNull(result);
        assertEquals(orderId, result.id());
        assertEquals("TIME",result.mode());
        assertEquals(50, result.guests());
        assertEquals(2, result.drinksPerPerson());
        assertEquals(5, result.durationHours());
        assertEquals("Draft", result.status());
        assertTrue(result.items().isEmpty());
        assertTrue(result.cocktail().isEmpty());

        verify(orderRepository).findById(orderId);
        verifyNoMoreInteractions(orderRepository);
        verifyNoInteractions(productRepository);
        verifyNoInteractions(cocktailRepository);

    }


    @Test
    void getOrderById_whenOrderDoesNotExist_throwsResourceNotFoundException(){

        Long orderId2= 2L;

        when(orderRepository.findById(orderId2)).thenReturn(Optional.empty());

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> {
            orderServiceImpl.getOrderById(orderId2);
        });


        assertEquals("Order not found: " + orderId2, exception.getMessage() );

        verify(orderRepository).findById(orderId2);
        verifyNoMoreInteractions(orderRepository);
        verifyNoInteractions(productRepository);
        verifyNoInteractions(cocktailRepository);

    }


    @Test
    void getAllOrders_whenOrdersExist_returnsOrderResponseDtoList() {

        Order order1 = createBasicOrder(1L);
        Order order2 = createBasicOrder(2L);

        when(orderRepository.findAll()).thenReturn(List.of(order1, order2));

        List<OrderResponseDto> result = orderServiceImpl.getAllOrders();

        assertNotNull(result);
        assertEquals(2, result.size());

        assertEquals(1L, result.get(0).id());
        assertEquals("TIME", result.get(0).mode());
        assertEquals(50, result.get(0).guests());
        assertEquals(2, result.get(0).drinksPerPerson());
        assertEquals(4, result.get(0).durationHours());
        assertEquals("Draft", result.get(0).status());
        assertTrue(result.get(0).items().isEmpty());
        assertTrue(result.get(0).cocktail().isEmpty());

        assertEquals(2L, result.get(1).id());
        assertEquals("TIME", result.get(1).mode());
        assertEquals(50, result.get(1).guests());
        assertEquals(2, result.get(1).drinksPerPerson());
        assertEquals(4, result.get(1).durationHours());
        assertEquals("Draft", result.get(1).status());
        assertTrue(result.get(1).items().isEmpty());
        assertTrue(result.get(1).cocktail().isEmpty());

        verify(orderRepository).findAll();
        verifyNoMoreInteractions(orderRepository);
        verifyNoInteractions(productRepository);
        verifyNoInteractions(cocktailRepository);
    }

    @Test
    void  getAllOrdes_whenOrdersDoNotExist_returnsEmptyList(){

        when(orderRepository.findAll()).thenReturn(Collections.emptyList());

        List<OrderResponseDto> result = orderServiceImpl.getAllOrders();

        assertNotNull(result);
        assertTrue(result.isEmpty());

        verify(orderRepository).findAll();
        verifyNoMoreInteractions(orderRepository);
        verifyNoInteractions(productRepository);
        verifyNoInteractions(cocktailRepository);

    }

    @Test
    void createOrder_whenGuestsIsNull_throwsBadRequestException(){

        List<OrderCocktailsWeightDto> listWeightDto = createValidOrderCocktailsWeightList();

        OrderRequestDto dto = createOrderRequestDto(null, 5 ,listWeightDto);

        BadRequestException exception = assertThrows(BadRequestException.class, () ->{
            orderServiceImpl.createOrder(dto);
        });

        assertEquals("Guests must be greater than 0", exception.getMessage() );

        verifyNoInteractions(orderRepository);
        verifyNoInteractions(productRepository);
        verifyNoInteractions(cocktailRepository);
    }


    @Test
    void createOrder_whenGuestsIsZero_throwsBadRequestException(){

        List<OrderCocktailsWeightDto> listWeightDto = createValidOrderCocktailsWeightList();

        OrderRequestDto order = createOrderRequestDto(0, 5 ,listWeightDto);

        BadRequestException exception = assertThrows(BadRequestException.class, () ->{
            orderServiceImpl.createOrder(order);
        });

        assertEquals("Guests must be greater than 0", exception.getMessage() );

        verifyNoInteractions(orderRepository);
        verifyNoInteractions(productRepository);
        verifyNoInteractions(cocktailRepository);
    }



    @Test
    void createOrder_whenDurationHoursIsZero_throwsBadRequestException(){

        List<OrderCocktailsWeightDto> listWeightDto = createValidOrderCocktailsWeightList();

        OrderRequestDto dto = createOrderRequestDto(15, 0 ,listWeightDto);

        BadRequestException exception = assertThrows(BadRequestException.class, () ->{
            orderServiceImpl.createOrder(dto);
        });

        assertEquals("Duration hours must be greater than 0", exception.getMessage() );

        verifyNoInteractions(orderRepository);
        verifyNoInteractions(productRepository);
        verifyNoInteractions(cocktailRepository);
    }




    @Test
    void createOrder_whenDurationHoursIsNull_throwsBadRequestException(){

        List<OrderCocktailsWeightDto> listWeightDto = createValidOrderCocktailsWeightList();

        OrderRequestDto order = createOrderRequestDto(50, null ,listWeightDto);

        BadRequestException exception = assertThrows(BadRequestException.class, () ->{
            orderServiceImpl.createOrder(order);
        });

        assertEquals("Duration hours must be greater than 0", exception.getMessage() );

        verifyNoInteractions(orderRepository);
        verifyNoInteractions(productRepository);
        verifyNoInteractions(cocktailRepository);
    }


    @Test
    void createOrder_whenListCocktailsIsNull_throwsBadRequestException(){

        OrderRequestDto order = createOrderRequestDto(50, 5 ,null);

        BadRequestException exception = assertThrows(BadRequestException.class, () ->{
            orderServiceImpl.createOrder(order);
        });

        assertEquals("At least one cocktail must be included in the order", exception.getMessage() );

        verifyNoInteractions(orderRepository);
        verifyNoInteractions(productRepository);
        verifyNoInteractions(cocktailRepository);
    }


    @Test
    void createOrder_whenListCocktailsIsEmpty_throwsBadRequestException(){

        List<OrderCocktailsWeightDto> listWeightDto = Collections.emptyList();

        OrderRequestDto dto = createOrderRequestDto(50, 5 ,listWeightDto);

        BadRequestException exception = assertThrows(BadRequestException.class, () ->{
            orderServiceImpl.createOrder(dto);
        });

        assertEquals("At least one cocktail must be included in the order", exception.getMessage() );

        verifyNoInteractions(orderRepository);
        verifyNoInteractions(productRepository);
        verifyNoInteractions(cocktailRepository);
    }
//    boolean invalidWeight = dto.cocktails().stream()
//            .anyMatch(c -> c.cocktailId() == null || (c.weight() != null && c.weight() <= 0));
//
//        if (invalidWeight) {
//        throw new BadRequestException("cocktailId is required and weight must be > 0");
//    }

    @Test
    void createOrder_whenCocktailIdIsNull_throwsBadRequestException(){

        OrderCocktailsWeightDto weightDto = createOrderCocktailsWeightDto(null, 2);

        OrderRequestDto dto = createOrderRequestDto(50, 5 ,List.of(weightDto));

        BadRequestException exception = assertThrows(BadRequestException.class, () ->{
            orderServiceImpl.createOrder(dto);
        });

        assertEquals("cocktailId is required and weight must be > 0", exception.getMessage() );

        verifyNoInteractions(orderRepository);
        verifyNoInteractions(productRepository);
        verifyNoInteractions(cocktailRepository);
    }

    @Test
    void createOrder_whenCocktailIdIsZero_throwsBadRequestException(){

        OrderCocktailsWeightDto weightDto = createOrderCocktailsWeightDto(1L, 0);

        OrderRequestDto dto = createOrderRequestDto(50, 5 ,List.of(weightDto));

        BadRequestException exception = assertThrows(BadRequestException.class, () ->{
            orderServiceImpl.createOrder(dto);
        });

        assertEquals("weight must be between 1 and 4", exception.getMessage() );

        verifyNoInteractions(orderRepository);
        verifyNoInteractions(productRepository);
        verifyNoInteractions(cocktailRepository);
    }

    @Test
    void createOrder_whenLimitReached_fails() {

        User user = new User();
        user.setId(1L);
        user.setRole(Role.USER);

        OrderRequestDto dto = createOrderRequestDto(
                50,
                5,
                createValidOrderCocktailsWeightList()
        );

        when(currentUserService.getCurrentUserOptional())
                .thenReturn(Optional.of(user));

        when(orderRepository.countByUserIdAndCreatedAtGreaterThanEqual(
                eq(user.getId()),
                any()
        )).thenReturn(25L);

        RateLimitExceededException exception = assertThrows(
                RateLimitExceededException.class,
                () -> orderServiceImpl.createOrder(dto)
        );

        assertEquals(
                "You reached the limit of 25 saved orders within 24 hours.",
                exception.getMessage()
        );

        verify(orderRepository).countByUserIdAndCreatedAtGreaterThanEqual(
                eq(user.getId()),
                any()
        );

        verify(orderRepository, never()).save(any());
    }

    @Test
    void previewOrderByDrinks_whenTotalDrinksIs101_addsTwoIceBags() {

        Cocktail cocktail = new Cocktail();
        cocktail.setId(1L);
        cocktail.setName("Negroni");
        cocktail.setIngredients(new ArrayList<>());

        Product ice = new Product();
        ice.setId(57L);
        ice.setName("Hielo");
        ice.setUnit("GR");
        ice.setUnitSize(new BigDecimal("15000"));
        ice.setActive(true);

        OrderByDrinksRequestDto dto = new OrderByDrinksRequestDto(
                101,
                List.of(
                        new OrderCocktailQuantityDto(1L, 101)
                )
        );

        when(cocktailRepository.findByWithIngredients(1L))
                .thenReturn(Optional.of(cocktail));

        when(productRepository.findByName("Hielo"))
                .thenReturn(Optional.of(ice));

        OrderResponseDto result = orderServiceImpl.previewOrderByDrinks(dto);

        assertNotNull(result);
        assertEquals(101, result.cocktail().get(0).quantity());

        assertEquals(1, result.items().size());
        assertEquals("Hielo", result.items().get(0).productName());
        assertEquals(2, result.items().get(0).packsToBuy());
        assertEquals(new BigDecimal("15000"), result.items().get(0).packSize());
        assertEquals("GR", result.items().get(0).measureUnit());

        verify(cocktailRepository).findByWithIngredients(1L);
        verify(productRepository).findByName("Hielo");
    }

    @Test
    void previewOrder_largeEventWithManyCocktails_usesOneDrinkPerPersonPerHour() {

        ReflectionTestUtils.setField(
                orderServiceImpl,
                "defaultDrinksPerPersonPerHour",
                1
        );

        List<OrderCocktailsWeightDto> cocktails = List.of(
                new OrderCocktailsWeightDto(1L, 1),
                new OrderCocktailsWeightDto(2L, 1),
                new OrderCocktailsWeightDto(3L, 1),
                new OrderCocktailsWeightDto(4L, 1),
                new OrderCocktailsWeightDto(5L, 1),
                new OrderCocktailsWeightDto(6L, 1),
                new OrderCocktailsWeightDto(7L, 1),
                new OrderCocktailsWeightDto(8L, 1)
        );

        OrderRequestDto dto = createOrderRequestDto(
                290,
                6,
                cocktails
        );

        for (long id = 1; id <= 8; id++) {
            Cocktail cocktail = new Cocktail();
            cocktail.setId(id);
            cocktail.setName("Cocktail " + id);
            cocktail.setIngredients(new ArrayList<>());

            when(cocktailRepository.findByWithIngredients(id))
                    .thenReturn(Optional.of(cocktail));
        }

        Product ice = new Product();
        ice.setId(57L);
        ice.setName("Hielo");
        ice.setUnit("GR");
        ice.setUnitSize(new BigDecimal("15000"));
        ice.setActive(true);

        when(productRepository.findByName("Hielo"))
                .thenReturn(Optional.of(ice));

        OrderResponseDto result = orderServiceImpl.previewOrder(dto);

        assertNotNull(result);

        assertEquals(1, result.drinksPerPerson());
        assertEquals(290, result.guests());
        assertEquals(6, result.durationHours());


        int distributedDrinks = result.cocktail().stream()
                .mapToInt(c -> c.quantity())
                .sum();

        assertEquals(1740, distributedDrinks);
    }


    @Test
    void previewOrderByDrinks_whenCocktailUsesPreparedSyrup_convertsToSugarAndMergesRequirements() {

        // ---------------------------------------------------------
        // Productos
        // ---------------------------------------------------------

        Product syrup = new Product();
        syrup.setId(11L);
        syrup.setName("Almíbar simple");
        syrup.setUnit("ML");
        syrup.setUnitSize(new BigDecimal("1000"));
        syrup.setActive(true);
        syrup.setPurchasable(false);

        Product sugar = new Product();
        sugar.setId(19L);
        sugar.setName("Azúcar");
        sugar.setUnit("GR");
        sugar.setUnitSize(new BigDecimal("1000"));
        sugar.setActive(true);
        sugar.setPurchasable(true);

        Product ice = new Product();
        ice.setId(57L);
        ice.setName("Hielo");
        ice.setUnit("GR");
        ice.setUnitSize(new BigDecimal("15000"));
        ice.setActive(true);
        ice.setPurchasable(true);


        // ---------------------------------------------------------
        // Cóctel
        //
        // Para que el test sea fácil de comprobar:
        //
        // 1600 ml almíbar -> 1000 g azúcar
        // +
        // 500 g azúcar directa
        //
        // Total = 1500 g
        // Compra esperada = 2 paquetes de 1 kg
        // ---------------------------------------------------------

        Cocktail cocktail = new Cocktail();
        cocktail.setId(1L);
        cocktail.setName("Test Cocktail");

        CocktailIngredient syrupIngredient = new CocktailIngredient();
        syrupIngredient.setCocktail(cocktail);
        syrupIngredient.setProduct(syrup);
        syrupIngredient.setAmount(new BigDecimal("1600"));
        syrupIngredient.setUnit(MeasureUnit.ML);

        CocktailIngredient sugarIngredient = new CocktailIngredient();
        sugarIngredient.setCocktail(cocktail);
        sugarIngredient.setProduct(sugar);
        sugarIngredient.setAmount(new BigDecimal("500"));
        sugarIngredient.setUnit(MeasureUnit.GR);

        cocktail.setIngredients(
                new ArrayList<>(List.of(
                        syrupIngredient,
                        sugarIngredient
                ))
        );


        // ---------------------------------------------------------
        // Receta interna del almíbar
        //
        // 1600 ml de almíbar
        // requieren
        // 1000 g de azúcar
        // ---------------------------------------------------------

        PreparedProductRecipe recipe = new PreparedProductRecipe();
        recipe.setId(1L);
        recipe.setProduct(syrup);
        recipe.setOutputAmount(new BigDecimal("1600"));
        recipe.setOutputUnit(MeasureUnit.ML);

        PreparedProductRecipeIngredient recipeSugar =
                new PreparedProductRecipeIngredient();

        recipeSugar.setId(1L);
        recipeSugar.setRecipe(recipe);
        recipeSugar.setIngredientProduct(sugar);
        recipeSugar.setAmount(new BigDecimal("1000"));
        recipeSugar.setUnit(MeasureUnit.GR);

        recipe.setIngredients(
                new ArrayList<>(List.of(recipeSugar))
        );


        // ---------------------------------------------------------
        // Request: un solo trago
        // ---------------------------------------------------------

        OrderByDrinksRequestDto dto = new OrderByDrinksRequestDto(
                1,
                List.of(
                        new OrderCocktailQuantityDto(1L, 1)
                )
        );


        // ---------------------------------------------------------
        // Mocks
        // ---------------------------------------------------------

        when(cocktailRepository.findByWithIngredients(1L))
                .thenReturn(Optional.of(cocktail));

        when(preparedProductRecipeRepository
                .findByProductIdWithIngredients(11L))
                .thenReturn(Optional.of(recipe));

        when(productRepository.findByName("Hielo"))
                .thenReturn(Optional.of(ice));


        // ---------------------------------------------------------
        // Ejecución
        // ---------------------------------------------------------

        OrderResponseDto result =
                orderServiceImpl.previewOrderByDrinks(dto);


        // ---------------------------------------------------------
        // Assertions
        // ---------------------------------------------------------

        assertNotNull(result);

        // El almíbar es preparado: NO debe aparecer en compras.
        assertTrue(
                result.items().stream()
                        .noneMatch(item ->
                                item.productName().equals("Almíbar simple"))
        );

        // El azúcar sí debe aparecer.
        var sugarItem = result.items().stream()
                .filter(item -> item.productName().equals("Azúcar"))
                .findFirst()
                .orElseThrow();

        /*
         * 1000 g provenientes del almíbar
         * +
         * 500 g utilizados directamente
         * =
         * 1500 g
         *
         * Pack = 1000 g
         * CEILING(1500 / 1000) = 2
         */
        assertEquals(2, sugarItem.packsToBuy());
        assertEquals(new BigDecimal("1000"), sugarItem.packSize());
        assertEquals("GR", sugarItem.measureUnit());

        verify(cocktailRepository)
                .findByWithIngredients(1L);

        verify(preparedProductRecipeRepository)
                .findByProductIdWithIngredients(11L);

        verify(productRepository)
                .findByName("Hielo");
    }

    @Test
    void previewOrderByDrinks_whenTotalDrinksIs1740_adds32IceBags() {

        Cocktail cocktail = new Cocktail();
        cocktail.setId(1L);
        cocktail.setName("Test Cocktail");
        cocktail.setIngredients(new ArrayList<>());

        Product ice = new Product();
        ice.setId(57L);
        ice.setName("Hielo");
        ice.setUnit("GR");
        ice.setUnitSize(new BigDecimal("15000"));
        ice.setActive(true);
        ice.setPurchasable(true);

        OrderByDrinksRequestDto dto = new OrderByDrinksRequestDto(
                1740,
                List.of(
                        new OrderCocktailQuantityDto(1L, 1740)
                )
        );

        when(cocktailRepository.findByWithIngredients(1L))
                .thenReturn(Optional.of(cocktail));

        when(productRepository.findByName("Hielo"))
                .thenReturn(Optional.of(ice));

        OrderResponseDto result =
                orderServiceImpl.previewOrderByDrinks(dto);

        var iceItem = result.items().stream()
                .filter(item -> item.productName().equals("Hielo"))
                .findFirst()
                .orElseThrow();

        assertEquals(32, iceItem.packsToBuy());
        assertEquals(new BigDecimal("15000"), iceItem.packSize());
        assertEquals("GR", iceItem.measureUnit());
    }
}
