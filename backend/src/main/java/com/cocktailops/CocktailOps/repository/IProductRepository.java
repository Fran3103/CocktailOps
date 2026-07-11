package com.cocktailops.CocktailOps.repository;

import com.cocktailops.CocktailOps.dto.productDto.ProductResponseDto;
import com.cocktailops.CocktailOps.entitie.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface IProductRepository extends JpaRepository<Product, Long> {

    Optional<Product> findByName(String name);

    @Query("SELECT p FROM Product p JOIN FETCH p.category")
    List<Product> findAllWithCategory();

    Boolean existsByName(String name);
}
