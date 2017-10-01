package com.liemily.stock.domain;

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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * Tests StockView expected values and required fields
 *
 * Created by Emily Li on 22/09/2017.
 */
@SuppressWarnings("WeakerAccess")
@RunWith(SpringRunner.class)
@SpringBootTest
public class StockViewIT {
    private static final String SYMBOL = "testSymbol";
    private static final String NAME = "testName";

    @Autowired
    private StockViewRepository stockViewRepository;

    private StockView stockView;

    @Before
    public void setup() {
        stockView = stockViewRepository.findOne(SYMBOL);
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
     *
     * The number fields are verified in testStockDataNumberFields()
     */
    @Test
    public void testStockFields() {
        assertEquals(SYMBOL, stockView.getSymbol());
        assertEquals(NAME, stockView.getName());
        assertNotNull(stockView.getLastTradeDateTime());
    }
}
