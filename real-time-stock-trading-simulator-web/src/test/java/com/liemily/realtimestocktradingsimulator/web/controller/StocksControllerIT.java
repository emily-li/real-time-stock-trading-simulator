package com.liemily.realtimestocktradingsimulator.web.controller;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.embedded.LocalServerPort;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.assertTrue;

/**
 * Created by Emily Li on 08/10/2017.
 */
@SuppressWarnings({"ConstantConditions", "WeakerAccess"})
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class StocksControllerIT {
    @LocalServerPort
    private int port;
    @Autowired
    private TestRestTemplate restTemplate;

    /**
     * C.S11 Stock data should be displayed with fields: Stock Symbol, Stock Name, Last Trade, Gains, Value, Volume, Open, Close
     */
    @Test
    public void testStockFieldNamess() {
        String stockURL = "http://localhost:" + port + "/stock/buy";
        String stockPageContents = restTemplate.getForObject(stockURL, String.class);

        assertTrue(stockPageContents.contains("Stock Symbol"));
        assertTrue(stockPageContents.contains("Stock Name"));
        assertTrue(stockPageContents.contains("Last Trade"));
        assertTrue(stockPageContents.contains("Gains"));
        assertTrue(stockPageContents.contains("Value"));
        assertTrue(stockPageContents.contains("Open"));
        assertTrue(stockPageContents.contains("Close"));
    }
}
