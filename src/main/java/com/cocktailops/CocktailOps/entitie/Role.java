package com.cocktailops.CocktailOps.entitie;

import com.fasterxml.jackson.annotation.JsonCreator;

public enum Role {

    ADMIN,
    USER;


    @JsonCreator
    public static Role from(String value) {
        return Role.valueOf(value.trim().toUpperCase());
    }
}
