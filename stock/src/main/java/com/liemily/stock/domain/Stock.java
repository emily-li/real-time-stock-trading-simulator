package com.liemily.stock.domain;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.math.BigDecimal;

@Entity
public class Stock {
    @Id
    private String symbol;
    private BigDecimal value;

    public BigDecimal getValue() {
        return value;
    }
}
