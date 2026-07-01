package com.cocktailops.CocktailOps.dto.authDto;

import com.cocktailops.CocktailOps.entitie.Role;

public record AuthResponseDto(
        Long id,
        String email,
        String firstName,
        String lastName,
        Role role
) {
}
