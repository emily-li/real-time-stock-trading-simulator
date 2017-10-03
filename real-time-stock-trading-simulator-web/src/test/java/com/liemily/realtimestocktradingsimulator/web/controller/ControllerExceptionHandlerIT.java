package com.liemily.realtimestocktradingsimulator.web.controller;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.mockito.Mockito.when;

/**
 * Tests exception handler e-mail functionality
 * <p>
 * Created by Emily Li on 04/10/2017.
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class ControllerExceptionHandlerIT {
    @Mock
    private StocksController stocksController;

    @Before
    public void setup() {
        when(stocksController.getStocksAttribute()).thenThrow(Exception.class);
    }

    /**
     * S.M01 The administrator should receive an e-mail should an exception occur
     */
    @Test
    public void testAdminEmailedOnException() {
        stocksController.getStocksAttribute();
    }

    /**
     * S.M02 The administrator should receive an e-mail should an exception occur with creation date time, logs, and stack trace
     */
    @Test
    public void testAdminEmailContainsExceptionDetails() {

    }
}
