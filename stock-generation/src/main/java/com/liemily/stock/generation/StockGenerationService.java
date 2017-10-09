package com.liemily.stock.generation;

import com.liemily.stock.domain.Stock;
import com.liemily.stock.generation.exceptions.StockGenerationException;
import com.liemily.stock.repository.StockRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

@Component
@Lazy
public class StockGenerationService {
    private StockRepository stockRepository;
    private StockGenerationRandomiser stockGenerationRandomiser;

    @Autowired
    public StockGenerationService(StockRepository stockRepository, StockGenerationRandomiser stockGenerationRandomiser) {
        this.stockRepository = stockRepository;
        this.stockGenerationRandomiser = stockGenerationRandomiser;
    }

    public void generateStock(String stockId) throws StockGenerationException {
        if (stockRepository.exists(stockId)) {
            throw new StockGenerationException("Stock already exists: " + stockId);
        } else {
            Stock stock = stockGenerationRandomiser.randomise(stockId);
            stockRepository.save(stock);
        }
    }
}
