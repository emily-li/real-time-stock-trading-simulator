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

import static org.junit.Assert.assertEquals;
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
        Stock stock2 = new Stock(UUID.randomUUID().toString(), new BigDecimal(2), 1);
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
        stocksController.getBuyableStocks(model, new PageRequest(0, Integer.MAX_VALUE), symbol.substring(10), null, null, null, null);
        List<StockView> stockViews = (List<StockView>) model.asMap().get(stocksController.getStocksAttribute());
        assertEquals(1, stockViews.size());
        assertEquals(symbol, stockViews.get(0).getSymbol());
    }

    /**
     * C.S15 The user should be able to search user stocks given a stock symbol
     */
    @Test
    public void testSearchUserStocksBySymbol() {
        stocksController.getSellableStocks(model, principal, new PageRequest(0, Integer.MAX_VALUE), symbol.substring(10), null, null, null);
        List<UserStock> stockViews = (List<UserStock>) model.asMap().get(stocksController.getStocksAttribute());
        assertEquals(1, stockViews.size());
        assertEquals(symbol, stockViews.get(0).getSymbol());
    }

    /**
     * C.S16 The user should be able to search stocks given a stock name
     */
    @Test
    public void testSearchStocksByName() {
        stocksController.getBuyableStocks(model, new PageRequest(0, Integer.MAX_VALUE), null, companyName.substring(10), null, null, null);
        List<StockView> stockViews = (List<StockView>) model.asMap().get(stocksController.getStocksAttribute());
        assertEquals(1, stockViews.size());
        assertEquals(companyName, stockViews.get(0).getName());
    }

    /**
     * C.S16 The user should be able to search stocks given a stock name
     */
    @Test
    public void testSearchUserStocksByName() {
        stocksController.getSellableStocks(model, principal, new PageRequest(0, Integer.MAX_VALUE), null, companyName.substring(10), null, null);
        List<UserStock> stockViews = (List<UserStock>) model.asMap().get(stocksController.getStocksAttribute());
        assertEquals(1, stockViews.size());
        assertEquals(companyName, stockViews.get(0).getName());
    }

    /**
     * C.S17 The user should be able to search stocks given a gains value, greater or less than the variable
     */
    @Test
    public void testSearchStocksByGains() {
        testBuyableGains("lt", new BigDecimal(2));
        testBuyableGains("gt", new BigDecimal(1));
    }

    /**
     * C.S17 The user should be able to search user stocks given a gains value, greater or less than the variable
     */
    @Test
    public void testSearchUserStocksByGains() {
        testSellableGains("lt", new BigDecimal(2));
        testSellableGains("gt", new BigDecimal(1));
    }

    private void testBuyableGains(String op, BigDecimal threshold) {
        stocksController.getBuyableStocks(model, new PageRequest(0, Integer.MAX_VALUE), null, null, op, threshold, null);
        List<StockView> stockViews = (List<StockView>) model.asMap().get(stocksController.getStocksAttribute());
        final int comparison = op.equalsIgnoreCase("lt") ? -1 : 1;
        stockViews.forEach(stockView -> assertTrue(stockView.getGains().compareTo(threshold) == comparison));
    }

    private void testSellableGains(String op, BigDecimal threshold) {
        stocksController.getSellableStocks(model, principal, new PageRequest(0, Integer.MAX_VALUE), null, null, op, threshold);
        List<UserStock> stockViews = (List<UserStock>) model.asMap().get(stocksController.getStocksAttribute());
        final int comparison = op.equalsIgnoreCase("lt") ? -1 : 1;
        stockViews.forEach(stockView -> assertTrue(stockView.getGains().compareTo(threshold) == comparison));
    }

    /**
     * C.S18 The user should be able to search stocks given a stock value, greater or lesser than the variable
     */
    @Test
    public void testSearchStocksByValue() {
        setupBuyableValuesTest("lt", new BigDecimal(2));
        stockViews.forEach(stockView -> assertTrue(stockView.getValue().compareTo(new BigDecimal(2)) == comparison));
        setupBuyableValuesTest("gt", new BigDecimal(1));
        stockViews.forEach(stockView -> assertTrue(stockView.getValue().compareTo(new BigDecimal(1)) == comparison));
    }

    private void setupBuyableValuesTest(String op, BigDecimal value) {
        model = new ExtendedModelMap();
        comparison = op.equalsIgnoreCase("lt") ? -1 : 1;
        stocksController.getBuyableStocks(model, new PageRequest(0, Integer.MAX_VALUE), null, null, op, null, value);
        stockViews = (List<StockView>) model.asMap().get(stocksController.getStocksAttribute());
    }

    /**
     * C.S19 The user should be able to search stocks given a volume, greater or lesser than the variable
     */
    @Test
    public void testSearchStocksByVolume() {

    }
}
