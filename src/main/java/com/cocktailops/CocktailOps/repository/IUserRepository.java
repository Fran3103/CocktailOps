package com.cocktailops.CocktailOps.repository;

import com.cocktailops.CocktailOps.dto.UserRequestDto;
import com.cocktailops.CocktailOps.dto.UserResponseDto;
import com.cocktailops.CocktailOps.entitie.Shop;
import com.cocktailops.CocktailOps.entitie.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface IUserRepository extends JpaRepository<User,Long> {

    UserResponseDto  findByEmail(String email);

    UserResponseDto  findByShop(Shop shop);

    boolean existsByEmail(String email);


}
