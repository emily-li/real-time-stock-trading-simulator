package com.liemily.stock.domain;

import javax.persistence.Entity;
import javax.persistence.Id;

/**
 * Created by Emily Li on 22/09/2017.
 */
@Entity
public class Company {
    @Id
    private String symbol;
    private String name;

    @SuppressWarnings("unused")
    private Company() {
    }

    public Company(String symbol, String name) {
        this.symbol = symbol;
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
