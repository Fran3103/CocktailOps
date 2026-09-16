package com.cocktailops.CocktailOps.dto.authDto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record LoginRequestDto(

        @Schema(
                description = "Correo electrónico del usuario.",
                example = "usuario@ejemplo.com",
                requiredMode = Schema.RequiredMode.REQUIRED
        )
        @NotBlank
        @Email
        String email,

        @Schema(
                description = "Contraseña del usuario.",
                example = "clave123",
                requiredMode = Schema.RequiredMode.REQUIRED
        )
        @NotBlank
        String password

) {
}