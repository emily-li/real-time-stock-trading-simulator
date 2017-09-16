package com.liemily.stock.generation;

import com.liemily.stock.StockRepository;
import com.liemily.stock.domain.Stock;
import com.liemily.stock.generation.exceptions.StockGenerationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
@Lazy
public class StockGenerationService {
    private StockRepository stockRepository;

    @Autowired
    public StockGenerationService(StockRepository stockRepository) {
        this.stockRepository = stockRepository;
    }

    void generateStock(String stockId) throws StockGenerationException {
        if (stockRepository.exists(stockId)) {
            throw new StockGenerationException("Stock already exists: " + stockId);
        }
        Stock stock = new Stock(stockId, new BigDecimal(550), 2500000);
        stockRepository.save(stock);
    }
}
