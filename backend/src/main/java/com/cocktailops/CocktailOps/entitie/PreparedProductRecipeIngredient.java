package com.cocktailops.CocktailOps.entitie;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Entity
@Table(
        name = "prepared_product_recipe_ingredients",
        uniqueConstraints = @UniqueConstraint(
                columnNames = {"recipe_id", "ingredient_product_id"}
        )
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PreparedProductRecipeIngredient {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "recipe_id", nullable = false)
    private PreparedProductRecipe recipe;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "ingredient_product_id", nullable = false)
    private Product ingredientProduct;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal amount;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 10)
    private MeasureUnit unit;
}