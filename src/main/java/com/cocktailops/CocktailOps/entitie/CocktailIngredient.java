package com.cocktailops.CocktailOps.entitie;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Entity
@Table(name = "cocktail_ingredients",
uniqueConstraints = @UniqueConstraint(columnNames = {"cocktail_id", "product_id"}))
@Getter @Setter
@AllArgsConstructor @NoArgsConstructor
public class CocktailIngredient {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "cocktail_id", nullable = false)
    private Cocktail cocktail;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    private BigDecimal ounces;
}
