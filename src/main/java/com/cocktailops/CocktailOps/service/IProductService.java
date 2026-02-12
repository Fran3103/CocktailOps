package com.cocktailops.CocktailOps.service;

import com.cocktailops.CocktailOps.dto.ProductRequestDto;
import com.cocktailops.CocktailOps.dto.ProductResponseDto;
import com.cocktailops.CocktailOps.entitie.Category;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface IProductService {

    ProductResponseDto findById(Long id);

    @Query("SELECT new com.cocktailops.CocktailOps.dto.ProductResponseDto(p.name, p.category.id, p.unit, p.imageUrl, p.imageAlt) " +(
            "FROM Product p WHERE p.name = :name"))
    ProductResponseDto findByName(String name);

    ProductResponseDto create(ProductRequestDto productDto);

    ProductResponseDto update(Long id, ProductRequestDto productDto) ;

    void delete(Long id);

    List<ProductResponseDto> findAll();

    List<ProductResponseDto> findByCategoryName(String categoryName);

}
