package com.cocktailops.CocktailOps.service;

import com.cocktailops.CocktailOps.dto.orderDto.OrderByDrinksRequestDto;
import com.cocktailops.CocktailOps.dto.orderDto.OrderRequestDto;
import com.cocktailops.CocktailOps.dto.orderDto.OrderResponseDto;

import java.util.List;

public interface IOrderService {

    OrderResponseDto getOrderById(Long id);

    List<OrderResponseDto> getAllOrders();

    OrderResponseDto createOrder(OrderRequestDto orderRequestDto);

    OrderResponseDto createOrderByDrinks(OrderByDrinksRequestDto Dto);

    List<OrderResponseDto> getMyOrders();

}
