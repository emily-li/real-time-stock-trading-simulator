package com.liemily.stock.domain;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.PrimaryKeyJoinColumn;
import java.math.BigDecimal;

@Entity
public class Stock {
    @Id
    private String symbol;
    private BigDecimal value;
    private int volume;

    @OneToOne
    @PrimaryKeyJoinColumn
    private StockDetails stockDetails;

    private Stock() {
    }

    public Stock(String symbol, BigDecimal value, int volume) {
        this.symbol = symbol;
        this.value = value;
        this.volume = volume;
    }

    public BigDecimal getValue() {
        return value;
    }

    public int getVolume() {
        return volume;
    }

    public StockDetails getStockDetails() {
        return stockDetails;
    }
}
