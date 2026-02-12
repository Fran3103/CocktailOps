package com.cocktailops.CocktailOps.dto;

public record OrderCocktailPdfDto(
        Long cocktailId,
        String cocktailName,
        Integer drinks
) {
}
