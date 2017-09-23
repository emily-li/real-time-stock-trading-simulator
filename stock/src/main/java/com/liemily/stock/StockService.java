package com.liemily.stock;

import com.liemily.stock.domain.Stock;
import com.liemily.stock.repository.StockRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.Pageable;
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

    public List<Stock> getStocks() {
        return stockRepository.findAll();
    }

    public List<Stock> getStocksWithVolume() {
        return stockRepository.findStocksWithVolume();
    }

    public List<Stock> getStocksWithVolume(Pageable pageable) {
        return stockRepository.findStocksWithVolume(pageable);
    }

    public void save(Stock stock) {
        stockRepository.save(stock);
    }

    public void save(Collection<Stock> stocks) {
        stockRepository.save(stocks);
    }
}
