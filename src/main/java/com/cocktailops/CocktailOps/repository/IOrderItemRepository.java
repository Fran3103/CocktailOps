package com.cocktailops.CocktailOps.repository;

import com.cocktailops.CocktailOps.entitie.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IOrderItemRepository extends JpaRepository<OrderItem, Long> {
}
