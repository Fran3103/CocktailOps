package com.cocktailops.CocktailOps.dto.userDto;

import com.cocktailops.CocktailOps.entitie.Role;
import io.swagger.v3.oas.annotations.media.Schema;

public record UserResponseDto(

        @Schema(description = "Nombre del usuario.", example = "Juan")
        String firstName,

        @Schema(description = "Apellido del usuario.", example = "Pérez")
        String lastName,

        @Schema(
                description = "ID de la tienda asociada al usuario. Puede ser nulo.",
                example = "1",
                nullable = true
        )
        Long shop,

        @Schema(description = "ID del usuario.", example = "1")
        Long id,

        @Schema(
                description = "Rol asignado al usuario.",
                example = "USER",
                allowableValues = {"USER", "ADMIN"}
        )
        Role role,

        @Schema(
                description = "Correo electrónico del usuario.",
                example = "usuario@ejemplo.com"
        )
        String email

) {
}