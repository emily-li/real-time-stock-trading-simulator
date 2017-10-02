package com.liemily.stock.service;

import com.liemily.stock.domain.StockView;
import com.liemily.stock.repository.StockViewRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
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

    public List<StockView> getStocksWithVolumeLessThan(int volume, Pageable pageable) {
        return stockViewRepository.findByStockVolumeLessThan(volume, pageable);
    }

    public List<StockView> getStocksWithVolumeGreaterThan(int volume, Pageable pageable) {
        return stockViewRepository.findByStockVolumeGreaterThan(volume, pageable);
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

    public List<StockView> getStocksWithVolumeByName(String name, Pageable pageable) {
        return stockViewRepository.findByCompanyNameContainingIgnoreCaseAndStockVolumeGreaterThan(name, 0, pageable);
    }

    public List<StockView> getStocksWithVolumeByGainsLessThan(BigDecimal gains, Pageable pageable) {
        return stockViewRepository.findByGainsLessThanAndStockVolumeGreaterThan(gains, 0, pageable);
    }

    public List<StockView> getStocksWithVolumeByGainsGreaterThan(BigDecimal gains, Pageable pageable) {
        return stockViewRepository.findByGainsGreaterThanAndStockVolumeGreaterThan(gains, 0, pageable);
    }

    public List<StockView> getStocksWithVolumeByValueLessThan(BigDecimal value, Pageable pageable) {
        return stockViewRepository.findByStockValueLessThanAndStockVolumeGreaterThan(value, 0, pageable);
    }

    public List<StockView> getStocksWithVolumeByValueGreaterThan(BigDecimal value, Pageable pageable) {
        return stockViewRepository.findByStockValueGreaterThanAndStockVolumeGreaterThan(value, 0, pageable);
    }

    public List<StockView> getStocksBySymbol(String[] symbols, Sort sort) {
        return stockViewRepository.findBySymbolIgnoreCaseIn(symbols, sort);
    }

    public List<StockView> getStocks(Sort sort) {
        return stockViewRepository.findAll(sort);
    }
}
