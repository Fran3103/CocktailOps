package com.cocktailops.CocktailOps.dto;

public record OrderItemsPdfDto(
        Long productId,
        String productName,
        Integer packsToBuy,
        java.math.BigDecimal packSize,
        String measureUnit,
        java.math.BigDecimal totalToBuy // packsToBuy * packSize
) {
}
