package com.liemily.realtimestocktradingsimulator.web.controller;

import com.liemily.stock.domain.Stock;
import com.liemily.stock.repository.StockRepository;
import com.liemily.user.domain.UserStock;
import com.liemily.user.repository.UserStockRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.ui.ExtendedModelMap;
import org.springframework.ui.Model;

import java.math.BigDecimal;
import java.nio.file.attribute.UserPrincipal;
import java.security.Principal;
import java.util.Collection;
import java.util.List;
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
    private static final String STOCKS_ATTRIBUTE = "stocks";
    @Autowired
    private StockController stockController;
    @Autowired
    private StockRepository stockRepository;
    @Autowired
    private UserStockRepository userStockRepository;

    private Stock stock;
    private Model model;

    @Before
    public void setup() {
        stock = new Stock(UUID.randomUUID().toString(), new BigDecimal(1), 1);
        stockRepository.save(stock);

        model = new ExtendedModelMap();
    }

    /**
     * C.S01 Buyable and sellable shares should be presented separately
     *
     * Automatically tests buyable shares can be retrieved individually.
     */
    @Test
    public void testGetBuyableShares() throws Exception {
        String stockPage = stockController.addBuyableStocks(model);
        Collection<Stock> stocks = (Collection<Stock>) model.asMap().get(stockController.getStocksAttribute());

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
        String username = UUID.randomUUID().toString();
        UserStock userStock = new UserStock(username, stock.getSymbol(), 1);
        userStockRepository.save(userStock);

        Principal principal = (UserPrincipal) () -> username;
        String stockPage = stockController.addSellableStocks(model, principal);
        Collection<UserStock> stocks = (Collection<UserStock>) model.asMap().get(STOCKS_ATTRIBUTE);

        assertEquals("stock", stockPage);
        assertTrue(stocks.contains(userStock));
    }

    /**
     * C.S02 Stock data should be ordered by stock symbol by default
     */
    @Test
    public void testGetOrderedStocks() {
        Stock stock2 = new Stock("b" + UUID.randomUUID(), new BigDecimal(9), 9);
        Stock stock1 = new Stock("a" + UUID.randomUUID(), new BigDecimal(10), 10);
        stockRepository.save(stock1);
        stockRepository.save(stock2);

        stockController.addBuyableStocks(model);
        List<Stock> stocks = (List<Stock>) model.asMap().get(STOCKS_ATTRIBUTE);
        Integer stock1Idx = null;
        Integer stock2Idx = null;


        for (int i = 0; i < stocks.size(); i++) {
            if (stocks.get(i).getSymbol().equals(stock1.getSymbol())) {
                stock1Idx = i;
            } else if (stocks.get(i).getSymbol().equals(stock2.getSymbol())) {
                stock2Idx = i;
            }
            if (stock1Idx != null && stock2Idx != null) {
                break;
            }
        }
        assertTrue(stock2Idx > stock1Idx);
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
