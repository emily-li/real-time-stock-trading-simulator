package com.liemily.report.domain;

import com.liemily.stock.domain.StockItem;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import java.math.BigDecimal;
import java.util.Date;

@XmlRootElement(name = "Stock")
@XmlAccessorType(XmlAccessType.FIELD)
public class ReportItem implements StockItem {
    private String symbol;
    private String name;
    private BigDecimal value;
    private int volume;
    private Date lastTradeDateTime;
    private BigDecimal gains;
    private BigDecimal open;
    private BigDecimal close;

    private ReportItem() {
    }

    public ReportItem(String symbol, String name, BigDecimal value, int volume, Date lastTradeDateTime, BigDecimal gains, BigDecimal open, BigDecimal close) {
        this.symbol = symbol;
        this.name = name;
        this.value = value;
        this.volume = volume;
        this.lastTradeDateTime = lastTradeDateTime;
        this.gains = gains;
        this.open = open;
        this.close = close;
    }

    @Override
    public String getSymbol() {
        return symbol;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public BigDecimal getValue() {
        return value;
    }

    @Override
    public int getVolume() {
        return volume;
    }

    @Override
    public Date getLastTradeDateTime() {
        return lastTradeDateTime;
    }

    @Override
    public BigDecimal getGains() {
        return gains;
    }

    @Override
    public BigDecimal getOpenValue() {
        return open;
    }

    @Override
    public BigDecimal getCloseValue() {
        return close;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ReportItem that = (ReportItem) o;

        if (volume != that.volume) return false;
        if (symbol != null ? !symbol.equals(that.symbol) : that.symbol != null) return false;
        if (name != null ? !name.equals(that.name) : that.name != null) return false;
        if (value != null ? !value.equals(that.value) : that.value != null) return false;
        if (lastTradeDateTime != null ? !lastTradeDateTime.equals(that.lastTradeDateTime) : that.lastTradeDateTime != null)
            return false;
        if (gains != null ? !gains.equals(that.gains) : that.gains != null) return false;
        if (open != null ? !open.equals(that.open) : that.open != null) return false;
        return close != null ? close.equals(that.close) : that.close == null;
    }

    @Override
    public int hashCode() {
        int result = symbol != null ? symbol.hashCode() : 0;
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (value != null ? value.hashCode() : 0);
        result = 31 * result + volume;
        result = 31 * result + (lastTradeDateTime != null ? lastTradeDateTime.hashCode() : 0);
        result = 31 * result + (gains != null ? gains.hashCode() : 0);
        result = 31 * result + (open != null ? open.hashCode() : 0);
        result = 31 * result + (close != null ? close.hashCode() : 0);
        return result;
    }
}
