package com.liemily.stock.modulation;

import com.liemily.stock.service.StockService;
import org.junit.Before;
import org.junit.Test;

import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import static com.jayway.awaitility.Awaitility.await;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;

/**
 * Created by Emily Li on 22/09/2017.
 */
public class StockModulationServiceTest {
    private static final int SERVICE_RUN_TIME_MS = 1000;
    private StockModulationService stockModulationService;

    @Before
    public void setup() {
        StockService stockService = mock(StockService.class);
        StockModulationRandomiser stockModulationRandomiser = mock(StockModulationRandomiser.class);
        StockModulator stockModulator = new TestStockModulator(stockService, stockModulationRandomiser);
        stockModulationService = new StockModulationService(Executors.newSingleThreadScheduledExecutor(), stockModulator);
    }

    /**
     * S.S11 - The price creator should run in a separate thread
     */
    @Test
    public void testPriceCreatorRunsInSeparateThread() {
        long startTime = System.currentTimeMillis();
        stockModulationService.run();
        long endTime = System.currentTimeMillis();
        assertTrue(endTime - startTime < SERVICE_RUN_TIME_MS);
    }

    private class TestStockModulator extends StockModulator {
        TestStockModulator(StockService stockService, StockModulationRandomiser stockModulationRandomiser) {
            super(stockService, stockModulationRandomiser);
        }

        @Override
        public void run() {
            try {
                super.run();
                await().atMost(SERVICE_RUN_TIME_MS, TimeUnit.MILLISECONDS);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }
}
