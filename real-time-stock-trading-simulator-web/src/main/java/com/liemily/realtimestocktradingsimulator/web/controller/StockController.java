package com.liemily.realtimestocktradingsimulator.web.controller;

import com.liemily.stock.StockService;
import com.liemily.stock.domain.Stock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Collection;

/**
 * Created by Emily Li on 20/09/2017.
 */
@Controller
@RequestMapping("/stock")
public class StockController {
    private StockService stockService;

    @Autowired
    public StockController(StockService stockService) {
        this.stockService = stockService;
    }

    @RequestMapping("/stock")
    public String stock(Model model) {
        model.addAttribute("stocks", stockService.findAll());
        return "stock";
    }

    public Collection<Stock> getBuyableStocks() {
        return null;
    }

    public Collection<Stock> getSellableStocks() {
        return null;
    }
}
