package com.cocktailops.CocktailOps.repository;

import com.cocktailops.CocktailOps.dto.CategoryResponseDto;
import com.cocktailops.CocktailOps.entitie.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ICategoryRepository extends JpaRepository<Category, Long> {

    boolean existsByName(String name);

    Category findByName(String name);
}
