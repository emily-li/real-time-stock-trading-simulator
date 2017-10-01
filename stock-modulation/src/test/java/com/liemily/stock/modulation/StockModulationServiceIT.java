package com.liemily.stock.modulation;

import com.liemily.stock.domain.Stock;
import com.liemily.stock.repository.StockRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigDecimal;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import static com.jayway.awaitility.Awaitility.await;
import static org.junit.Assert.assertTrue;

/**
 * StockModulationService integration testing
 * Tests with the underlying database
 *
 * See "docs/FDM05-05 Functional Test Plan.doc" for more documentation
 */
@SuppressWarnings("WeakerAccess")
@RunWith(SpringRunner.class)
@SpringBootTest
public class StockModulationServiceIT {
    @Autowired
    private StockModulationService stockModulationService;
    @Autowired
    private StockRepository stockRepository;
    @Value("${stock.modulation.rateMs}")
    private int updateRateMs;

    private String symbol;

    @Before
    public void setup() {
        symbol = UUID.randomUUID().toString();
        Stock stock = new Stock(symbol, new BigDecimal(1), 0);
        stockRepository.save(stock);
    }

    /**
     * S.S09 - Stocks should have field ‘Value’, calculated as ‘Value * NumberGen()’ every 5 minutes
     */
    @Test
    public void testStocksModulation() throws Exception {
        BigDecimal prevValue = stockRepository.findOne(symbol).getValue();
        for (int i = 0; i < 3; i++) {
            await().atMost(updateRateMs + 2000, TimeUnit.MILLISECONDS).until(new StockChangeWaiter(prevValue));
            BigDecimal currValue = stockRepository.findOne(symbol).getValue();
            assertTrue(prevValue.compareTo(currValue) != 0);
            prevValue = stockRepository.findOne(symbol).getValue();
        }
    }

    private class StockChangeWaiter implements Runnable {
        private BigDecimal prevValue;

        public StockChangeWaiter(BigDecimal prevValue) {
            this.prevValue = prevValue;
        }

        @Override
        public void run() {
            BigDecimal currValue = stockRepository.findOne(symbol).getValue();
            while (prevValue.compareTo(currValue) == 0) {
                currValue = stockRepository.findOne(symbol).getValue();
            }
        }
    }
}
