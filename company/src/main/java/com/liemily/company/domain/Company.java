package com.liemily.company.domain;

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

    public String getSymbol() {
        return symbol;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Company company = (Company) o;

        return (symbol != null ? symbol.equals(company.symbol) : company.symbol == null) && (name != null ? name.equals(company.name) : company.name == null);
    }

    @Override
    public int hashCode() {
        int result = symbol != null ? symbol.hashCode() : 0;
        result = 31 * result + (name != null ? name.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Company{" +
                "symbol='" + symbol + '\'' +
                ", name='" + name + '\'' +
                '}';
    }
}
