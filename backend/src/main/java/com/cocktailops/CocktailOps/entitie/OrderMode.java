package com.cocktailops.CocktailOps.entitie;

import com.fasterxml.jackson.annotation.JsonCreator;

public enum OrderMode {
    TIME,
    DRINKS;

    @JsonCreator
    public static OrderMode from(String value) {
        return OrderMode.valueOf(value.trim().toUpperCase());
    }
}
