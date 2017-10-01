package com.liemily.realtimestocktradingsimulator.web.controller;

import com.liemily.company.domain.Company;
import com.liemily.company.service.CompanyService;
import com.liemily.stock.domain.Stock;
import com.liemily.stock.domain.StockAsOfDetails;
import com.liemily.stock.domain.StockView;
import com.liemily.stock.repository.StockAsOfDetailsRepository;
import com.liemily.stock.service.StockService;
import com.liemily.user.UserStockService;
import com.liemily.user.domain.UserStock;
import com.sun.security.auth.UserPrincipal;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.ui.ExtendedModelMap;
import org.springframework.ui.Model;

import java.math.BigDecimal;
import java.security.Principal;
import java.util.List;
import java.util.UUID;

import static org.junit.Assert.assertTrue;

/**
 * Test class to ensure stock results can be sorted by each field
 *
 * Created by Emily Li on 25/09/2017.
 */
@SuppressWarnings("WeakerAccess")
@RunWith(SpringRunner.class)
@SpringBootTest
public class StocksControllerSearchIT {
    private static final String LT_OP = "lt";
    private static final String GT_OP = "gt";
    
    @Autowired
    private StocksController stocksController;
    @Autowired
    private CompanyService companyService;
    @Autowired
    private StockService stockService;
    @Autowired
    private UserStockService userStockService;
    @Autowired
    private StockAsOfDetailsRepository stockAsOfDetailsRepository;

    private Model model;
    private String symbol;
    private String companyName;
    private StockAsOfDetails stockAsOfDetails;
    private String username;
    private Principal principal;

    private int comparison;
    private List<StockView> stockViews;
    private List<UserStock> userStocks;

    @Before
    public void setup() {
        model = new ExtendedModelMap();
        username = UUID.randomUUID().toString();
        principal = new UserPrincipal(username);

        symbol = UUID.randomUUID().toString().toUpperCase();
        companyName = UUID.randomUUID().toString();

        Company company = new Company(symbol, companyName);
        companyService.save(company);

        Stock stock1 = new Stock(symbol, new BigDecimal(1), 1);
        Stock stock2 = new Stock(UUID.randomUUID().toString(), new BigDecimal(2), 3);
        stockService.save(stock1);
        stockService.save(stock2);

        UserStock userStock1 = new UserStock(username, symbol, 1);
        UserStock userStock2 = new UserStock(username, stock2.getSymbol(), 1);
        userStockService.save(userStock1);
        userStockService.save(userStock2);

        stockAsOfDetails = new StockAsOfDetails(stock1);
        stockAsOfDetails.setOpenValue(new BigDecimal(0));
        StockAsOfDetails stockAsOfDetails2 = new StockAsOfDetails(stock2);
        stockAsOfDetails2.setOpenValue(new BigDecimal(0));
        stockAsOfDetailsRepository.save(stockAsOfDetails);
        stockAsOfDetailsRepository.save(stockAsOfDetails2);
    }

    /**
     * C.S15 The user should be able to search stocks given a stock symbol
     */
    @Test
    public void testSearchStocksBySymbol() {
        setupBuyableTest(symbol.substring(10), null, null, null, null, null);
        stockViews.forEach(stockView -> assertTrue(stockView.getSymbol().contains(symbol)));
    }

    /**
     * C.S15 The user should be able to search user stocks given a stock symbol
     */
    @Test
    public void testSearchUserStocksBySymbol() {
        setupSellableTest(symbol.substring(10), null, null, null, null);
        userStocks.forEach(userStock -> assertTrue(userStock.getSymbol().equalsIgnoreCase(symbol)));
    }

    /**
     * C.S16 The user should be able to search stocks given a stock name
     */
    @Test
    public void testSearchStocksByName() {
        setupBuyableTest(null, companyName.substring(10), null, null, null, null);
        stockViews.forEach(stockView -> assertTrue(stockView.getName().toUpperCase().contains(companyName.toUpperCase())));
    }

    /**
     * C.S16 The user should be able to search stocks given a stock name
     */
    @Test
    public void testSearchUserStocksByName() {
        setupSellableTest(null, companyName.substring(10), null, null, null);
        userStocks.forEach(userStock -> assertTrue(userStock.getName().equalsIgnoreCase(companyName.toUpperCase())));
    }

