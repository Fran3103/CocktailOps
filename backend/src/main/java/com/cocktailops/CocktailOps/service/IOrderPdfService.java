package com.cocktailops.CocktailOps.service;


import com.cocktailops.CocktailOps.dto.orderDto.OrderByDrinksRequestDto;
import com.cocktailops.CocktailOps.dto.orderDto.OrderRequestDto;
import org.apache.coyote.BadRequestException;

public interface IOrderPdfService {
    byte[] generateOrderPdf(Long orderId) throws BadRequestException;

    byte[] generateOrderPreviewPdf(OrderRequestDto orderRequestDto) throws BadRequestException;

    byte[] generateOrderByDrinksPreviewPdf(OrderByDrinksRequestDto orderByDrinksRequestDto) throws BadRequestException;

}
