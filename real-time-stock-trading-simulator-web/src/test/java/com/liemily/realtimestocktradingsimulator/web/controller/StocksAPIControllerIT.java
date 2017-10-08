package com.liemily.realtimestocktradingsimulator.web.controller;

import com.liemily.company.domain.Company;
import com.liemily.company.service.CompanyService;
import com.liemily.stock.domain.Stock;
import com.liemily.stock.domain.StockAsOfDetails;
import com.liemily.stock.domain.StockItem;
import com.liemily.stock.domain.StockView;
import com.liemily.stock.repository.StockAsOfDetailsRepository;
import com.liemily.stock.service.StockService;
import com.liemily.stock.service.StockViewService;
import com.liemily.trade.domain.Trade;
import com.liemily.trade.service.TradeService;
import com.liemily.user.UserStockService;
import com.liemily.user.domain.UserStock;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
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

import java.lang.invoke.MethodHandles;
import java.math.BigDecimal;
import java.nio.file.attribute.UserPrincipal;
import java.security.Principal;
import java.text.SimpleDateFormat;
import java.util.*;

import static org.junit.Assert.*;

/**
 * Covers automated tests covered by "docs/FDM05-05 Functional Test Plan.doc" for client-side stock functionality
 * Created by Emily Li on 21/09/2017.
 */
