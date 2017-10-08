package com.liemily.realtimestocktradingsimulator.web.domain;

/**
 * Created by Emily Li on 08/10/2017.
 */
public enum UserProperty {
    USERNAME,
    PASSWORD,
    EMAIL,
    FORENAME,
    SURNAME;

    @Override
    public String toString() {
        return super.toString().toLowerCase();
    }
}
