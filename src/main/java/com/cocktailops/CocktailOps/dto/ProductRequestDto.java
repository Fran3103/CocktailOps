package com.cocktailops.CocktailOps.dto;

import com.cocktailops.CocktailOps.entitie.Category;

import java.math.BigDecimal;

public record ProductRequestDto(
        Long id,
        String name,
        Long category,
        String unit,
        BigDecimal unitSize,
        Boolean active,
        String imageUrl,
        String imageAlt
) {
}
