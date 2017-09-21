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
    private BigDecimal openValue;
    private BigDecimal closeValue;
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

    public BigDecimal getOpenValue() {
        return openValue;
    }

    public void setOpenValue(BigDecimal openValue) {
        this.openValue = openValue;
    }

    public BigDecimal getStockValue() {
        return stock.getValue();
    }

    public BigDecimal getCloseValue() {
        return closeValue;
    }

    public void setCloseValue(BigDecimal closeValue) {
        this.closeValue = closeValue;
    }
}
