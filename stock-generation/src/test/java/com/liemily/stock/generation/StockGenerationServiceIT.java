package com.liemily.stock.generation;

import com.liemily.stock.StockRepository;
import com.liemily.stock.domain.Stock;
import com.liemily.stock.generation.exceptions.StockGenerationException;
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
@RunWith(SpringRunner.class)
@SpringBootTest
public class StockGenerationServiceIT {
    private static final Logger logger = LogManager.getLogger(MethodHandles.lookup().lookupClass());

    @Autowired
    private StockGenerationService stockGenerationService;
    @Autowired
    private StockRepository stockRepository;

    private String stockId;
    private Stock persistedStock;

    @Before
    public void setup() throws Exception {
        stockId = UUID.randomUUID().toString();
        stockGenerationService.generateStock(stockId);
        persistedStock = stockRepository.findOne(stockId);
    }

    @After
    public void tearDown() {
        try {
            stockRepository.delete(stockId);
        } catch (EmptyResultDataAccessException e) {
            logger.info("Failed to delete stock as it was already deleted: " + stockId);
        }
    }

    /**
     * S.S01 - Stocks should be generated with data type number and field ‘Value’
     */
    @Test
    public void testStocksGeneratedWithFieldValue() {
        BigDecimal value = persistedStock.getValue();
        assertNotNull(value);
    }

    /**
     * S.S02 - Stocks should be generated with values between £5 and £550
     */
    @Test
    public void testStocksValuesGeneratedWithRequirementsRange() {
        BigDecimal value = persistedStock.getValue();
        boolean ge5 = value.compareTo(new BigDecimal(5)) >= 0;
        boolean le550 = value.compareTo(new BigDecimal(550)) <= 0;
        assertTrue(ge5);
        assertTrue(le550);
    }

    /**
     * S.S03 - Stocks should be generated with data type number and field ‘Volume’
     */
    @Test
    public void testStocksGeneratedWithFieldVolume() {
        int volume = persistedStock.getVolume();
        assertTrue(volume >= 0);
    }

    /**
     * S.S04 - Stocks should be generated with volumes between 2,000,000 and 1,000,000,000
     */
    @Test
    public void testStocksVolumeGeneratedWithRequirementsRange() {
        int volume = persistedStock.getVolume();
        assertTrue(volume >= 2000000);
        assertTrue(volume <= 1000000000);
    }

    /**
     * S.S05 - Stock generation should fail if the stocks already exist
     */
    @Test(expected = StockGenerationException.class)
    public void testStocksFailIfAlreadyExist() throws StockGenerationException {
        stockGenerationService.generateStock(stockId);
    }

    /**
     * S.S06 - Stocks should have field ‘Open’ with field ‘Value’ as of 0800
     * This test only checks as of functionality for the open field
     *
     * @see StockGenerationServiceTest for assertion check of the time
     */
    @Test
    public void testStocksOpenAsOf() {
        persistedStock = stockRepository.findOne("test");
        BigDecimal openValue = persistedStock.getStockDetails().getOpenValue();
        assertTrue(openValue.compareTo(new BigDecimal(1.5)) == 0);
    }

    /**
     * S.S07 - Stocks should have field ‘Close’ with field ‘Value’ as of 1630
     * This test only checks as of functionality for the close field
     *
     * @see StockGenerationServiceTest for assertion check of the time
     */
    @Test
    public void testStocksCloseAsOf() {

    }

    /**
     * S.S08 - Stocks should have field ‘Gains’, calculated as ‘Value – Open’ when ‘Value’ is updated
     */
    @Test
    public void testStocksGains() {

    }
}
