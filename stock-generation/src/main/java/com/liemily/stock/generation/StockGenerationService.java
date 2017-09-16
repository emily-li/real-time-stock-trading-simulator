package com.liemily.stock.generation;

import com.liemily.stock.StockRepository;
import com.liemily.stock.domain.Stock;
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



    void generateStock(String stockId) {
        Stock stock = new Stock(stockId, new BigDecimal(550));
        stockRepository.save(stock);
    }
}
