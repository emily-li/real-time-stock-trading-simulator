package com.liemily.stock.modulation;

import com.liemily.stock.domain.Stock;
import com.liemily.stock.service.StockService;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import java.lang.invoke.MethodHandles;
import java.math.BigDecimal;
import java.util.Collection;

/**
 * Created by Emily Li on 23/09/2017.
 */
@Component
@Lazy
class StockModulator implements Runnable {
    private static final Logger logger = LogManager.getLogger(MethodHandles.lookup().lookupClass());
    private StockService stockService;
    private StockModulationRandomiser stockModulationRandomiser;

    @Autowired
    public StockModulator(StockService stockService, StockModulationRandomiser stockModulationRandomiser) {
        this.stockService = stockService;
        this.stockModulationRandomiser = stockModulationRandomiser;
    }

    @Override
    public void run() {
        Collection<Stock> stocks = stockService.getStocks();
        for (Stock stock : stocks) {
            double newValue = modulateValue(stock.getValue().doubleValue());
            stock.setValue(BigDecimal.valueOf(newValue));
        }
        if (!stocks.isEmpty()) {
            stockService.save(stocks);
            logger.info("Updated " + stocks.size() + " stocks");
        }
    }

    private double modulateValue(double initialValue) {
        return initialValue > 0 ? initialValue * stockModulationRandomiser.numberGen() : stockModulationRandomiser.numberGen();
    }
}
