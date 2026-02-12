package com.cocktailops.CocktailOps.repository;

import com.cocktailops.CocktailOps.entitie.CocktailIngredient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;


@Repository
public interface ICocktailIngredientRepository extends JpaRepository<CocktailIngredient, Long> {


    @Transactional
    @Query("SELECT ci FROM CocktailIngredient ci WHERE ci.cocktail.id = :cocktailId AND ci.product.id = :productId")
    Optional<CocktailIngredient> findByCocktailIdAndProductId(Long cocktailId, Long productId);


    List<CocktailIngredient> findAllByCocktailId(Long cocktailId);
    void deleteByCocktailIdAndProductId(Long cocktailId, Long productId);
    boolean existsByCocktailIdAndProductId(Long cocktailId, Long productId);
}
