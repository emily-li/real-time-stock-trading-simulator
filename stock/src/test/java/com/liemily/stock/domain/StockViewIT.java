package com.liemily.stock.domain;

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
@RunWith(SpringRunner.class)
@SpringBootTest
public class StockViewIT {
    @Autowired
    private StockRepository stockRepository;
    @Autowired
    private StockViewRepository stockViewRepository;

    private StockView stockView;

    @Before
    public void setup() {
        String symbol = UUID.randomUUID().toString();
        Stock stock = new Stock(symbol, new BigDecimal(1), 0);
        stockRepository.save(stock);
        stockView = stockViewRepository.findOne(symbol);
    }

    /**
     * S.S08 - Stocks should have field ‘Gains’, calculated as ‘Value – Open’ when ‘Value’ is updated
     */
    @Test
    public void testStocksGains() {
        BigDecimal gains = stockView.getGains();
        assertTrue(gains.compareTo(new BigDecimal("-0.3")) == 0);
    }

    @Test
    public void testNewStockDetailsNull() {
        assertNull(stockView.getGains());
    }
}