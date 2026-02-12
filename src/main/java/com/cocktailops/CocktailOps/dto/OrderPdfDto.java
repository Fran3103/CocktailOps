package com.cocktailops.CocktailOps.dto;

import java.time.Instant;
import java.util.List;

public record OrderPdfDto(
        Long orderId,
        String mode,
        String createdAt,
        Integer guests,
        Integer drinksPerPerson,
        Integer durationHours,
        Integer totalDrinks,
        List<OrderCocktailPdfDto> cocktails,
        List<OrderItemsPdfDto> items
) {
}
