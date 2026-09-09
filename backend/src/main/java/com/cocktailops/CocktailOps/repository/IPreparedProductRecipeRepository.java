package com.cocktailops.CocktailOps.repository;

import com.cocktailops.CocktailOps.entitie.PreparedProductRecipe;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface IPreparedProductRecipeRepository
        extends JpaRepository<PreparedProductRecipe, Long> {

    @Query("""
            SELECT DISTINCT r
            FROM PreparedProductRecipe r
            LEFT JOIN FETCH r.ingredients i
            LEFT JOIN FETCH i.ingredientProduct
            WHERE r.product.id = :productId
            """)
    Optional<PreparedProductRecipe> findByProductIdWithIngredients(
            @Param("productId") Long productId
    );
}