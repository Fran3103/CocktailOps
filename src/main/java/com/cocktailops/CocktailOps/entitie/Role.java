package com.cocktailops.CocktailOps.entitie;

import com.fasterxml.jackson.annotation.JsonCreator;

public enum Role {

    ADMIN,
    SHOPPING;


    @JsonCreator
    public static Role from(String value) {
        return Role.valueOf(value.trim().toUpperCase());
    }
}
