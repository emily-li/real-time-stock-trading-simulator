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

    public Stock(String symbol, BigDecimal value, int volume) {
        this.symbol = symbol;
        this.value = value;
        this.volume = volume;
    }

    public String getSymbol() {
        return symbol;
    }

    public BigDecimal getValue() {
        return value;
    }

    public int getVolume() {
        return volume;
    }

    public void setValue(BigDecimal value) {
        this.value = value;
    }
}
