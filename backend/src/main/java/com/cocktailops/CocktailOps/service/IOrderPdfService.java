package com.cocktailops.CocktailOps.service;


import com.cocktailops.CocktailOps.dto.orderDto.OrderByDrinksRequestDto;
import com.cocktailops.CocktailOps.dto.orderDto.OrderRequestDto;


public interface IOrderPdfService {
    byte[] generateOrderPdf(Long orderId);

    byte[] generateOrderPreviewPdf(OrderRequestDto orderRequestDto) ;

    byte[] generateOrderByDrinksPreviewPdf(OrderByDrinksRequestDto orderByDrinksRequestDto) ;

}
