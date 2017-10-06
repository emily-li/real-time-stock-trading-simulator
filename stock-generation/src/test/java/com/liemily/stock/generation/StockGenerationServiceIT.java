package com.liemily.stock.generation;

import com.liemily.stock.domain.StockView;
import com.liemily.stock.generation.exceptions.StockGenerationException;
import com.liemily.stock.repository.StockRepository;
import com.liemily.stock.repository.StockViewRepository;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.test.context.junit4.SpringRunner;

import java.lang.invoke.MethodHandles;
import java.math.BigDecimal;
import java.util.UUID;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

/**
 * StockGenerationService Integration Test
 * Interacts with MySQL database for testing
 * Please see "docs/FDM05-05 Functional Test Plan.doc" for documentation
 */
@SuppressWarnings("WeakerAccess")
@RunWith(SpringRunner.class)
@SpringBootTest
public class StockGenerationServiceIT {
    private static final Logger logger = LogManager.getLogger(MethodHandles.lookup().lookupClass());

    @Autowired
    private StockGenerationService stockGenerationService;
    @Autowired
    private StockRepository stockRepository;
    @Autowired
    private StockViewRepository stockViewRepository;

    private String stockId;
    private StockView persistedStockView;

    @Before
    public void setup() throws Exception {
        stockId = UUID.randomUUID().toString();
        stockGenerationService.generateStock(stockId);
        persistedStockView = stockViewRepository.findById(stockId).orElse(null);
    }

    @After
    public void tearDown() {
        try {
            stockRepository.deleteById(stockId);
        } catch (EmptyResultDataAccessException e) {
            logger.info("Failed to delete stock as it was already deleted: " + stockId);
        }
    }

    /**
     * S.S01 - Stocks should be generated with data type number and field ‘Value’
     */
    @Test
    public void testStocksGeneratedWithFieldValue() {
        BigDecimal value = persistedStockView.getValue();
        assertNotNull(value);
    }

    /**
     * S.S02 - Stocks should be generated with values between £5 and £550
     */
    @Test
    public void testStocksValuesGeneratedWithRequirementsRange() throws Exception {
        BigDecimal prevValue = null;
        for (int i = 0; i < 5; i++) {
            assert persistedStockView != null;
            BigDecimal value = persistedStockView.getValue();
            boolean ge5 = value.compareTo(new BigDecimal(5)) >= 0;
            boolean le550 = value.compareTo(new BigDecimal(550)) <= 0;

            assertTrue(ge5);
            assertTrue(le550);
            if (prevValue != null) {
                assertTrue(prevValue.compareTo(value) != 0);
            }

            prevValue = value;
            stockId = UUID.randomUUID().toString();
            stockGenerationService.generateStock(stockId);
            persistedStockView = stockViewRepository.findById(stockId).orElse(null);
        }
    }

    /**
     * S.S03 - Stocks should be generated with data type number and field ‘Volume’
     */
    @Test
    public void testStocksGeneratedWithFieldVolume() {
        int volume = persistedStockView.getVolume();
        assertTrue(volume >= 0);
    }

    /**
     * S.S04 - Stocks should be generated with volumes between 2,000,000 and 1,000,000,000
     */
    @Test
    public void testStocksVolumeGeneratedWithRequirementsRange() throws Exception {
        Integer prevVol = null;
        for (int i = 0; i < 5; i++) {
            assert persistedStockView != null;
            int volume = persistedStockView.getVolume();

            assertTrue(volume >= 2000000);
            assertTrue(volume <= 1000000000);
            if (prevVol != null) {
                assertTrue(volume - prevVol != 0);
            }

            prevVol = volume;
            stockId = UUID.randomUUID().toString();
            stockGenerationService.generateStock(stockId);
            persistedStockView = stockViewRepository.findById(stockId).orElse(null);
        }
    }

    /**
     * S.S05 - Stock generation should fail if the stocks already exist
     */
    @Test(expected = StockGenerationException.class)
    public void testStocksFailIfAlreadyExist() throws StockGenerationException {
        stockGenerationService.generateStock(stockId);
    }
}
