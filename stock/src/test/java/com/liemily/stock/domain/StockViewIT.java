package com.liemily.stock.domain;

import com.liemily.stock.repository.StockAsOfDetailsRepository;
import com.liemily.stock.repository.StockRepository;
import com.liemily.stock.repository.StockViewRepository;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.UUID;

import static org.junit.Assert.assertEquals;

/**
 * Created by Emily Li on 22/09/2017.
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class StockViewIT {
    @Autowired
    private StockViewRepository stockViewRepository;
    @Autowired
    private StockRepository stockRepository;
    @Autowired
    private StockAsOfDetailsRepository stockAsOfDetailsRepository;

    private StockView stockView;

    @Before
    public void setup() {
        String symbol = UUID.randomUUID().toString();
        Stock stock = new Stock(symbol, new BigDecimal(1), 1);
        stockRepository.save(stock);

        StockAsOfDetails stockAsOfDetails = new StockAsOfDetails(stock);
        stockAsOfDetails.setOpenValue(new BigDecimal(1));
        stockAsOfDetails.setCloseValue(new BigDecimal(1));
        stockAsOfDetailsRepository.save(stockAsOfDetails);

        stockView = stockViewRepository.findOne(symbol);
    }

    /**
     * C.S06 Stock data should present the following fields as a number: Gains, Value, Volume, Open, Close
     */
    @Test
    public void testStockDataNumberFields() {
        Collection<BigDecimal> values = new ArrayList<>();
        values.add(stockView.getGains());
        values.add(stockView.getValue());
        values.add(stockView.getOpenValue());
        values.add(stockView.getCloseValue());

        values.forEach(Assert::assertNotNull);
        assertEquals(1, stockView.getVolume());
    }

    /**
     * C.S11 Stock data should be displayed with fields: Stock Symbol, Stock Name, Last Trade, Gains, Value, Volume, Open, Close
     */
    @Test
    public void testStockFields() {
        // TODO
    }
}
