package com.cocktailops.CocktailOps.service;


import com.cocktailops.CocktailOps.dto.orderDto.OrderResponseDto;
import com.cocktailops.CocktailOps.entitie.Order;
import com.cocktailops.CocktailOps.entitie.OrderMode;
import com.cocktailops.CocktailOps.exception.ResourceNotFoundException;
import com.cocktailops.CocktailOps.repository.ICocktailRepository;
import com.cocktailops.CocktailOps.repository.IOrderRepository;
import com.cocktailops.CocktailOps.repository.IProductRepository;
import com.cocktailops.CocktailOps.service.impl.OrderServiceImpl;
import com.fasterxml.jackson.databind.introspect.TypeResolutionContext;
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
    void  getAllOrdes_whenOrdersNotExist_returnListEmpty(){

        when(orderRepository.findAll()).thenReturn(Collections.emptyList());

        List<OrderResponseDto> result = orderServiceImpl.getAllOrders();

        assertNotNull(result);
        assertTrue(result.isEmpty());

        verify(orderRepository).findAll();
        verifyNoMoreInteractions(orderRepository);
        verifyNoInteractions(productRepository);
        verifyNoInteractions(cocktailRepository);

    }



}
