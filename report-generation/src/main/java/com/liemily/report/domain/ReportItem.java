package com.liemily.report.domain;

import com.liemily.stock.domain.StockItem;
import com.opencsv.bean.CsvBindByPosition;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import java.math.BigDecimal;
import java.util.Date;

@XmlAccessorType(XmlAccessType.FIELD)
public class ReportItem implements StockItem {
    private static final String CSV_HEADER = "\"symbol\",\"name\",\"value\",\"volume\",\"gains\"";

    @CsvBindByPosition(position = 0)
    private String symbol;
    @CsvBindByPosition(position = 1)
    private String name;
    @CsvBindByPosition(position = 2)
    private BigDecimal value;
    @CsvBindByPosition(position = 3)
    private int volume;
    @CsvBindByPosition(position = 4)
    private BigDecimal gains;
    private Date lastTradeDateTime;
    private BigDecimal open;
    private BigDecimal close;

    public ReportItem() {
    }

    public ReportItem(String symbol, String name, BigDecimal value, int volume, BigDecimal gains) {
        this.symbol = symbol;
        this.name = name;
        this.value = value;
        this.volume = volume;
        this.gains = gains;
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

    public static String getCsvHeader() {
        return CSV_HEADER;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ReportItem that = (ReportItem) o;

        return volume == that.volume
                && (symbol != null ? symbol.equals(that.symbol) : that.symbol == null)
                && (name != null ? name.equals(that.name) : that.name == null)
                && (value != null ? value.equals(that.value) : that.value == null)
                && (lastTradeDateTime != null ? lastTradeDateTime.equals(that.lastTradeDateTime) : that.lastTradeDateTime == null)
                && (gains != null ? gains.equals(that.gains) : that.gains == null) && (open != null ? open.equals(that.open) : that.open == null)
                && (close != null ? close.equals(that.close) : that.close == null);
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
