package com.liemily.trade.domain;

import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
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
    @CreationTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    private Date tradeDateTime;

    @SuppressWarnings("unused")
    private Trade() {
    }

    public Trade(String stockSymbol, String username) {
        this.stockSymbol = stockSymbol;
        this.username = username;
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

    @Override
    public String toString() {
        return "Trade{" +
                "id=" + id +
                ", stockSymbol='" + stockSymbol + '\'' +
                ", username='" + username + '\'' +
                '}';
    }
}
