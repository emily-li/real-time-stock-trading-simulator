package com.liemily.realtimestocktradingsimulator.web.controller;

import com.liemily.stock.domain.Stock;
import com.liemily.stock.repository.StockRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.ui.ExtendedModelMap;
import org.springframework.ui.Model;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.UUID;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Covers automated tests covered by "docs/FDM05-05 Functional Test Plan.doc" for client-side stock functionality
 * Created by Emily Li on 21/09/2017.
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class StockControllerIT {
    @Autowired
    private StockController stockController;
    @Autowired
    private StockRepository stockRepository;
    /**
     * C.S01 Buyable and sellable shares should be presented separately
     *
     * Automatically tests buyable shares can be retrieved individually.
     */
    @Test
    public void testGetBuyableShares() throws Exception {
        Stock stock = new Stock(UUID.randomUUID().toString(), new BigDecimal(1), 1);
        stockRepository.save(stock);

        Model model = new ExtendedModelMap();
        String stockPage = stockController.addBuyableStocks(model);
        Collection<Stock> stocks = (Collection<Stock>) model.asMap().get("stocks");

        assertEquals("stock", stockPage);
        assertTrue(stocks.contains(stock));
    }

    /**
     * C.S01 Buyable and sellable shares should be presented separately
     *
     * Automatically tests sellable shares can be retrieved individually.
     */
    @Test
    public void testGetSellableShares() {
        // Collection<Stock> stocks = stockController.getSellableStocks();

    }

    /**
     * C.S02 Stock data should be ordered by stock symbol by default
     */
    @Test
    public void testGetOrderedStocks() {

    }

    /**
     * C.S03 Stock data should be broadcast in real time
     */
    @Test
    public void testStockDataUpdates() {

    }

    /**
     * C.S06 Stock data should present the following fields as a number: Gains, Value, Volume, Open, Close
     */
    @Test
    public void testStockDataNumberFields() {

    }

    /**
     * C.S07 Stock data should be displayed paginated
     */
    @Test
    public void testPaginatedStocks() {
    }

    /**
     * C.S08 20 stocks should be visible per page by default
     */
    public void test20StocksVisibleByDefault() {

    }

    /**
     * C.S10 Each row of the table should denote a company stock
     */
    @Test
    public void testEachRowIsCompanyStock() {

    }

    /**
     * C.S11 Stock data should be displayed with fields: Stock Symbol, Stock Name, Last Trade, Gains, Value, Volume, Open, Close
     */
    @Test
    public void testStockFields() {

    }

    /**
     * C.S12 Presented stock symbols must be capitals
     */
    @Test
    public void testStockSymbolCapitals() {

    }

    /**
     * C.S13 Last Trade should be presented in format HH:mm:ss
     */
    @Test
    public void testLastTradeFormat() {

    }

    /**
     * C.S14 The user should be able to view all stocks on a single page
     */
    @Test
    public void testViewAllStocks() {

    }

    /**
     * C.S15 The user should be able to search stocks given a stock symbol
     */
    @Test
    public void testSearchStocksBySymbol() {

    }

    /**
     * C.S16 The user should be able to search stocks given a stock name
     */
    @Test
    public void testSearchStocksByName() {

    }

    /**
     * C.S17 The user should be able to search stocks given a gains value, greater or lesser than the variable
     */
    @Test
    public void testSearchStocksByGains() {

    }

    /**
     * C.S18 The user should be able to search stocks given a stock value, greater or lesser than the variable
     */
    @Test
    public void testSearchStocksByValue() {

    }

    /**
     * C.S19 The user should be able to search stocks given a volume, greater or lesser than the variable
     */
    @Test
    public void testSearchStocksByVolume() {

    }

    /**
     * C.S20 The user should be able to order stocks in ascending or descending direction given a field
     */
    @Test
    public void testOrderStocksByAnyField() {

    }

    /**
     * C.S21 Companies with no available stock should be removed from the stock availability list
     */
    @Test
    public void testNoStocksReturnedIfEmptyVolume() {

    }

    /**
     * C.S22 Companies with no stock should reappear to the stock availability list when new stock is available
     */
    @Test
    public void testCompaniesWithStockReturnedWhenPreviouslyEmpty() {

    }
}
