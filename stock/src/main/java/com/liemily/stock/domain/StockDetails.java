package com.liemily.stock.domain;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.math.BigDecimal;

/**
 * Created by Emily Li on 17/09/2017.
 */
@Entity
public class StockDetails {
    @Id
    private String symbol;
    private BigDecimal openValue;
    private BigDecimal closeValue;

    public BigDecimal getOpenValue() {
        return openValue;
    }

    public BigDecimal getCloseValue() {
        return closeValue;
    }
}
