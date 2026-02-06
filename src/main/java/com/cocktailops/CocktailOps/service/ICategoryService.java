package com.cocktailops.CocktailOps.service;

import com.cocktailops.CocktailOps.dto.CategoryRequestDto;
import com.cocktailops.CocktailOps.dto.CategoryResponseDto;

import java.util.List;

public interface ICategoryService {

    CategoryResponseDto getCategoryById(long id);

    CategoryResponseDto createCategory(CategoryRequestDto categoryRequestDto);

    CategoryResponseDto updateCategory(Long id ,CategoryRequestDto categoryRequestDto);

    void deleteCategory(Long id);

    List<CategoryResponseDto> getAllCategories();

    CategoryResponseDto getCategoryByName(String name);
}
