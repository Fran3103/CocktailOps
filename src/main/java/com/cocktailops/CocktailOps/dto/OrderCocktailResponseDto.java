package com.cocktailops.CocktailOps.dto;

public record OrderCocktailResponseDto (
        Long cocktailId,
        String cocktailName,
        Integer quantity
){
}
