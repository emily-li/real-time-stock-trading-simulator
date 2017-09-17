package com.liemily.stock.generation;

import com.liemily.stock.StockRepository;
import com.liemily.stock.StockViewRepository;
import com.liemily.stock.domain.Stock;
import com.liemily.stock.domain.StockView;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.lang.invoke.MethodHandles;
import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.UUID;
import java.util.concurrent.*;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

/**
 * Long running test to test functionality of MySQL event
 * Intended to update the open and close values of the stocks
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class LongRunningStockDetailsEventIT {
    private static final long TEST_WAIT_BEFORE_EVENT_MS = 60 * 1000;
    private static final BigDecimal EXPECTED_VALUE = new BigDecimal(1);
    private static final Logger logger = LogManager.getLogger(MethodHandles.lookup().lookupClass());

    @Autowired
    private StockViewRepository stockViewRepository;
    @Autowired
    private StockRepository stockRepository;

    private ScheduledExecutorService scheduledExecutorService;
    private String stockSymbol;

    @Before
    public void setup() throws Exception {
        scheduledExecutorService = Executors.newScheduledThreadPool(1);
        stockSymbol = UUID.randomUUID().toString();

        Stock stock = new Stock(stockSymbol, EXPECTED_VALUE, 0);
        stockRepository.save(stock);
    }

    @After
    public void tearDown() {
        stockRepository.delete(stockSymbol);
    }

    /**
     * S.S06 - Stocks should have field 'Open' with field 'Value' as of 0800
     *
     * @see StockGenerationServiceIT for integration testing of the as of functionality
     */
    @Test
    public void testStocksOpenAsOf0800() throws Exception {
        long initialDelay = getScheduleTime(7, 59, 30).getSeconds();

        logger.info("Running StockOpenTester in " + initialDelay + " seconds");
        StockOpenTester stockOpenTester = new StockOpenTester();
        Future<Boolean> testResult = scheduledExecutorService.schedule(stockOpenTester, initialDelay, TimeUnit.SECONDS);
        assertTrue(testResult.get());
    }

    /**
     * S.S07 - Stocks should have field ‘Close’ with field ‘Value’ as of 1630
     *
     * @see StockGenerationServiceIT for integration testing of the as of functionality
     */
    @Test
    public void testStocksCloseAsOf1630() throws Exception {
        long initialDelay = getScheduleTime(16, 29, 30).getSeconds();

        logger.info("Running StockCloseTester in " + initialDelay + " seconds");
        StockCloseTester stockCloseTester = new StockCloseTester();
        Future<Boolean> testResult = scheduledExecutorService.schedule(stockCloseTester, initialDelay, TimeUnit.SECONDS);
        assertTrue(testResult.get());
    }

    private Duration getScheduleTime(int hour, int minute, int second) {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime runTime = LocalDateTime.now().withHour(hour).withMinute(minute).withSecond(second);
        runTime = runTime.compareTo(now) > 0 ? runTime : runTime.plusDays(1);

        return Duration.between(now, runTime);
    }

    private class StockOpenTester implements Callable<Boolean> {
        @Override
        public Boolean call() throws Exception {
            logger.info("Starting " + getClass().getSimpleName());

            StockView stockView = stockViewRepository.findOne(stockSymbol);
            assertNotNull(stockView.getOpen());
            assertTrue(stockView.getOpen().compareTo(EXPECTED_VALUE) == 0);

            logger.info("Successfully asserted open is now " + EXPECTED_VALUE + " at " + LocalDateTime.now());
            return true;
        }
    }

    private class StockCloseTester implements Callable<Boolean> {
        @Override
        public Boolean call() throws Exception {
            logger.info("Starting " + getClass().getSimpleName());

            StockView stockView = stockViewRepository.findOne(stockSymbol);
            assertNotNull(stockView.getClose());
            assertTrue(stockView.getClose().compareTo(EXPECTED_VALUE) == 0);

            logger.info("Successfully asserted close is now " + EXPECTED_VALUE + " at " + LocalDateTime.now());
            return true;
        }
    }
}
