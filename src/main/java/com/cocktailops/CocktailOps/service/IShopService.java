package com.cocktailops.CocktailOps.service;

import com.cocktailops.CocktailOps.dto.ShopRequestDto;
import com.cocktailops.CocktailOps.dto.ShopResponseDto;

import java.util.List;

public interface IShopService {

    ShopResponseDto findById(long id);

    ShopResponseDto findByName(String name);

    ShopResponseDto createShop(ShopRequestDto Dto);

    ShopResponseDto updateShop(Long id, ShopRequestDto Dto);

    void deleteShop(Long id);

    List<ShopResponseDto> findAll();


}
