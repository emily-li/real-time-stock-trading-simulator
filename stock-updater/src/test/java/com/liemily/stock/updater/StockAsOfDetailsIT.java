package com.liemily.stock.updater;

import com.liemily.stock.domain.Stock;
import com.liemily.stock.domain.StockAsOfDetails;
import com.liemily.stock.repository.StockAsOfDetailsRepository;
import com.liemily.stock.repository.StockRepository;
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
 * Created by Emily Li on 19/09/2017.
 */
@SuppressWarnings("WeakerAccess")
@RunWith(SpringRunner.class)
@SpringBootTest
public class StockAsOfDetailsIT {
    private StockAsOfUpdateRunnable stockAsOfUpdateRunnable;
    @Autowired
    private StockAsOfDetailsRepository stockAsOfDetailsRepository;
    @Autowired
    private StockRepository stockRepository;
    private StockAsOfDetails stockAsOfDetails;
    private String symbol;

    @Before
    public void setup() {
        symbol = UUID.randomUUID().toString();
        Stock stock = new Stock(symbol, new BigDecimal(1), 0);
        stockRepository.save(stock);

        stockAsOfUpdateRunnable = new StockAsOfUpdateRunnable(stockRepository, stockAsOfDetailsRepository, null);
        stockAsOfUpdateRunnable.insertNewStocks();
        stockAsOfDetails = stockAsOfDetailsRepository.findOne(symbol);
    }

    /**
     * S.S06 - Stocks should have field ‘Open’ with field ‘Value’
     */
    @Test
    public void testStocksOpenAsOf() {
        stockAsOfUpdateRunnable = new StockAsOfUpdateRunnable(stockRepository, stockAsOfDetailsRepository, STOCK_AS_OF.OPEN);
        stockAsOfUpdateRunnable.updateAsOf();
        stockAsOfDetails = stockAsOfDetailsRepository.findOne(symbol);
        BigDecimal open = stockAsOfDetails.getOpenValue();
        assertTrue(open.compareTo(new BigDecimal(1)) == 0);
    }

    /**
     * S.S07 - Stocks should have field ‘Close’ with field ‘Value’
     */
    @Test
    public void testStocksCloseAsOf() {
        stockAsOfUpdateRunnable = new StockAsOfUpdateRunnable(stockRepository, stockAsOfDetailsRepository, STOCK_AS_OF.CLOSE);
        stockAsOfUpdateRunnable.updateAsOf();
        stockAsOfDetails = stockAsOfDetailsRepository.findOne(symbol);
        BigDecimal close = stockAsOfDetails.getCloseValue();
        assertTrue(close.compareTo(new BigDecimal(1)) == 0);
    }

    @Test
    public void testNewStockDetailsNull() {
        assertNull(stockAsOfDetails.getOpenValue());
        assertNull(stockAsOfDetails.getCloseValue());
    }
}
