package com.liemily.stock.domain;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.PrimaryKeyJoinColumn;
import java.math.BigDecimal;

/**
 * Created by Emily Li on 17/09/2017.
 */
@Entity
public class StockAsOfDetails {
    @Id
    private String symbol;
    private BigDecimal open;
    private BigDecimal close;
    @OneToOne
    @PrimaryKeyJoinColumn
    private Stock stock;

    private StockAsOfDetails() {
    }

    public StockAsOfDetails(Stock stock) {
        this.symbol = stock.getSymbol();
        this.stock = stock;
    }

    public String getSymbol() {
        return symbol;
    }

    public BigDecimal getOpen() {
        return open;
    }

    public BigDecimal getClose() {
        return close;
    }

    public BigDecimal getStockValue() {
        return stock.getValue();
    }

    public void setOpen(BigDecimal open) {
        this.open = open;
    }

    public void setClose(BigDecimal close) {
        this.close = close;
    }
}
