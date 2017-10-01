package com.liemily.stock.modulation;

import com.liemily.stock.domain.Stock;
import com.liemily.stock.service.StockService;
import org.junit.Before;
import org.junit.Test;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.mockito.internal.verification.VerificationModeFactory.times;

/**
 * Created by Emily Li on 23/09/2017.
 */
@SuppressWarnings("WeakerAccess")
public class StockModulatorTest {
    private StockModulator stockModulator;
    private StockService stockService;
    private StockModulationRandomiser stockModulationRandomiser;

    @Before
    public void setup() {
        stockService = mock(StockService.class);
        stockModulationRandomiser = mock(StockModulationRandomiser.class);
        stockModulator = new StockModulator(stockService, stockModulationRandomiser);
    }

    /**
     * Test StockModulationRandomiser.numberGen() is called for modulation
     */
    @Test
    public void testNumberGenUsed() throws Exception {
        Stock stock = new Stock("", new BigDecimal(1), 0);
        List<Stock> stocks = Collections.singletonList(stock);
        when(stockService.getStocks()).thenReturn(stocks);
        stockModulator.run();
        verify(stockModulationRandomiser, times(1)).numberGen();
    }
}
