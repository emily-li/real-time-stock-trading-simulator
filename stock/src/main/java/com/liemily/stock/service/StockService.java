package com.liemily.stock.service;

import com.liemily.stock.domain.Stock;
import com.liemily.stock.repository.StockRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;

@Service
@Lazy
public class StockService {
    private StockRepository stockRepository;

    @Autowired
    public StockService(StockRepository stockRepository) {
        this.stockRepository = stockRepository;
    }

    public Stock getStock(String stockSymbol) {
        return stockRepository.findById(stockSymbol).orElse(null);
    }

    public List<Stock> getStocks() {
        return stockRepository.findAll();
    }

    public void save(Stock stock) {
        stockRepository.save(stock);
    }

    public void save(Collection<Stock> stocks) {
        stockRepository.saveAll(stocks);
    }
}
