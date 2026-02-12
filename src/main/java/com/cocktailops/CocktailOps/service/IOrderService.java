package com.cocktailops.CocktailOps.service;

import com.cocktailops.CocktailOps.dto.OrderByDrinksRequestDto;
import com.cocktailops.CocktailOps.dto.OrderRequestDto;
import com.cocktailops.CocktailOps.dto.OrderResponseDto;

import java.util.List;

public interface IOrderService {

    OrderResponseDto getOrderById(Long id);

    List<OrderResponseDto> getAllOrders();

    OrderResponseDto createOrder(OrderRequestDto orderRequestDto);

    OrderResponseDto createOrderByDrinks(OrderByDrinksRequestDto Dto);

}
