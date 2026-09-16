package com.cocktailops.CocktailOps.dto.authDto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record RegisterRequestDto(

        @Schema(
                description = "Nombre del usuario.",
                example = "Franco",
                requiredMode = Schema.RequiredMode.REQUIRED
        )
        @NotBlank
        String firstName,

        @Schema(
                description = "Apellido del usuario.",
                example = "Aguirre",
                requiredMode = Schema.RequiredMode.REQUIRED
        )
        @NotBlank
        String lastName,

        @Schema(
                description = "Correo electrónico que se utilizará para iniciar sesión.",
                example = "usuario@ejemplo.com",
                requiredMode = Schema.RequiredMode.REQUIRED
        )
        @NotBlank
        @Email
        String email,

        @Schema(
                description = "Contraseña del usuario. Debe tener al menos 6 caracteres.",
                example = "clave123",
                minLength = 6,
                requiredMode = Schema.RequiredMode.REQUIRED
        )
        @NotBlank
        @Size(min = 6)
        String password

) {
}