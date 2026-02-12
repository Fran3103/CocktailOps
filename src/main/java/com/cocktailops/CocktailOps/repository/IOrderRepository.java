package com.cocktailops.CocktailOps.repository;

import com.cocktailops.CocktailOps.entitie.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface IOrderRepository extends JpaRepository<Order, Long> {

}
