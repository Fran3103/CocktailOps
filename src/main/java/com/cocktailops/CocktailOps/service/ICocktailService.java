package com.cocktailops.CocktailOps.service;

import com.cocktailops.CocktailOps.dto.CocktailIngredientRequestDto;
import com.cocktailops.CocktailOps.dto.CocktailRequestDto;
import com.cocktailops.CocktailOps.dto.CocktailResponseDto;
import com.cocktailops.CocktailOps.entitie.Cocktail;

import java.util.List;

public interface ICocktailService {

    CocktailResponseDto create(CocktailRequestDto Dto);

    CocktailResponseDto getById(Long id);

    CocktailResponseDto update(Long id, CocktailResponseDto Dto);

    void delete(Long id);

    CocktailResponseDto findByName(String name);

    List<CocktailResponseDto> findAll();

    CocktailResponseDto addIngredientToCocktail(Long cocktailId, List<CocktailIngredientRequestDto> cocktailIngredientDto);

    void removeIngredientFromCocktail(Long cocktailId, Long productId);

    CocktailResponseDto updateCocktailIngredient(Long cocktailId, Long productId, CocktailIngredientRequestDto cocktailIngredientDto);

}
