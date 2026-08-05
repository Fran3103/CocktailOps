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
import com.cocktailops.CocktailOps.repository.IProductRepository;
import com.cocktailops.CocktailOps.security.CurrentUserService;
import com.cocktailops.CocktailOps.service.impl.OrderServiceImpl;
import com.fasterxml.jackson.databind.introspect.TypeResolutionContext;
import org.hamcrest.text.IsEmptyString;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import static com.cocktailops.CocktailOps.testutil.TestDataFactory.*;

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

        assertEquals("cocktailId is required and weight must be > 0", exception.getMessage() );

        verifyNoInteractions(orderRepository);
        verifyNoInteractions(productRepository);
        verifyNoInteractions(cocktailRepository);
    }
}
