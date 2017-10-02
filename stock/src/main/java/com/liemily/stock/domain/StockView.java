package com.liemily.stock.domain;

import com.liemily.company.domain.Company;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.PrimaryKeyJoinColumn;
import java.math.BigDecimal;
import java.util.Date;

/**
 * Created by Emily Li on 17/09/2017.
 */
@Entity
public class StockView implements StockItem {
    @Id
    private String symbol;
    private BigDecimal gains;
    private Date lastTradeDateTime;

    @OneToOne
    @PrimaryKeyJoinColumn
    private Company company;

    @OneToOne
    @PrimaryKeyJoinColumn
    private Stock stock;

    @OneToOne
    @PrimaryKeyJoinColumn
    private StockAsOfDetails stockAsOfDetails;

    @Override
    public String getSymbol() {
        return symbol;
    }

    @Override
    public BigDecimal getGains() {
        return gains;
    }

    @Override
    public Date getLastTradeDateTime() {
        return lastTradeDateTime;
    }

    @Override
    public String getName() {
        return company == null ? null : company.getName();
    }

    @Override
    public BigDecimal getValue() {
        return stock.getValue();
    }

    @Override
    public int getVolume() {
        return stock.getVolume();
    }

    @Override
    public BigDecimal getOpenValue() {
        return stockAsOfDetails == null ? null : stockAsOfDetails.getOpenValue();
    }

    @Override
    public BigDecimal getCloseValue() {
        return stockAsOfDetails == null ? null : stockAsOfDetails.getCloseValue();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        StockView stockView = (StockView) o;

        return (symbol != null ? symbol.equals(stockView.symbol) : stockView.symbol == null)
                && (gains != null ? gains.equals(stockView.gains) : stockView.gains == null)
                && (lastTradeDateTime != null ? lastTradeDateTime.equals(stockView.lastTradeDateTime) : stockView.lastTradeDateTime == null);
    }

    @Override
    public int hashCode() {
        int result = symbol != null ? symbol.hashCode() : 0;
        result = 31 * result + (gains != null ? gains.hashCode() : 0);
        result = 31 * result + (lastTradeDateTime != null ? lastTradeDateTime.hashCode() : 0);
        return result;
    }
}
