package com.cocktailops.CocktailOps.entitie;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Entity
@Table(name = "cocktails")
@Getter @Setter
@AllArgsConstructor @NoArgsConstructor
public class Cocktail {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String description;

    private List<CocktailIngredient>  ingredients;
}
