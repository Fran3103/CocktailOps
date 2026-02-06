package com.cocktailops.CocktailOps.repository;

import com.cocktailops.CocktailOps.entitie.Shop;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IShopRepository extends JpaRepository<Shop, Long> {

    Boolean existsBySlug (String slug);
    Shop findByName(String name);
}
