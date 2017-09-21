package com.liemily.user.domain;

import javax.persistence.Entity;
import javax.persistence.Id;

/**
 * Created by Emily Li on 21/09/2017.
 */
@Entity
public class UserStock {
    @Id
    private String username;
    private String symbol;
    private int volume;

    @SuppressWarnings("unused")
    private UserStock() {
    }

    public UserStock(String username, String symbol, int volume) {
        this.username = username;
        this.symbol = symbol;
        this.volume = volume;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        UserStock userStock = (UserStock) o;

        return volume == userStock.volume
                && (username != null ? username.equals(userStock.username) : userStock.username == null)
                && (symbol != null ? symbol.equals(userStock.symbol) : userStock.symbol == null);
    }

    @Override
    public int hashCode() {
        int result = username != null ? username.hashCode() : 0;
        result = 31 * result + (symbol != null ? symbol.hashCode() : 0);
        result = 31 * result + volume;
        return result;
    }
}