@SuppressWarnings({"ConstantConditions", "WeakerAccess"})
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class StocksAPIControllerIT {
    private static final Logger logger = LogManager.getLogger(MethodHandles.lookup().lookupClass());

    private static final String DATETIME_FORMAT = "HH:mm:ss";
    private static final int PAGE_STOCK_DEFAULT_SIZE = 20;

    @LocalServerPort
    private int port;
    @Autowired
    private TestRestTemplate restTemplate;
    @Autowired
    private StocksAPIController stocksAPIController;
    @Autowired
    private StocksController stocksController;

    @Autowired
    private StockViewService stockViewService;
    @Autowired
    private CompanyService companyService;
    @Autowired
    private StockService stockService;
    @Autowired
    private UserStockService userStockService;
    @Autowired
    private TradeService tradeService;
    @Autowired
    private StockAsOfDetailsRepository stockAsOfDetailsRepository;

    private String stockURL;
    private String username;
    private Principal principal;

    private Trade trade;
    private StockView stockView;
    private BigDecimal expectedGains;

    @Before
    public void setup() {
        username = UUID.randomUUID().toString();
        principal = (UserPrincipal) () -> username;

        Company company = new Company(UUID.randomUUID().toString().toUpperCase(), UUID.randomUUID().toString());
        companyService.save(company);

        Stock stock = new Stock(company.getSymbol(), new BigDecimal(2), 1);
        stockService.save(stock);

        trade = new Trade(company.getSymbol(), username, 1);
        tradeService.save(trade);

        expectedGains = new BigDecimal(2);
        StockAsOfDetails stockAsOfDetails = new StockAsOfDetails(stock);
        stockAsOfDetails.setOpenValue(stock.getValue().subtract(expectedGains));
        stockAsOfDetails.setCloseValue(new BigDecimal(3));
        stockAsOfDetailsRepository.save(stockAsOfDetails);

        stockURL = "http://localhost:" + port + "/api/v1/buy?page=0&size=" + Integer.MAX_VALUE;
        stockView = stockViewService.getStockView(company.getSymbol());
    }

    /**
     * C.S01 Buyable and sellable shares should be presented separately
     * <p>
     * Automatically tests buyable shares can be retrieved individually.
     */
    @Test
    public void testGetBuyableShares() throws Exception {
        Collection<StockItem> stockViews = stocksAPIController.getBuyableStocks(new PageRequest(0, stockViewService.getStocksWithVolume(null).size()), null, null, null, null, null, null);
        StockView expectedStockView = stockViewService.getStockView(stockView.getSymbol());

        assertTrue(stockViews.contains(expectedStockView));
    }

    /**
     * C.S01 Buyable and sellable shares should be presented separately
     * <p>
     * Automatically tests sellable shares can be retrieved individually.
     */
    @Test
    public void testGetSellableShares() {
        UserStock userStock = new UserStock(username, stockView.getSymbol(), 1);
        userStockService.save(userStock);

        Collection<StockItem> stockItems = stocksAPIController.getSellableStocks(principal, null, null, null, null, null, null, null);

        assertTrue(stockItems.contains(userStock));
    }

    /**
     * C.S02 Stock data should be ordered by stock symbol by default
     */
    @Test
    public void testGetOrderedStocks() {
        Stock stock2 = new Stock("B" + UUID.randomUUID().toString().toUpperCase(), new BigDecimal(9), 9);
        Stock stock1 = new Stock("A" + UUID.randomUUID().toString().toUpperCase(), new BigDecimal(10), 10);
        stockService.save(stock1);
        stockService.save(stock2);

        List<StockItem> stockItems = stocksAPIController.getBuyableStocks(new PageRequest(0, Integer.MAX_VALUE), null, null, null, null, null, null);
        List<String> stockSymbols = new ArrayList<>();
        stockItems.forEach(stockItem -> stockSymbols.add(stockItem.getSymbol()));

        List<String> expectedOrder = new ArrayList<>(stockSymbols);
        Collections.sort(expectedOrder);

        assertEquals(expectedOrder, stockSymbols);
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
        stockService.save(stocks);
        userStockService.save(userStocks);

        Collection<StockItem> retrievedStocks = stocksAPIController.getBuyableStocks(null, null, null, null, null, null, null);
        Collection<String> retrievedStockSymbols = new ArrayList<>();
        retrievedStocks.forEach(stock -> retrievedStockSymbols.add(stock.getSymbol()));

        Collection<StockItem> retrievedUserStocks = stocksAPIController.getSellableStocks(principal, null, null, null, null, null, null, null);
        Collection<String> retrievedUserStockSymbols = new ArrayList<>();
        retrievedUserStocks.forEach(userStock -> retrievedUserStockSymbols.add(userStock.getSymbol()));

        assertEquals(new HashSet<>(retrievedStockSymbols).size(), retrievedStockSymbols.size());
        assertEquals(new HashSet<>(retrievedUserStockSymbols).size(), retrievedUserStockSymbols.size());
    }

    /**
     * C.S11 Stock data should be displayed with fields: Stock Symbol, Stock Name, Last Trade, Gains, Value, Volume, Open, Close
     */
    public void testStockFieldValues() {
        String expectedLastTradeDateTime = new SimpleDateFormat(DATETIME_FORMAT).format(trade.getTradeDateTime());
        expectedGains = new BigDecimal(2);

        stockView = stockViewService.getStockView(stockView.getSymbol());

        String stockPageContents = restTemplate.getForObject(stockURL, String.class);

        assertTrue(stockPageContents.contains(stockView.getSymbol().toUpperCase()));
        assertTrue(stockPageContents.contains(expectedLastTradeDateTime));
        assertTrue(stockPageContents.contains(stockView.getName()));
        assertTrue(stockPageContents.contains(stockView.getGains().toString()));
        assertTrue(stockPageContents.contains(stockView.getValue().toString()));
        assertTrue(stockPageContents.contains(stockView.getOpenValue().toString()));
        assertTrue(stockPageContents.contains(stockView.getCloseValue().toString()));
    }

    @Test
    public void testUserStock() {
        UserStock userStock = new UserStock(username, stockView.getSymbol(), 1);
        userStockService.save(userStock);
        userStock = userStockService.getUserStock(username, stockView.getSymbol());

        assertNotNull(userStock.getSymbol());
        assertNotNull(userStock.getName());
        assertNotNull(userStock.getLastTradeDateTime());
        assertNotNull(userStock.getGains());
        assertNotNull(userStock.getValue());
        assertNotNull(userStock.getOpenValue());
        assertNotNull(userStock.getCloseValue());
    }

    /**
     * C.S12 Presented stock symbols must be capitals
     */
    @Test
    public void testStockSymbolCapitals() {
        Stock stock = new Stock("case1" + UUID.randomUUID(), new BigDecimal(1), 1);
        stockService.save(stock);

        List<Stock> stocks = new ArrayList<>();
        stocks.add(new Stock("case2" + UUID.randomUUID(), new BigDecimal(1), 1));
        stocks.add(new Stock("case3" + UUID.randomUUID(), new BigDecimal(1), 1));
        stockService.save(stocks);

        String pageContents = restTemplate.getForObject(stockURL, String.class);
        assertTrue(pageContents.contains(stock.getSymbol().toUpperCase()));
        assertTrue(pageContents.contains(stocks.get(0).getSymbol().toUpperCase()));
        assertTrue(pageContents.contains(stocks.get(1).getSymbol().toUpperCase()));
    }

    /**
     * C.S14 The user should be able to view all stocks on a single page
     */
    @Test
    public void testViewAllStocks() {
        generateStocks(PAGE_STOCK_DEFAULT_SIZE * 2);
        Collection<StockView> stockViews = stockViewService.getStocksWithVolume(null);
        String pageContents = restTemplate.getForObject(stockURL, String.class);
        stockViews.forEach(stockView -> assertTrue(pageContents.contains(stockView.getSymbol())));
    }

    @Test
    public void testViewAllUserStocks() {
        generateStocks(PAGE_STOCK_DEFAULT_SIZE * 2);
        Collection<UserStock> userStocks = userStockService.getUserStocks(username, null);
        Collection<StockItem> retrievedUserStocks = stocksAPIController.getSellableStocks(principal, new PageRequest(0, Integer.MAX_VALUE), null, null, null, null, null, null);
        assertTrue(retrievedUserStocks.containsAll(userStocks));
    }

    /**
     * C.S21 Companies with no available stock should be removed from the stock availability list
     */
    @Test
    public void testNoStocksReturnedIfEmptyVolume() {
        Stock stock = new Stock(stockView.getSymbol(), stockView.getValue(), 0);
        stockService.save(stock);
        String pageContents = restTemplate.getForObject(stockURL, String.class);
        assertFalse(pageContents.contains(stock.getSymbol().toUpperCase()));
        pageContents = restTemplate.getForObject(stockURL + "&volume=1&op=lt", String.class);
        assertFalse(pageContents.contains(stock.getSymbol().toUpperCase()));
    }

    /**
     * C.S22 Companies with no stock should reappear to the stock availability list when new stock is available
     */
    @Test
    public void testCompaniesWithStockReturnedWhenPreviouslyEmpty() {
        Stock stock = new Stock(stockView.getSymbol(), stockView.getValue(), 0);
        stockService.save(stock);
        stock = new Stock(stockView.getSymbol(), stockView.getValue(), 1);
        stock = stockService.save(stock);

        List<StockItem> stocks = stocksAPIController.getBuyableStocks(null, stock.getSymbol(), null, null, null, null, null);

        assert stocks.size() == 1;
        assertEquals(stock.getSymbol(), stocks.get(0).getSymbol());
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
        stockService.save(stocks);
        userStockService.save(userStocks);
    }

    private void testPaginationSize(int expectedSize, Pageable pageable) {
        Collection<StockItem> paginatedStocks = stocksAPIController.getBuyableStocks(pageable, null, null, null, null, null, null);
        assertEquals(expectedSize, paginatedStocks.size());

        paginatedStocks = stocksAPIController.getSellableStocks(principal, pageable, null, null, null, null, null, null);
        assertEquals(expectedSize, paginatedStocks.size());
    }
}
