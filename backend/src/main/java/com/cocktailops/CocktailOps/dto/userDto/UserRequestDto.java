package com.cocktailops.CocktailOps.dto.userDto;

import com.cocktailops.CocktailOps.entitie.Role;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

public record UserRequestDto (
        @Schema(description = "email del usuario", example = "usuario@ejemplo.com")
        @NotBlank String email,
        @Schema(description = "contraseña del usuario", example = "contraseña123")
        @NotBlank String password,
        @Schema(description = "nombre del usuario", example = "Juan")
        @NotBlank String firstName,
        @Schema(description = "apellido del usuario", example = "Pérez")
        @NotBlank String lastName,
        @Schema(description = "rol del usuario", example = "ADMIN")
        Role role,
        @Schema(description = "id del local al que pertenece el usuario", example = "1")
        Long shop
){
}
