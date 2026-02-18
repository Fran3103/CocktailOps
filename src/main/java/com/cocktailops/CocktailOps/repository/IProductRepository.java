package com.cocktailops.CocktailOps.repository;

import com.cocktailops.CocktailOps.dto.productDto.ProductResponseDto;
import com.cocktailops.CocktailOps.entitie.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface IProductRepository extends JpaRepository<Product, Long> {

    @Query("SELECT new com.cocktailops.CocktailOps.dto.productDto.ProductResponseDto(p.id,p.name, p.category.id, p.unit, p.imageUrl, p.imageAlt,p.active,p.unitSize) " +(
            "FROM Product p WHERE p.name = :name"))
    ProductResponseDto findByName(String name);

    Boolean existsByName(String name);
}
