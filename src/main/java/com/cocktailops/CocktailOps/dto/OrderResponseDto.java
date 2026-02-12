package com.cocktailops.CocktailOps.dto;

import java.time.Instant;
import java.util.List;

public record OrderResponseDto(
        Long id,
        String mode,
        Instant createdAt,
        Integer guests,
        Integer drinksPerPerson,
        Integer durationHours,
        String status,
        List<OrderItemsResponseDto> items,
        List<OrderCocktailResponseDto> cocktail

) {
}
