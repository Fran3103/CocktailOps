package com.cocktailops.CocktailOps.dto;

import com.cocktailops.CocktailOps.entitie.Category;

import java.math.BigDecimal;

public record ProductResponseDto(
        Long productId,
        String name,
        Long category,
        String unit,
        String imageUrl,
        String imageAlt,
        Boolean active,
        BigDecimal unitSize
) {
}