    /**
     * C.S17 The user should be able to search stocks given a gains value, greater or less than the variable
     */
    @Test
    public void testSearchStocksByGains() {
        setupBuyableTest(null, null, LT_OP, new BigDecimal(2), null, null);
        stockViews.forEach(stockView -> assertTrue(stockView.getGains().compareTo(new BigDecimal(2)) == comparison));
        setupBuyableTest(null, null, GT_OP, new BigDecimal(1), null, null);
        stockViews.forEach(stockView -> assertTrue(stockView.getGains().compareTo(new BigDecimal(1)) == comparison));
    }

    /**
     * C.S17 The user should be able to search user stocks given a gains value, greater or less than the variable
     */
    @Test
    public void testSearchUserStocksByGains() {
        setupSellableTest(null, null, LT_OP, new BigDecimal(2), null);
        userStocks.forEach(userStock -> assertTrue(userStock.getGains().compareTo(new BigDecimal(2)) == comparison));
        setupSellableTest(null, null, GT_OP, new BigDecimal(1), null);
        userStocks.forEach(userStock -> assertTrue(userStock.getGains().compareTo(new BigDecimal(1)) == comparison));
    }

    /**
     * C.S18 The user should be able to search stocks given a stock value, greater or lesser than the variable
     */
    @Test
    public void testSearchStocksByValue() {
        setupBuyableTest(null, null, LT_OP, null, new BigDecimal(2), null);
        stockViews.forEach(stockView -> assertTrue(stockView.getValue().compareTo(new BigDecimal(2)) == comparison));
        setupBuyableTest(null, null, GT_OP, null, new BigDecimal(1), null);
        stockViews.forEach(stockView -> assertTrue(stockView.getValue().compareTo(new BigDecimal(1)) == comparison));
    }

    /**
     * C.S18 The user should be able to search user stocks given a stock value, greater or lesser than the variable
     */
    @Test
    public void testSearchUserStocksByValue() {
        setupSellableTest(null, null, LT_OP, null, new BigDecimal(2));
        userStocks.forEach(userStock -> assertTrue(userStock.getValue().compareTo(new BigDecimal(2)) == comparison));
        setupSellableTest(null, null, GT_OP, null, new BigDecimal(1));
        userStocks.forEach(userStock -> assertTrue(userStock.getValue().compareTo(new BigDecimal(1)) == comparison));
    }

    /**
     * C.S19 The user should be able to search stocks given a volume, greater or lesser than the variable
     */
    @Test
    public void testSearchStocksByVolume() {
        setupBuyableTest(null, null, LT_OP, null, null, 2);
        stockViews.forEach(stockView -> assertTrue(Integer.compare(stockView.getVolume(), 2) == comparison));
        setupBuyableTest(null, null, GT_OP, null, null, 1);
        stockViews.forEach(stockView -> assertTrue(Integer.compare(stockView.getVolume(), 2) == comparison));
    }

    private void setupBuyableTest(String symbol, String companyName, String op, BigDecimal gains, BigDecimal value, Integer volume) {
        model = new ExtendedModelMap();
        comparison = op == null || op.equalsIgnoreCase(LT_OP) ? -1 : 1;
        stocksController.getBuyableStocks(model, new PageRequest(0, Integer.MAX_VALUE), symbol, companyName, op, gains, value, volume);
        stockViews = (List<StockView>) model.asMap().get(stocksController.getStocksAttribute());
        assertTrue(stockViews.size() > 0);
    }

    private void setupSellableTest(String symbol, String companyName, String op, BigDecimal gains, BigDecimal value) {
        model = new ExtendedModelMap();
        comparison = op == null || op.equalsIgnoreCase(LT_OP) ? -1 : 1;
        stocksController.getSellableStocks(model, principal, new PageRequest(0, Integer.MAX_VALUE), symbol, companyName, op, gains, value);
        userStocks = (List<UserStock>) model.asMap().get(stocksController.getStocksAttribute());
        assertTrue(userStocks.size() > 0);
    }
}
