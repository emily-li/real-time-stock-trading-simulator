package com.liemily.report;

import com.liemily.stock.domain.StockDetails;

import java.util.List;

/**
 * Created by Emily Li on 01/10/2017.
 */
public class Report {
    private List<? extends StockDetails> stockDetails;

    public Report(List<? extends StockDetails> stockDetails) {
        this.stockDetails = stockDetails;
    }

    public List<? extends StockDetails> getStockDetails() {
        return stockDetails;
    }
}
