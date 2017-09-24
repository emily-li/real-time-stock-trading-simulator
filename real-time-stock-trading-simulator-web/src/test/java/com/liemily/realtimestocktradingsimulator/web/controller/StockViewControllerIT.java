package com.liemily.realtimestocktradingsimulator.web.controller;

import com.liemily.stock.domain.Stock;
import com.liemily.stock.repository.StockRepository;
import com.liemily.user.domain.UserStock;
import com.liemily.user.repository.UserStockRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.embedded.LocalServerPort;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.ui.ExtendedModelMap;
import org.springframework.ui.Model;

import java.math.BigDecimal;
import java.nio.file.attribute.UserPrincipal;
import java.security.Principal;
import java.util.*;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Covers automated tests covered by "docs/FDM05-05 Functional Test Plan.doc" for client-side stock functionality
 * Created by Emily Li on 21/09/2017.
 */
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class StockViewControllerIT {
    @LocalServerPort
    private int port;
    @Autowired
    private TestRestTemplate restTemplate;
    @Autowired
    private StockViewController stockViewController;

    @Autowired
    private StockRepository stockRepository;
    @Autowired
    private UserStockRepository userStockRepository;

    private String stockURL;
    private Model model;
    private String username;
    private Principal principal;

    private Stock stock;

    @Before
    public void setup() {
        stock = new Stock(UUID.randomUUID().toString(), new BigDecimal(1), 1);
        stockRepository.save(stock);

        model = new ExtendedModelMap();

        username = UUID.randomUUID().toString();
        principal = (UserPrincipal) () -> username;

        stockURL = "http://localhost:" + port + "/stock";
    }

    /**
     * C.S01 Buyable and sellable shares should be presented separately
     *
     * Automatically tests buyable shares can be retrieved individually.
     */
    @Test
    public void testGetBuyableShares() throws Exception {
        String stockPage = stockViewController.getBuyableStocks(model, null);
        Collection<Stock> stocks = (Collection<Stock>) model.asMap().get(stockViewController.getStocksAttribute());

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
        UserStock userStock = new UserStock(username, stock.getSymbol(), 1);
        userStockRepository.save(userStock);

        String stockPage = stockViewController.getSellableStocks(model, principal, null);
        Collection<UserStock> stocks = (Collection<UserStock>) model.asMap().get(stockViewController.getStocksAttribute());

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

        stockViewController.getBuyableStocks(model, null);
        List<Stock> stocks = (List<Stock>) model.asMap().get(stockViewController.getStocksAttribute());
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
        final int PAGE_SIZE = 2;
        generateStocks(PAGE_SIZE + 1);

        Pageable pageable = new PageRequest(0, PAGE_SIZE);
        testPaginationSize(PAGE_SIZE, pageable);
    }

    /**
     * C.S08 20 stocks should be visible per page by default
     */
    @Test
    public void test20StocksVisibleByDefault() {
        final int PAGE_SIZE = 20;
        generateStocks(PAGE_SIZE + 1);
        testPaginationSize(PAGE_SIZE, null);
    }

    /**
     * C.S10 Each row of the table should denote a company stock
     */
    @Test
    public void testEachRowIsCompanyStock() {
        Collection<Stock> stocks = new ArrayList<>();
        Collection<UserStock> userStocks = new ArrayList<>();
        for (int i = 1; i <= 2; i++) {
            String symbol = UUID.randomUUID().toString();
            stocks.add(new Stock(symbol, new BigDecimal(i * 2), i * 2));
            stocks.add(new Stock(symbol, new BigDecimal(i * 3), i * 3));
            userStocks.add(new UserStock(username, symbol, i * 2));
            userStocks.add(new UserStock(username, symbol, i * 3));
        }
        stockRepository.save(stocks);
        userStockRepository.save(userStocks);

        stockViewController.getBuyableStocks(model, null);
        Collection<Stock> retrievedStocks = (Collection<Stock>) model.asMap().get(stockViewController.getStocksAttribute());
        Collection<String> retrievedStockSymbols = new ArrayList<>();
        retrievedStocks.forEach(stock -> retrievedStockSymbols.add(stock.getSymbol()));

        model = new ExtendedModelMap();
        stockViewController.getSellableStocks(model, principal, null);
        Collection<UserStock> retrievedUserStocks = (Collection<UserStock>) model.asMap().get(stockViewController.getStocksAttribute());
        Collection<String> retrievedUserStockSymbols = new ArrayList<>();
        retrievedUserStocks.forEach(userStock -> retrievedUserStockSymbols.add(userStock.getSymbol()));

        assertEquals(new HashSet<>(retrievedStockSymbols).size(), retrievedStockSymbols.size());
        assertEquals(new HashSet<>(retrievedUserStockSymbols).size(), retrievedUserStockSymbols.size());
    }

    /**
     * C.S11 Stock data should be displayed with fields: Stock Symbol, Stock Name, Last Trade, Gains, Value, Volume, Open, Close
     * <p>
     * The number fields are verified in testStockDataNumberFields()
     */
    @Test
    public void testStockFields() {
        String stockPageContents = restTemplate.getForObject(stockURL, String.class);
        assertTrue(stockPageContents.contains("Stock Symbol"));
        assertTrue(stockPageContents.contains(stock.getSymbol()));
        assertTrue(stockPageContents.contains("Stock Name"));
        assertTrue(stockPageContents.contains(stock.getName()));
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

    private void generateStocks(int num) {
        Collection<Stock> stocks = new ArrayList<>();
        Collection<UserStock> userStocks = new ArrayList<>();
        for (int i = 1; i <= num; i++) {
            Stock stock = new Stock(UUID.randomUUID().toString(), new BigDecimal(i), i);
            stocks.add(stock);
            UserStock userStock = new UserStock(username, stock.getSymbol(), i);
            userStocks.add(userStock);
        }
        stockRepository.save(stocks);
        userStockRepository.save(userStocks);
    }

    private void testPaginationSize(int expectedSize, Pageable pageable) {
        stockViewController.getBuyableStocks(model, pageable);
        Collection<Stock> paginatedStocks = (Collection<Stock>) model.asMap().get(stockViewController.getStocksAttribute());
        assertEquals(expectedSize, paginatedStocks.size());

        model = new ExtendedModelMap();
        stockViewController.getSellableStocks(model, principal, pageable);
        paginatedStocks = (Collection<Stock>) model.asMap().get(stockViewController.getStocksAttribute());
        assertEquals(expectedSize, paginatedStocks.size());
    }
}
