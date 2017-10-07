package com.liemily.stock.updater;

import com.liemily.stock.domain.Stock;
import com.liemily.stock.domain.StockView;
import com.liemily.stock.repository.StockAsOfDetailsRepository;
import com.liemily.stock.repository.StockRepository;
import com.liemily.stock.repository.StockViewRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigDecimal;
import java.util.UUID;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

/**
 * Created by Emily Li on 18/09/2017.
 */
@SuppressWarnings("WeakerAccess")
@RunWith(SpringRunner.class)
@SpringBootTest
public class StockViewIT {
    @Autowired
    private StockRepository stockRepository;
    @Autowired
    private StockViewRepository stockViewRepository;
    @Autowired
    private StockAsOfDetailsRepository stockAsOfDetailsRepository;

    private String symbol;
    private StockView stockView;

    @Before
    public void setup() {
        symbol = UUID.randomUUID().toString();
        Stock stock = new Stock(symbol, new BigDecimal(1), 0);
        stockRepository.save(stock);
        stockView = stockViewRepository.findOne(symbol);
    }

    /**
     * S.S08 - Stocks should have field ‘Gains’, calculated as ‘Value – Open’ when ‘Value’ is updated
     */
    @Test
    public void testStocksGains() {
        StockAsOfUpdateRunnable stockAsOfUpdateRunnable = new StockAsOfUpdateRunnable(stockRepository, stockAsOfDetailsRepository, STOCK_AS_OF.OPEN);
        stockAsOfUpdateRunnable.run();

        Stock stock = stockRepository.findOne(symbol);
        stock.setValue(new BigDecimal("0.7"));
        stockRepository.save(stock);

        stockView = stockViewRepository.findOne(symbol);
        BigDecimal gains = stockView.getGains();

        assertTrue(gains.compareTo(new BigDecimal("-0.3")) == 0);
    }

    @Test
    public void testNewStockDetailsNull() {
        assertNull(stockView.getGains());
    }
}
