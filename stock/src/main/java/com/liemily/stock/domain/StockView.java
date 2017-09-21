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
public class StockView {
    @Id
    private String symbol;
    private BigDecimal gains;
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

    public BigDecimal getValue() {
        return stock.getValue();
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
}
