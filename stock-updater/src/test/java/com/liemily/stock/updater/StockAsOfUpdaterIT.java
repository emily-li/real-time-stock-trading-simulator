package com.liemily.stock.updater;

import com.jayway.awaitility.Awaitility;
import com.liemily.stock.domain.Stock;
import com.liemily.stock.domain.StockAsOfDetails;
import com.liemily.stock.repository.StockAsOfDetailsRepository;
import com.liemily.stock.repository.StockRepository;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.lang.invoke.MethodHandles;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import static org.junit.Assert.assertTrue;

/**
 * Created by Emily Li on 19/09/2017.
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class StockAsOfUpdaterIT {
    private static final Logger logger = LogManager.getLogger(MethodHandles.lookup().lookupClass());

    private static int initialDelay = 1;

    @Autowired
    private StockRepository stockRepository;
    @Autowired
    private StockAsOfDetailsRepository stockAsOfDetailsRepository;

    private StockAsOfUpdaterTester stockAsOfUpdater;
    private String updateTimeString;
    private String symbol;
    private Stock stock;

    @Before
    public void setup() throws Exception {
        symbol = UUID.randomUUID().toString();
        stock = new Stock(symbol, new BigDecimal(1), 0);
        stockRepository.save(stock);

        LocalDateTime updateTime = LocalDateTime.now();
        updateTime = updateTime.plusSeconds(initialDelay);
        updateTimeString = updateTime.format(DateTimeFormatter.ofPattern("HH:mm:ss"));
        logger.info("Setting update time for stock " + symbol + " at " + updateTimeString);

        stockAsOfUpdater = new StockAsOfUpdaterTester(stockRepository, stockAsOfDetailsRepository, updateTimeString, updateTimeString);
        new Thread(stockAsOfUpdater).run();
    }

    /**
     * S.S06 - Stocks should have field 'Open' with field 'Value' as of 0800
     *
     * @see StockAsOfUpdaterTest for mocking of the time
     */
    @Test
    public void testStocksOpenUpdatesAtTime() throws Exception {
        BigDecimal expectedValue = new BigDecimal(2);
        updateStock(expectedValue);
        StockAsOfDetails stockAsOfDetails = getUpdatedStockAsOf(STOCK_AS_OF.OPEN);
        assertTrue(stockAsOfDetails.getOpenValue().compareTo(expectedValue) == 0);
    }

    /**
     * S.S07 - Stocks should have field ‘Close’ with field ‘Value’ as of 1630
     *
     * @see StockAsOfUpdaterTest for mocking of the time
     */
    @Test
    public void testStocksCloseUpdatesAtTime() throws Exception {
        BigDecimal expectedValue = new BigDecimal(3);
        updateStock(expectedValue);
        StockAsOfDetails stockAsOfDetails = getUpdatedStockAsOf(STOCK_AS_OF.CLOSE);
        assertTrue(stockAsOfDetails.getCloseValue().compareTo(expectedValue) == 0);
    }

    private void updateStock(BigDecimal newValue) {
        stock.setValue(newValue);
        stockRepository.save(stock);
    }

    private StockAsOfDetails getUpdatedStockAsOf(STOCK_AS_OF stockAsOf) throws Exception {
        Awaitility.await().atMost(initialDelay + 1, TimeUnit.SECONDS).until(new StockAsOfUpdateWaiter(stockAsOf));
        return stockAsOfDetailsRepository.findOne(symbol);
    }

    private class StockAsOfUpdaterTester extends StockAsOfUpdater implements Runnable {
        StockAsOfUpdaterTester(StockRepository stockRepository, StockAsOfDetailsRepository stockAsOfDetailsRepository, String openTime, String closeTime) {
            super(stockRepository, stockAsOfDetailsRepository, openTime, closeTime);
        }

        @Override
        public void run() {
            super.run();
        }
    }

    private class StockAsOfUpdateWaiter implements Runnable {
        private STOCK_AS_OF stockAsOf;

        public StockAsOfUpdateWaiter(STOCK_AS_OF stockAsOf) {
            this.stockAsOf = stockAsOf;
        }

        @Override
        public void run() {
            BigDecimal asOfValue = null;

            while (asOfValue == null) {
                StockAsOfDetails stockAsOfDetails = stockAsOfDetailsRepository.findOne(symbol);
                if (stockAsOfDetails != null) {
                    asOfValue = stockAsOf.equals(STOCK_AS_OF.OPEN) ? stockAsOfDetails.getOpenValue() : stockAsOfDetails.getCloseValue();
                }
            }
        }
    }
}
