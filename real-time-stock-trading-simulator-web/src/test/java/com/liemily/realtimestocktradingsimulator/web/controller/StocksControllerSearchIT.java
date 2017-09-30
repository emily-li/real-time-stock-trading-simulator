package com.liemily.realtimestocktradingsimulator.web.controller;

import com.liemily.stock.domain.Stock;
import com.liemily.stock.domain.StockView;
import com.liemily.stock.service.StockService;
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
import java.util.List;
import java.util.UUID;

import static org.junit.Assert.assertEquals;

/**
 * Created by Emily Li on 25/09/2017.
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class StocksControllerSearchIT {
    @Autowired
    private StocksController stocksController;
    @Autowired
    private StockService stockService;

    private Model model;
    private String symbol;

    @Before
    public void setup() {
        model = new ExtendedModelMap();

        symbol = UUID.randomUUID().toString().toUpperCase();
        Stock stock = new Stock(symbol, new BigDecimal(1), 1);
        stockService.save(stock);
    }

    /**
     * C.S15 The user should be able to search stocks given a stock symbol
     */
    @Test
    public void testSearchStocksBySymbol() {
        stocksController.getBuyableStocks(model, new PageRequest(0, Integer.MAX_VALUE), symbol.substring(10));
        List<StockView> stockViews = (List<StockView>) model.asMap().get(stocksController.getStocksAttribute());
        assertEquals(1, stockViews.size());
        assertEquals(symbol, stockViews.get(0).getSymbol());
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
}