package com.cocktailops.CocktailOps.service;

import com.cocktailops.CocktailOps.dto.UserRequestDto;
import com.cocktailops.CocktailOps.dto.UserResponseDto;
import com.cocktailops.CocktailOps.entitie.Shop;

import java.util.List;
import java.util.Optional;


public interface IUserService {


    UserResponseDto findByEmail(String email);

    UserResponseDto findByShop(Shop shop);

    UserResponseDto findById(long id);

    UserResponseDto save(UserRequestDto userRequestDto);

    UserResponseDto update(long id, UserRequestDto userRequestDto);

    void delete(long id);

    List<UserResponseDto> findAll();
}
