package com.liemily.user.domain;

import com.liemily.stock.domain.StockItem;
import com.liemily.stock.domain.StockView;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;

/**
 * Created by Emily Li on 21/09/2017.
 */
@Entity
@IdClass(UserStockId.class)
public class UserStock implements StockItem {
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
    public String getSymbol() {
        return symbol;
    }

    @Override
    public String getName() {
        return stockView == null ? null : stockView.getName();
    }

    @Override
    public Date getLastTradeDateTime() {
        return stockView.getLastTradeDateTime();
    }

    @Override
    public BigDecimal getGains() {
        return stockView.getGains();
    }

    @Override
    public BigDecimal getValue() {
        return stockView.getValue();
    }

    @Override
    public BigDecimal getOpenValue() {
        return stockView.getOpenValue();
    }

    @Override
    public BigDecimal getCloseValue() {
        return stockView.getCloseValue();
    }

    @Override
    public int getVolume() {
        return volume;
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

    @Override
    public String toString() {
        return "UserStock{" +
                "username='" + username + '\'' +
                ", symbol='" + symbol + '\'' +
                ", volume=" + volume +
                '}';
    }
}
