package com.liemily.report;

import com.liemily.stock.domain.StockItem;

import java.util.List;

/**
 * Created by Emily Li on 01/10/2017.
 */
public class Report {
    private List<? extends StockItem> stockDetails;

    public Report(List<? extends StockItem> stockDetails) {
        this.stockDetails = stockDetails;
    }

    public List<? extends StockItem> getStockDetails() {
        return stockDetails;
    }
}
