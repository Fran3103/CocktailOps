package com.cocktailops.CocktailOps.entitie;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "prepared_product_recipes")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PreparedProductRecipe {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "product_id", nullable = false, unique = true)
    private Product product;

    @Column(name = "output_amount", nullable = false, precision = 10, scale = 2)
    private BigDecimal outputAmount;

    @Enumerated(EnumType.STRING)
    @Column(name = "output_unit", nullable = false, length = 10)
    private MeasureUnit outputUnit;

    @OneToMany(
            mappedBy = "recipe",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private List<PreparedProductRecipeIngredient> ingredients = new ArrayList<>();
}