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

    public BigDecimal getGains() {
        return gains;
    }

    public BigDecimal getValue() {
        return stock.getValue();
    }

    public int getVolume() {
        return stock.getVolume();
    }

    public BigDecimal getOpen() {
        return stockAsOfDetails.getOpen();
    }

    public BigDecimal getClose() {
        return stockAsOfDetails.getClose();
    }
}
