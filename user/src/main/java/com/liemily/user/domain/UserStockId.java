package com.liemily.user.domain;

import javax.persistence.Embeddable;
import java.io.Serializable;

/**
 * Created by Emily Li on 24/09/2017.
 */
@Embeddable
public class UserStockId implements Serializable {
    private String username;
    private String symbol;

    @SuppressWarnings("unused")
    private UserStockId() {
    }

    public UserStockId(String username, String symbol) {
        this.username = username;
        this.symbol = symbol;
    }

    public String getUsername() {
        return username;
    }

    public String getSymbol() {
        return symbol;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        UserStockId that = (UserStockId) o;

        if (username != null ? !username.equals(that.username) : that.username != null) return false;
        return symbol != null ? symbol.equals(that.symbol) : that.symbol == null;
    }

    @Override
    public int hashCode() {
        int result = username != null ? username.hashCode() : 0;
        result = 31 * result + (symbol != null ? symbol.hashCode() : 0);
        return result;
    }
}
