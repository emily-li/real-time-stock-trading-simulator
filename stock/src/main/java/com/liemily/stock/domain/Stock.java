package com.liemily.stock.domain;

import com.liemily.company.domain.Company;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.PrimaryKeyJoinColumn;
import java.math.BigDecimal;

@Entity
public class Stock {
    @Id
    private String symbol;
    private BigDecimal value;
    private int volume;
    @OneToOne
    @PrimaryKeyJoinColumn
    private Company company;

    @SuppressWarnings("unused")
    private Stock() {
    }

    public Stock(String symbol, BigDecimal value, int volume) {
        this.symbol = symbol;
        this.value = value;
        this.volume = volume;
    }

    public String getSymbol() {
        return symbol;
    }

    public BigDecimal getValue() {
        return value;
    }

    public int getVolume() {
        return volume;
    }

    public void setValue(BigDecimal value) {
        this.value = value;
    }

    public String getName() {
        return company.getName();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Stock stock = (Stock) o;

        return stock.value.compareTo(value) == 0 && volume == stock.volume && (symbol != null ? symbol.equals(stock.symbol) : stock.symbol == null);
    }

    @Override
    public int hashCode() {
        int result;
        long temp;
        result = symbol != null ? symbol.hashCode() : 0;
        temp = Double.doubleToLongBits(value.doubleValue());
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        result = 31 * result + volume;
        return result;
    }
}
