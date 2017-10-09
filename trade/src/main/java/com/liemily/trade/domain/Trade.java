package com.liemily.trade.domain;

import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;

/**
 * Created by Emily Li on 22/09/2017.
 */
@Entity
public class Trade {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;
    private String stockSymbol;
    private String username;
    private BigDecimal value;
    private int volume;
    @CreationTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    private Date tradeDateTime;

    @SuppressWarnings("unused")
    private Trade() {
    }

    public Trade(String stockSymbol, String username, BigDecimal value, int volume) {
        this.stockSymbol = stockSymbol;
        this.username = username;
        this.value = value;
        this.volume = volume;
    }

    public Date getTradeDateTime() {
        return tradeDateTime;
    }

    public String getUsername() {
        return username;
    }

    public String getStockSymbol() {
        return stockSymbol;
    }

    public int getVolume() {
        return volume;
    }

    // Setters required for AJAX calls
    public void setStockSymbol(String stockSymbol) {
        this.stockSymbol = stockSymbol;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setVolume(int volume) {
        this.volume = volume;
    }

    public BigDecimal getValue() {
        return value;
    }

    @Override
    public String toString() {
        return "Trade{" +
                "stockSymbol='" + stockSymbol + '\'' +
                ", username='" + username + '\'' +
                ", volume=" + volume +
                '}';
    }

    public void setValue(BigDecimal value) {
        this.value = value;
    }
}
