package com.liemily.trade.domain;

import com.liemily.user.domain.User;
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
    @CreationTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    private Date tradeDateTime;
    @ManyToOne
    private User user;

    @SuppressWarnings("unused")
    private Trade() {
    }

    public Trade(String stockSymbol) {
        this.stockSymbol = stockSymbol;
    }

    public Date getTradeDateTime() {
        return tradeDateTime;
    }

    public User getUser() {
        return user;
    }
}
