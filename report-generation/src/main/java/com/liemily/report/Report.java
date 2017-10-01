package com.liemily.report;

import com.liemily.stock.domain.StockView;

import java.util.List;

/**
 * Created by Emily Li on 01/10/2017.
 */
public class Report {
    private List<StockView> stockViews;

    public Report(List<StockView> stockViews) {
        this.stockViews = stockViews;
    }

    public List<StockView> getStockViews() {
        return stockViews;
    }
}
