package com.cocktailops.CocktailOps.dto.authDto;

import com.cocktailops.CocktailOps.entitie.Role;
import io.swagger.v3.oas.annotations.media.Schema;

public record AuthResponseDto(

        @Schema(
                description = "ID del usuario.",
                example = "8"
        )
        Long id,

        @Schema(
                description = "Correo electrónico del usuario.",
                example = "usuario@ejemplo.com"
        )
        String email,

        @Schema(
                description = "Nombre del usuario.",
                example = "Franco"
        )
        String firstName,

        @Schema(
                description = "Apellido del usuario.",
                example = "Aguirre"
        )
        String lastName,

        @Schema(
                description = "Rol asignado al usuario.",
                example = "USER",
                allowableValues = {"USER", "ADMIN"}
        )
        Role role,

        @Schema(
                description = """
                        Token JWT utilizado para acceder a los endpoints protegidos.
                        Debe ingresarse en el botón Authorize de Swagger.
                        """,
                example = "eyJhbGciOiJIUzI1NiJ9.ejemplo.firma"
        )
        String token

) {
}