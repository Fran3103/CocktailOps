package com.cocktailops.CocktailOps.dto;

import com.cocktailops.CocktailOps.entitie.Shop;

public record CategoryRequestDto(
        String name,
        Long shop,
        String slug,
        Boolean active
) {
}
