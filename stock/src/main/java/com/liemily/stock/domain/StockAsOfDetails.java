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
    private BigDecimal openValue;
    private BigDecimal closeValue;

    BigDecimal getOpenValue() {
        return openValue;
    }

    BigDecimal getCloseValue() {
        return closeValue;
    }
}
