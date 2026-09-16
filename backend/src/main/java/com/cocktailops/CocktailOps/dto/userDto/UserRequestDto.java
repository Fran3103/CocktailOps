package com.cocktailops.CocktailOps.dto.userDto;

import com.cocktailops.CocktailOps.entitie.Role;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

public record UserRequestDto(

        @Schema(
                description = "Correo electrónico del usuario.",
                example = "usuario@ejemplo.com"
        )
        @NotBlank
        String email,

        @Schema(
                description = "Contraseña del usuario.",
                example = "clave123"
        )
        @NotBlank
        String password,

        @Schema(
                description = "Nombre del usuario.",
                example = "Juan"
        )
        @NotBlank
        String firstName,

        @Schema(
                description = "Apellido del usuario.",
                example = "Pérez"
        )
        @NotBlank
        String lastName,

        @Schema(
                description = """
                        Rol asignado al usuario.
                        Si no se especifica al crear un usuario desde la API administrativa,
                        se utiliza USER.
                        """,
                example = "USER",
                allowableValues = {"USER", "ADMIN"},
                defaultValue = "USER"
        )
        Role role,

        @Schema(
                description = """
                        ID de la tienda asociada al usuario.
                        Es opcional y corresponde al módulo de tiendas implementado a nivel backend.
                        """,
                example = "1",
                nullable = true
        )
        Long shop

) {
}