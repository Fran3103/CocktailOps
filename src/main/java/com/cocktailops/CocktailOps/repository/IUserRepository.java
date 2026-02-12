package com.cocktailops.CocktailOps.repository;

import com.cocktailops.CocktailOps.dto.UserResponseDto;
import com.cocktailops.CocktailOps.entitie.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface IUserRepository extends JpaRepository<User,Long> {

    @Query("SELECT new com.cocktailops.CocktailOps.dto.UserResponseDto(u.firstName, u.lastName, u.shop.id, u.id, u.role, u.email) " +
            ("FROM User u WHERE u.email = :email"))
    UserResponseDto  findByEmail(String email);

    @Query("SELECT new com.cocktailops.CocktailOps.dto.UserResponseDto(u.firstName, u.lastName, u.shop.id, u.id, u.role, u.email) " +
            ("FROM User u WHERE u.shop.id = :shop"))
    UserResponseDto  findByShop(Long shop);

    boolean existsByEmail(String email);


}
