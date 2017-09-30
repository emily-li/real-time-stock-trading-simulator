package com.liemily.realtimestocktradingsimulator.web.controller;

import com.liemily.stock.domain.Stock;
import com.liemily.stock.domain.StockAsOfDetails;
import com.liemily.stock.domain.StockView;
import com.liemily.stock.repository.StockAsOfDetailsRepository;
import com.liemily.stock.service.StockService;
import com.liemily.user.UserStockService;
import com.liemily.user.domain.UserStock;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.ui.ExtendedModelMap;
import org.springframework.ui.Model;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.nio.file.attribute.UserPrincipal;
import java.security.Principal;
import java.util.List;
import java.util.UUID;

import static org.junit.Assert.assertTrue;

/**
 * C.S20 The user should be able to order stocks in ascending or descending direction given a field
 * Created by Emily Li on 30/09/2017.
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class StockViewSortIT {
    @Autowired
    private StockViewController stockViewController;

    @Autowired
    private StockService stockService;
    @Autowired
    private UserStockService userStockService;
    @Autowired
    private StockAsOfDetailsRepository stockAsOfDetailsRepository;

    private Model model;
    private String username;
    private Principal principal;

    private Stock smallStock;
    private Stock smallerStock;

    private Sort sort;
    private int comparison;
    private BigDecimal firstExpectedValue;

    @Before
    public void setup() {
        BigDecimal smallValue = new BigDecimal("-" + Math.random());
        smallValue = smallValue.setScale(2, RoundingMode.CEILING);
        BigDecimal smallerValue = smallValue.multiply(new BigDecimal(2));
        smallerValue = smallerValue.setScale(2, RoundingMode.CEILING);

        smallStock = new Stock(UUID.randomUUID().toString(), smallValue, 1);
        smallerStock = new Stock(UUID.randomUUID().toString(), smallerValue, 1);
        stockService.save(smallerStock);
        stockService.save(smallStock);

        StockAsOfDetails smallStockAsOfDetails = new StockAsOfDetails(smallStock);
        smallStockAsOfDetails.setOpenValue(new BigDecimal(5));
        smallStockAsOfDetails.setCloseValue(new BigDecimal(10));
        StockAsOfDetails smallerStockAsOfDetails = new StockAsOfDetails(smallerStock);
        smallerStockAsOfDetails.setOpenValue(new BigDecimal(4));
        smallerStockAsOfDetails.setCloseValue(new BigDecimal(6));
        stockAsOfDetailsRepository.save(smallStockAsOfDetails);
        stockAsOfDetailsRepository.save(smallerStockAsOfDetails);

        username = UUID.randomUUID().toString();
        principal = (UserPrincipal) () -> username;

        UserStock smallUserStock = new UserStock(username, smallStock.getSymbol(), 1);
        UserStock smallerUserStock = new UserStock(username, smallerStock.getSymbol(), 1);
        userStockService.save(smallUserStock);
        userStockService.save(smallerUserStock);
    }

    /**
     * Tests stocks can be ordered by value
     */
    @Test
    public void testOrderStocksByValue() {
        final String property = "stock.value";
        for (Sort.Direction direction : Sort.Direction.values()) {
            setupTest(direction, property);
            List<StockView> stocks = getOrderedStocks();
            stocks.forEach(stockView -> assertTrue(firstExpectedValue.compareTo(stockView.getValue()) == comparison));
        }
    }

    /**
     * Tests stocks can be ordered by open value
     */
    @Test
    public void testOrderStocksByOpenValue() {
        final String property = "stockAsOfDetails.closeValue";
        for (Sort.Direction direction : Sort.Direction.values()) {
            setupTest(direction, property);
            List<StockView> stocks = getOrderedStocks();
            stocks.forEach(stockView -> assertTrue(firstExpectedValue.compareTo(stockView.getOpenValue()) == comparison));
        }
    }

    /**
     * Tests stocks can be ordered by close value
     */
    @Test
    public void testOrderStocksByCloseValue() {
        final String property = "stockAsOfDetails.closeValue";
        for (Sort.Direction direction : Sort.Direction.values()) {
            setupTest(direction, property);
            List<StockView> stocks = getOrderedStocks();
            stocks.forEach(stockView -> assertTrue(firstExpectedValue.compareTo(stockView.getCloseValue()) == comparison));
        }
    }

    /**
     * Tests user stocks can be ordered by value
     */
    @Test
    public void testOrderUserStocksByValue() {
        final String property = "stockView.stock.value";
        for (Sort.Direction direction : Sort.Direction.values()) {
            setupTest(direction, property);
            List<UserStock> userStocks = getOrderedUserStocks();
            userStocks.forEach(userStock -> assertTrue(firstExpectedValue.compareTo(userStock.getValue()) == comparison));
        }
    }

    /**
     * Tests user stocks can be ordered by open value
     */
    @Test
    public void testOrderUserStocksByOpenValue() {
        final String property = "stockView.stockAsOfDetails.openValue";
        for (Sort.Direction direction : Sort.Direction.values()) {
            setupTest(direction, property);
            List<UserStock> userStocks = getOrderedUserStocks();
            userStocks.forEach(userStock -> assertTrue(firstExpectedValue.compareTo(userStock.getOpenValue()) == comparison));
        }
    }

    /**
     * Tests user stocks can be ordered by close value
     */
    @Test
    public void testOrderUserStocksByCloseValue() {
        final String property = "stockView.stockAsOfDetails.closeValue";
        for (Sort.Direction direction : Sort.Direction.values()) {
            setupTest(direction, property);
            List<UserStock> userStocks = getOrderedUserStocks();
            userStocks.forEach(userStock -> assertTrue(firstExpectedValue.compareTo(userStock.getCloseValue()) == comparison));
        }
    }

    private List<StockView> getOrderedStocks() {
        model = new ExtendedModelMap();
        stockViewController.getBuyableStocks(model, new PageRequest(0, Integer.MAX_VALUE, sort));
        return (List<StockView>) model.asMap().get(stockViewController.getStocksAttribute());
    }

    private List<UserStock> getOrderedUserStocks() {
        model = new ExtendedModelMap();
        stockViewController.getSellableStocks(model, principal, new PageRequest(0, Integer.MAX_VALUE, sort));
        return (List<UserStock>) model.asMap().get(stockViewController.getStocksAttribute());
    }

    private void setupTest(Sort.Direction direction, String property) {
        sort = new Sort(direction, property);
        comparison = direction.isAscending() ? -1 : 1;
        firstExpectedValue = direction.isAscending() ? new BigDecimal(Integer.MIN_VALUE) : new BigDecimal(Integer.MAX_VALUE);
    }
}
