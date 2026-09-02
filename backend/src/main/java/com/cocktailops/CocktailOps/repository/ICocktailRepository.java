package com.cocktailops.CocktailOps.repository;

import com.cocktailops.CocktailOps.dto.cocktailDto.CocktailResponseDto;
import com.cocktailops.CocktailOps.entitie.Cocktail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.List;

@Repository
public interface ICocktailRepository extends JpaRepository<Cocktail, Long> {

    CocktailResponseDto findByName(String name);

    Boolean existsByName(String name);

    @Query("""
  select distinct c from Cocktail c
  left join fetch c.ingredients i
  left join fetch i.product
  where c.id = :id
""")
    Optional<Cocktail> findByWithIngredients(@Param("id") Long id);

    @Query("""
    select distinct c from Cocktail c
    left join fetch c.ingredients i
    left join fetch i.product
""")
    List<Cocktail> findAllWithIngredients();
}
