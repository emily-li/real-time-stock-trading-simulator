package com.liemily.stock.generation;

import org.junit.Test;

/**
 * Long running test to test functionality of MySQL event
 * Intended to update the open and close values of the stocks
 */
public class LongRunningStockDetailsEventIT {

    /**
     * S.S06 - Stocks should have field 'Open' with field 'Value' as of 0800
     *
     * @see StockGenerationServiceIT for integration testing of the as of functionality
     */
    @Test
    public void testStocksOpenAsOf0800() {

    }

    /**
     * S.S07 - Stocks should have field ‘Close’ with field ‘Value’ as of 1630
     *
     * @see StockGenerationServiceIT for integration testing of the as of functionality
     */
    @Test
    public void testStocksCloseAsOf1630() {

    }
}
