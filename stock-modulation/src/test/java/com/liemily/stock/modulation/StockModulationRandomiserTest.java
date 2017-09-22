package com.liemily.stock.modulation;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertNotEquals;

/**
 * StockModulationRandomiser unit testing
 *
 * See "docs/FDM05-05 Functional Test Plan.doc" for more documentation
 */
public class StockModulationRandomiserTest {
    private StockModulationRandomiser stockModulationRandomiser;

    @Before
    public void setup() {
        stockModulationRandomiser = new StockModulationRandomiser();
    }
    /**
     * S.S10 - numberGen() should return a number with randomness
     */
    @Test
    public void testNumberGenRandomness() {
        int firstNumber = stockModulationRandomiser.numberGen();
        int secondNumber = stockModulationRandomiser.numberGen();
        assertNotEquals(firstNumber, secondNumber);
    }

    /**
     * S.S11 - The price creator should run in a separate thread
     */
    @Test
    public void testPriceCreatorRunsInSeparateThread() {

    }
}
