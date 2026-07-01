package com.cocktailops.CocktailOps.service;

import com.cocktailops.CocktailOps.dto.authDto.AuthResponseDto;
import com.cocktailops.CocktailOps.dto.authDto.LoginRequestDto;
import com.cocktailops.CocktailOps.dto.authDto.RegisterRequestDto;

public interface IAuthService {

    AuthResponseDto register(RegisterRequestDto dto);

    AuthResponseDto login(LoginRequestDto dto);
}
