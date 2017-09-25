package com.liemily.realtimestocktradingsimulator.web.controller;

import com.liemily.company.domain.Company;
import com.liemily.company.service.CompanyService;
import com.liemily.stock.domain.Stock;
import com.liemily.stock.domain.StockAsOfDetails;
import com.liemily.stock.domain.StockView;
import com.liemily.stock.repository.StockAsOfDetailsRepository;
import com.liemily.stock.service.StockService;
import com.liemily.stock.service.StockViewService;
import com.liemily.trade.domain.Trade;
import com.liemily.trade.service.TradeService;
import com.liemily.user.UserStockService;
import com.liemily.user.domain.UserStock;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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
import java.text.SimpleDateFormat;
import java.util.*;

import static org.junit.Assert.*;

/**
 * Covers automated tests covered by "docs/FDM05-05 Functional Test Plan.doc" for client-side stock functionality
 * Created by Emily Li on 21/09/2017.
 */
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class StockViewControllerIT {
    private static final String DATETIME_FORMAT = "HH:mm:ss";

    @LocalServerPort
    private int port;
    @Autowired
    private TestRestTemplate restTemplate;
    @Autowired
    private StockViewController stockViewController;

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
    @Value("${page.stock.defaultSize}")
    private int pageStockDefaultSize;

    private String stockURL;
    private Model model;
    private String username;
    private Principal principal;

    private Trade trade;
    private StockView stockView;
    private BigDecimal expectedGains;

    @Before
    public void setup() {
        Company company = new Company(UUID.randomUUID().toString().toUpperCase(), UUID.randomUUID().toString());
        companyService.save(company);

        Stock stock = new Stock(company.getSymbol(), new BigDecimal(2), 1);
        stockService.save(stock);

        trade = new Trade(company.getSymbol());
        tradeService.save(trade);

        expectedGains = new BigDecimal(2);
        StockAsOfDetails stockAsOfDetails = new StockAsOfDetails(stock);
        stockAsOfDetails.setOpenValue(stock.getValue().subtract(expectedGains));
        stockAsOfDetails.setCloseValue(new BigDecimal(3));
        stockAsOfDetailsRepository.save(stockAsOfDetails);

        model = new ExtendedModelMap();

        username = UUID.randomUUID().toString();
        principal = (UserPrincipal) () -> username;

        stockURL = "http://localhost:" + port + "/stock";
        stockView = stockViewService.getStockView(company.getSymbol());
    }

    /**
     * C.S01 Buyable and sellable shares should be presented separately
     *
     * Automatically tests buyable shares can be retrieved individually.
     */
    @Test
    public void testGetBuyableShares() throws Exception {
        String stockPage = stockViewController.getBuyableStocks(model, new PageRequest(0, stockViewService.getStocksWithVolume(null).size()));
        StockView expectedStockView = stockViewService.getStockView(stockView.getSymbol());
        Collection<StockView> stockViews = (Collection<StockView>) model.asMap().get(stockViewController.getStocksAttribute());

        assertEquals("stock", stockPage);
        assertTrue(stockViews.contains(expectedStockView));
    }

    /**
     * C.S01 Buyable and sellable shares should be presented separately
     *
     * Automatically tests sellable shares can be retrieved individually.
     */
    @Test
    public void testGetSellableShares() {
        UserStock userStock = new UserStock(username, stockView.getSymbol(), 1);
        userStockService.save(userStock);

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
        Stock stock2 = new Stock("B" + UUID.randomUUID().toString().toUpperCase(), new BigDecimal(9), 9);
        Stock stock1 = new Stock("A" + UUID.randomUUID().toString().toUpperCase(), new BigDecimal(10), 10);
        stockService.save(stock1);
        stockService.save(stock2);

        stockViewController.getBuyableStocks(model, null);
        List<StockView> stocks = (List<StockView>) model.asMap().get(stockViewController.getStocksAttribute());
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
        stockService.save(stocks);
        userStockService.save(userStocks);

        stockViewController.getBuyableStocks(model, null);
        Collection<StockView> retrievedStocks = (Collection<StockView>) model.asMap().get(stockViewController.getStocksAttribute());
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
     */
    @Test
    public void testStockFields() {
        String expectedLastTradeDateTime = new SimpleDateFormat(DATETIME_FORMAT).format(trade.getTradeDateTime());
        expectedGains = new BigDecimal(2);

        stockView = stockViewService.getStockView(stockView.getSymbol());

        String stockPageContents = restTemplate.getForObject(stockURL, String.class);
        assertTrue(stockPageContents.contains("Stock Symbol"));
        assertTrue(stockPageContents.contains(stockView.getSymbol().toUpperCase()));
        assertTrue(stockPageContents.contains("Stock Name"));
        assertTrue(stockPageContents.contains(stockView.getName()));
        assertTrue(stockPageContents.contains("Last Trade"));
        assertTrue(stockPageContents.contains(expectedLastTradeDateTime));
        assertTrue(stockPageContents.contains("Gains"));
        assertTrue(stockPageContents.contains(stockView.getGains().toString()));
        assertTrue(stockPageContents.contains("Value"));
        assertTrue(stockPageContents.contains(stockView.getValue().toString()));
        assertTrue(stockPageContents.contains("Open"));
        assertTrue(stockPageContents.contains(stockView.getOpenValue().toString()));
        assertTrue(stockPageContents.contains("Close"));
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
     * C.S13 Last Trade should be presented in format HH:mm:ss
     */
    @Test
    public void testLastTradeFormat() {
        String expectedLastTradeDateTime = new SimpleDateFormat(DATETIME_FORMAT).format(trade.getTradeDateTime());
        String stockPageContents = restTemplate.getForObject(stockURL, String.class);

        assertTrue(stockPageContents.contains(">" + expectedLastTradeDateTime + "<"));
    }

    /**
     * C.S14 The user should be able to view all stocks on a single page
     */
    @Test
    public void testViewAllStocks() {
        generateStocks(pageStockDefaultSize * 2);
        Collection<Stock> stocks = stockService.getStocks();
        String pageContents = restTemplate.getForObject(stockURL + "/buy/all", String.class);
        stocks.forEach(stock -> assertTrue(pageContents.contains(stock.getSymbol().toUpperCase())));
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
        stockService.save(stocks);
        userStockService.save(userStocks);
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
