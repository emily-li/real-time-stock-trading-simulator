package com.liemily.stock.domain;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.math.BigDecimal;

@Entity
public class Stock {
    @Id
    private String symbol;
    private BigDecimal value;
    private int volume;

    private Stock() {
    }

    public Stock(String symbol, BigDecimal value) {
        this.symbol = symbol;
        this.value = value;
    }

    public BigDecimal getValue() {
        return value;
    }

    public int getVolume() {
        return volume;
    }
}
