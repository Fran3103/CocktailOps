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

import java.net.URI;

@Tag(name="Auth", description = "Registro e inicio de sesión de usuarios")
@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final IAuthService iAuthService;

    @PostMapping("/register")
    public ResponseEntity<AuthResponseDto> register(@Valid @RequestBody RegisterRequestDto dto){

        AuthResponseDto responseDto = iAuthService.register(dto);

        URI location = URI.create("user/"+ responseDto.id());

        return ResponseEntity.created(location).body(responseDto);
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponseDto> login(@Valid @RequestBody LoginRequestDto dto){
        AuthResponseDto responseDto = iAuthService.login(dto);
        return ResponseEntity.ok(responseDto);
    }
}
