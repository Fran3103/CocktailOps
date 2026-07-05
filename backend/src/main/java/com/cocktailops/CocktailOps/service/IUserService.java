package com.cocktailops.CocktailOps.service;

import com.cocktailops.CocktailOps.dto.userDto.UserRequestDto;
import com.cocktailops.CocktailOps.dto.userDto.UserResponseDto;

import java.util.List;


public interface IUserService {


    UserResponseDto findByEmail(String email);

    UserResponseDto findByShop(Long shop);

    UserResponseDto findById(long id);

    UserResponseDto save(UserRequestDto userRequestDto);

    UserResponseDto update(long id, UserRequestDto userRequestDto);

    void delete(long id);

    List<UserResponseDto> findAll();
}
