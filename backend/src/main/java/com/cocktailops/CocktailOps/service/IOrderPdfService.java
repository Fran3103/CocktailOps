package com.cocktailops.CocktailOps.service;


import org.apache.coyote.BadRequestException;

public interface IOrderPdfService {
    byte[] generateOrderPdf(Long orderId) throws BadRequestException;
}
