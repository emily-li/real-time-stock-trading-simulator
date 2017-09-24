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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        StockAsOfDetails that = (StockAsOfDetails) o;

        if (symbol != null ? !symbol.equals(that.symbol) : that.symbol != null) return false;
        if (openValue != null ? !openValue.equals(that.openValue) : that.openValue != null) return false;
        if (closeValue != null ? !closeValue.equals(that.closeValue) : that.closeValue != null) return false;
        return stock != null ? stock.equals(that.stock) : that.stock == null;
    }

    @Override
    public int hashCode() {
        int result = symbol != null ? symbol.hashCode() : 0;
        result = 31 * result + (openValue != null ? openValue.hashCode() : 0);
        result = 31 * result + (closeValue != null ? closeValue.hashCode() : 0);
        result = 31 * result + (stock != null ? stock.hashCode() : 0);
        return result;
    }
}
