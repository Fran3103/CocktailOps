package com.cocktailops.CocktailOps.dto;

import com.cocktailops.CocktailOps.entitie.Role;
import com.cocktailops.CocktailOps.entitie.Shop;
import jakarta.validation.constraints.NotBlank;

public record UserRequestDto (
        @NotBlank String email,
        @NotBlank String password,
        @NotBlank String firstName,
        @NotBlank String lastName,
        Role role,
        Shop shop
){
}
