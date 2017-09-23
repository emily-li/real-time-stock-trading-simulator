package com.liemily.stock.modulation;

import com.liemily.stock.StockService;
import org.junit.Before;
import org.junit.Test;

import java.util.concurrent.Executors;

import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;

/**
 * Created by Emily Li on 22/09/2017.
 */
public class StockModulationServiceTest {
    private static final int SERVICE_RUN_TIME_MS = 1000;
    private StockModulationService stockModulationService;
    private StockService stockService;
    private StockModulationRandomiser stockModulationRandomiser;

    @Before
    public void setup() {
        stockService = mock(StockService.class);
        stockModulationRandomiser = mock(StockModulationRandomiser.class);
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
        public TestStockModulator(StockService stockService, StockModulationRandomiser stockModulationRandomiser) {
            super(stockService, stockModulationRandomiser);
        }

        @Override
        public void run() {
            try {
                super.run();
                Thread.sleep(SERVICE_RUN_TIME_MS);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }
}
