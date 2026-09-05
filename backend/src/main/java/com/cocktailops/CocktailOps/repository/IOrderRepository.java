package com.cocktailops.CocktailOps.repository;

import com.cocktailops.CocktailOps.entitie.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.time.Instant;
import java.util.List;


@Repository
public interface IOrderRepository extends JpaRepository<Order, Long> {

    List<Order> findByUserId(Long userId);

    long countByUserIdAndCreatedAtGreaterThanEqual(Long userId, Instant createdAt);
}
