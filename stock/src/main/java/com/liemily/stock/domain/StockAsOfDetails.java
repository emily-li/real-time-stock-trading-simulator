package com.liemily.stock.domain;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.math.BigDecimal;

/**
 * Created by Emily Li on 17/09/2017.
 */
@Entity
class StockAsOfDetails {
    @Id
    private String symbol;
    private BigDecimal open;
    private BigDecimal close;

    public BigDecimal getOpen() {
        return open;
    }

    public BigDecimal getClose() {
        return close;
    }
}
