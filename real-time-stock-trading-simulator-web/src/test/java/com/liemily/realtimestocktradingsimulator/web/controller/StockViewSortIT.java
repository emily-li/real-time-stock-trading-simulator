package com.liemily.realtimestocktradingsimulator.web.controller;

import com.liemily.stock.domain.Stock;
import com.liemily.stock.domain.StockView;
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

        username = UUID.randomUUID().toString();
        principal = (UserPrincipal) () -> username;
    }

    /**
     * Tests stocks can be ordered by value
     */
    @Test
    public void testOrderStocksByValue() {
        for (Sort.Direction direction : Sort.Direction.values()) {
            setupAssert(direction, "value");

            stockViewController.getBuyableStocks(model, new PageRequest(0, Integer.MAX_VALUE, sort));
            List<StockView> stocks = (List<StockView>) model.asMap().get(stockViewController.getStocksAttribute());

            stocks.forEach(stockView -> assertTrue(firstExpectedValue.compareTo(stockView.getValue()) == comparison));
        }
    }

    /**
     * Tests stocks can be ordered by open value
     */
    @Test
    public void testOrderStocksByOpenValue() {
        for (Sort.Direction direction : Sort.Direction.values()) {
            setupAssert(direction, "stockAsOfDetails.openValue");

            stockViewController.getBuyableStocks(model, new PageRequest(0, Integer.MAX_VALUE, sort));
            List<StockView> stocks = (List<StockView>) model.asMap().get(stockViewController.getStocksAttribute());

            stocks.forEach(stockView -> assertTrue(firstExpectedValue.compareTo(stockView.getOpenValue()) == comparison));
        }
    }

    /**
     * Tests stocks can be ordered by close value
     */
    @Test
    public void testOrderStocksByCloseValue() {
        for (Sort.Direction direction : Sort.Direction.values()) {
            setupAssert(direction, "stockAsOfDetails.closeValue");

            stockViewController.getBuyableStocks(model, new PageRequest(0, Integer.MAX_VALUE, sort));
            List<StockView> stocks = (List<StockView>) model.asMap().get(stockViewController.getStocksAttribute());

            stocks.forEach(stockView -> assertTrue(firstExpectedValue.compareTo(stockView.getCloseValue()) == comparison));
        }
    }

    /**
     * Tests user stocks can be ordered by value
     */
    @Test
    public void testOrderUserStocksByValue() {
        UserStock smallUserStock = new UserStock(username, smallStock.getSymbol(), 1);
        UserStock smallerUserStock = new UserStock(username, smallerStock.getSymbol(), 1);
        userStockService.save(smallUserStock);
        userStockService.save(smallerUserStock);

        for (Sort.Direction direction : Sort.Direction.values()) {
            setupAssert(direction, "stockView.value");

            stockViewController.getSellableStocks(model, principal, new PageRequest(0, Integer.MAX_VALUE, sort));
            List<UserStock> stocks = (List<UserStock>) model.asMap().get(stockViewController.getStocksAttribute());

            stocks.forEach(stockView -> assertTrue(firstExpectedValue.compareTo(stockView.getValue()) == comparison));
        }
    }

    private void setupAssert(Sort.Direction direction, String property) {
        sort = new Sort(direction, property);
        comparison = direction.isAscending() ? -1 : 1;
        firstExpectedValue = direction.isAscending() ? new BigDecimal(Integer.MIN_VALUE) : new BigDecimal(Integer.MAX_VALUE);
        model = new ExtendedModelMap();
    }
}
