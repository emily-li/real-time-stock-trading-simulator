package com.liemily.stock.service;

import com.liemily.stock.domain.StockView;
import com.liemily.stock.repository.StockViewRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by Emily Li on 24/09/2017.
 */
@Service
@Lazy
public class StockViewService {
    private StockViewRepository stockViewRepository;

    @Autowired
    public StockViewService(StockViewRepository stockViewRepository) {
        this.stockViewRepository = stockViewRepository;
    }

    public List<StockView> getStocksWithVolume(Pageable pageable) {
        return stockViewRepository.findByStockVolumeGreaterThan(0, pageable);
    }

    public StockView getStockView(String symbol) {
        return stockViewRepository.findOne(symbol);
    }

    public List<StockView> getStocksWithVolumeBySymbol(String symbol, Pageable pageable) {
        return stockViewRepository.findBySymbolContainingIgnoreCaseAndStockVolumeGreaterThan(symbol, 0, pageable);
    }

    public List<StockView> getStocksWithVolumeByName(String name, Pageable stocksPageable) {
        return stockViewRepository.findByCompanyNameContainingIgnoreCaseAndStockVolumeGreaterThan(name, 0, stocksPageable);
    }
}
