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
public class StockView {
    @Id
    private String symbol;
    private BigDecimal gains;
    private Date lastTradeDateTime;
    private BigDecimal value;
    @OneToOne
    @PrimaryKeyJoinColumn
    private Company company;
    @OneToOne
    @PrimaryKeyJoinColumn
    private Stock stock;
    @OneToOne
    @PrimaryKeyJoinColumn
    private StockAsOfDetails stockAsOfDetails;

    public String getSymbol() {
        return symbol;
    }

    public BigDecimal getGains() {
        return gains;
    }

    public Date getLastTradeDateTime() {
        return lastTradeDateTime;
    }

    public String getName() {
        return company == null ? null : company.getName();
    }

    public BigDecimal getValue() {
        return value;
    }

    public int getVolume() {
        return stock.getVolume();
    }

    public BigDecimal getOpenValue() {
        return stockAsOfDetails == null ? null : stockAsOfDetails.getOpenValue();
    }

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
                && (lastTradeDateTime != null ? lastTradeDateTime.equals(stockView.lastTradeDateTime) : stockView.lastTradeDateTime == null)
                && (company != null ? company.equals(stockView.company) : stockView.company == null)
                && (stock != null ? stock.equals(stockView.stock) : stockView.stock == null)
                && (stockAsOfDetails != null ? stockAsOfDetails.equals(stockView.stockAsOfDetails) : stockView.stockAsOfDetails == null);
    }

    @Override
    public int hashCode() {
        int result = symbol != null ? symbol.hashCode() : 0;
        result = 31 * result + (gains != null ? gains.hashCode() : 0);
        result = 31 * result + (lastTradeDateTime != null ? lastTradeDateTime.hashCode() : 0);
        result = 31 * result + (company != null ? company.hashCode() : 0);
        result = 31 * result + (stock != null ? stock.hashCode() : 0);
        result = 31 * result + (stockAsOfDetails != null ? stockAsOfDetails.hashCode() : 0);
        return result;
    }
}
