package com.cocktailops.CocktailOps.dto;

import java.util.List;

public record OrderRequestDto(
        Integer guests,
        Integer durationHours,
        List<OrderCocktailsWeightDto> cocktails

) {
}
