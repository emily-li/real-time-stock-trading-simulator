package com.liemily.stock.updater;

import com.liemily.stock.domain.Stock;
import com.liemily.stock.domain.StockAsOfDetails;
import com.liemily.stock.repository.StockAsOfDetailsRepository;
import com.liemily.stock.repository.StockRepository;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.transaction.annotation.Transactional;

import java.lang.invoke.MethodHandles;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Emily Li on 18/09/2017.
 */
class StockAsOfUpdateRunnable implements Runnable {
    private static final Logger logger = LogManager.getLogger(MethodHandles.lookup().lookupClass());
    private final StockRepository stockRepository;
    private final StockAsOfDetailsRepository stockAsOfDetailsRepository;
    private final STOCK_AS_OF asOf;

    StockAsOfUpdateRunnable(StockRepository stockRepository, StockAsOfDetailsRepository stockAsOfDetailsRepository, STOCK_AS_OF asOf) {
        this.stockRepository = stockRepository;
        this.stockAsOfDetailsRepository = stockAsOfDetailsRepository;
        this.asOf = asOf;
    }

    @Override
    public void run() {
        logger.info("Running update for " + asOf);

        insertNewStocks();
        updateAsOf();

        logger.info("Ran update for " + asOf);
    }

    @Transactional
    public void updateAsOf() {
        List<StockAsOfDetails> stockAsOfDetails = stockAsOfDetailsRepository.findAll();

        for (StockAsOfDetails stockAsOfs : stockAsOfDetails) {
            BigDecimal stockValue = stockAsOfs.getStockValue();
            if (asOf.equals(STOCK_AS_OF.OPEN)) {
                stockAsOfs.setOpenValue(stockValue);
            } else if (asOf.equals(STOCK_AS_OF.CLOSE)) {
                stockAsOfs.setCloseValue(stockValue);
            }
        }
        stockAsOfDetailsRepository.save(stockAsOfDetails);
    }

    @Transactional
    public void insertNewStocks() {
        List<Stock> stocks = stockRepository.findAll();
        Map<String, Stock> stocksSymbolsMap = new HashMap<>();
        stocks.forEach(stock -> stocksSymbolsMap.put(stock.getSymbol(), stock));
        List<StockAsOfDetails> stockAsOfDetails = stockAsOfDetailsRepository.findAll();
        List<String> stockAsOfDetailsSymbols = new ArrayList<>();
        stockAsOfDetails.stream().map(StockAsOfDetails::getSymbol).forEach(stockAsOfDetailsSymbols::add);

        List<String> stocksSymbols = new ArrayList<>(stocksSymbolsMap.keySet());
        stocksSymbols.removeAll(stockAsOfDetailsSymbols);
        List<StockAsOfDetails> newStockAsOfDetails = new ArrayList<>();
        stocksSymbols.forEach(stockSymbol -> newStockAsOfDetails.add(new StockAsOfDetails(stocksSymbolsMap.get(stockSymbol))));

        stockAsOfDetailsRepository.save(newStockAsOfDetails);
    }
}
