package com.cocktailops.CocktailOps.controller;

import com.cocktailops.CocktailOps.dto.authDto.AuthResponseDto;
import com.cocktailops.CocktailOps.dto.authDto.LoginRequestDto;
import com.cocktailops.CocktailOps.dto.authDto.RegisterRequestDto;
import com.cocktailops.CocktailOps.service.IAuthService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

import java.net.URI;

@Tag(
        name = "Autenticación",
        description = "Registro de usuarios e inicio de sesión mediante JWT"
)
@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final IAuthService iAuthService;

    @Operation(
            summary = "Registrar un usuario",
            description = """
                Crea una nueva cuenta de usuario.

                Los usuarios registrados reciben el rol USER
                y se genera automáticamente un token JWT.
                """
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "201",
                    description = "Usuario registrado correctamente",
                    content = @Content(schema = @Schema(implementation = AuthResponseDto.class))
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Datos de registro inválidos"
            ),
            @ApiResponse(
                    responseCode = "409",
                    description = "Ya existe un usuario registrado con ese correo electrónico"
            )
    })
    @PostMapping("/register")
    public ResponseEntity<AuthResponseDto> register(@Valid @RequestBody RegisterRequestDto dto){

        AuthResponseDto responseDto = iAuthService.register(dto);

        URI location = URI.create("user/"+ responseDto.id());

        return ResponseEntity.created(location).body(responseDto);
    }


    @Operation(
            summary = "Iniciar sesión",
            description = """
                Autentica al usuario mediante correo electrónico y contraseña.

                Si las credenciales son correctas, devuelve los datos básicos
                del usuario junto con un token JWT para acceder a los endpoints protegidos.
                """
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Inicio de sesión correcto",
                    content = @Content(schema = @Schema(implementation = AuthResponseDto.class))
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Datos de inicio de sesión inválidos"
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "Correo electrónico o contraseña incorrectos"
            )
    })
    @PostMapping("/login")
    public ResponseEntity<AuthResponseDto> login(@Valid @RequestBody LoginRequestDto dto){
        AuthResponseDto responseDto = iAuthService.login(dto);
        return ResponseEntity.ok(responseDto);
    }
}
