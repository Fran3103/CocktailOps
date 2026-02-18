package com.cocktailops.CocktailOps.dto.userDto;

import com.cocktailops.CocktailOps.entitie.Role;
import io.swagger.v3.oas.annotations.media.Schema;

public record UserResponseDto (
        @Schema(description = "nombre del usuario", example = "Juan")
        String firstName,
        @Schema(description = "apellido del usuario", example = "Pérez")
        String lastName,
        @Schema(description = "id del local al que pertenece el usuario", example = "1")
        Long shop,
        @Schema(description = "id del usuario", example = "1")
        Long id,
        @Schema(description = "rol del usuario", example = "ADMIN")
        Role role,
        @Schema(description = "email del usuario", example = "juanperez@gmail.com")
        String email
){
}
