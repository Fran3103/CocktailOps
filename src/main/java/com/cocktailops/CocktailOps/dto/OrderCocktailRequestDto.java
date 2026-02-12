package com.cocktailops.CocktailOps.dto;

public record OrderCocktailRequestDto (
        Long cocktailId,
        Integer drinks
){
}
