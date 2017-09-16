package com.liemily.stock.generation;

import com.liemily.stock.StockRepository;
import com.liemily.stock.domain.Stock;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.util.UUID;

import static org.junit.Assert.assertNotNull;

/**
 * StockGenerationService Integration Test
 * Interacts with MySQL database for testing
 * Please see "docs/FDM05-05 Functional Test Plan.doc" for documentation
 */
public class StockGenerationServiceIT {
    @Autowired
    private StockGenerationService stockGenerationService;

    @Autowired
    private StockRepository stockRepository;

    private String stockId;

    @Before
    public void setup() {
        stockId = UUID.randomUUID().toString();
    }

    @After
    public void tearDown() {
        stockRepository.delete(stockId);
    }

    /**
     * S.S01 - Stocks should be generated with data type number and field ‘Value’
     */
    @Test
    public void testStocksGeneratedWithFieldValue() {
        stockGenerationService.generateStock(stockId);

        Stock stock = stockRepository.findOne(stockId);
        BigDecimal value = stock.getValue();
        assertNotNull(value);
    }

    /**
     * S.S02 - Stocks should be generated with values between £5 and £550
     */
    @Test
    public void testStocksValuesGeneratedWithRequirementsRange() {

    }

    /**
     * S.S03 - Stocks should be generated with data type number and field ‘Volume’
     */
    @Test
    public void testStocksGeneratedWithFieldVolume() {

    }

    /**
     * S.S04 - Stocks should be generated with volumes between 2,000,000 and 1,000,000,000
     */
    @Test
    public void testStocksVolumeGeneratedWithRequirementsRange() {

    }

    /**
     * S.S05 - Stock generation should fail if the stocks already exist
     */
    @Test
    public void testStocksFailIfAlreadyExist() {

    }

    /**
     * S.S06 - Stocks should have field ‘Open’ with field ‘Value’ as of 0800
     * This test only checks as of functionality for the open field
     *
     * @see StockGenerationServiceTest for mocking of the time
     */
    @Test
    public void testStocksOpenAsOf() {

    }

    /**
     * S.S07 - Stocks should have field ‘Close’ with field ‘Value’ as of 1630
     * This test only checks as of functionality for the close field
     *
     * @see StockGenerationServiceTest for mocking of the time
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