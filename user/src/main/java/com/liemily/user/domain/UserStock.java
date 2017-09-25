package com.liemily.user.domain;

import com.liemily.stock.domain.StockView;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;

/**
 * Created by Emily Li on 21/09/2017.
 */
@Entity
@IdClass(UserStockId.class)
public class UserStock {
    @Id
    private String username;
    @Id
    private String symbol;
    private int volume;

    @ManyToOne
    @JoinColumn(name = "symbol", insertable = false, updatable = false)
    private StockView stockView;

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

    public String getSymbol() {
        return symbol;
    }

    public String getName() {
        return stockView == null ? null : stockView.getName();
    }

    public Date getLastTradeDateTime() {
        return stockView.getLastTradeDateTime();
    }

    public BigDecimal getGains() {
        return stockView.getGains();
    }

    public BigDecimal getValue() {
        return stockView.getValue();
    }

    public BigDecimal getOpenValue() {
        return stockView.getOpenValue();
    }

    public BigDecimal getCloseValue() {
        return stockView.getCloseValue();
    }
}
