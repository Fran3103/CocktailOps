package com.cocktailops.CocktailOps.entitie;

import com.fasterxml.jackson.annotation.JsonCreator;

public enum MeasureUnit {

    OZ,
    ML,
    GR,
    UNID;

    @JsonCreator
    public static MeasureUnit from(String value) {
        return MeasureUnit.valueOf(value.trim().toUpperCase());
    }
}
