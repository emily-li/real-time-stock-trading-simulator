package com.liemily.realtimestocktradingsimulator.web.controller;

import com.liemily.stock.domain.Stock;
import com.liemily.stock.domain.StockView;
import com.liemily.stock.service.StockService;
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
    private StockService stockService;
    @Autowired
    private StockViewController stockViewController;

    /**
     * Tests stocks can be ordered by value
     */
    @Test
    public void testOrderStocksByValue() {
        BigDecimal smallValue = new BigDecimal("-" + Math.random());
        smallValue = smallValue.setScale(2, RoundingMode.CEILING);
        BigDecimal smallerValue = smallValue.multiply(new BigDecimal(2));
        smallerValue = smallerValue.setScale(2, RoundingMode.CEILING);

        Stock smallStock = new Stock(UUID.randomUUID().toString(), smallValue, 1);
        Stock smallerStock = new Stock(UUID.randomUUID().toString(), smallerValue, 1);
        stockService.save(smallerStock);
        stockService.save(smallStock);

        Model model = new ExtendedModelMap();
        stockViewController.getBuyableStocks(model, new PageRequest(0, Integer.MAX_VALUE, new Sort(Sort.Direction.ASC, "value")));
        List<StockView> ascStocks = (List<StockView>) model.asMap().get(stockViewController.getStocksAttribute());

        BigDecimal prevValue = new BigDecimal(Integer.MIN_VALUE);
        ascStocks.forEach(stockView -> assertTrue(prevValue.compareTo(stockView.getValue()) < 0));
    }
}
