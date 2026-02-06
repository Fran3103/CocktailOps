package com.cocktailops.CocktailOps.dto;

import com.cocktailops.CocktailOps.entitie.Role;
import com.cocktailops.CocktailOps.entitie.Shop;

public record UserResponseDto (
        String firstName,
        String lastName,
        Shop shop,
        Long id,
        Role role,
        String email
){
}
