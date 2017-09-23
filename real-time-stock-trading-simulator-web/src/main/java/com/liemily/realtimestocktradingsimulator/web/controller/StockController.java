package com.liemily.realtimestocktradingsimulator.web.controller;

import com.liemily.stock.StockService;
import com.liemily.user.UserStockService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import java.security.Principal;

/**
 * Created by Emily Li on 20/09/2017.
 */
@Controller
public class StockController {
    private final String STOCKS_ATTRIBUTE = "stocks";
    private StockService stockService;
    private UserStockService userStockService;

    @Autowired
    public StockController(StockService stockService, UserStockService userStockService) {
        this.stockService = stockService;
        this.userStockService = userStockService;
    }

    @RequestMapping("/stock")
    public String requestStocks() {
        return "redirect:/stock/buy";
    }

    @RequestMapping("/stock/buy")
    public String getBuyableStocks(Model model, Pageable pageable) {
        model.addAttribute(STOCKS_ATTRIBUTE, stockService.getStocksWithVolume(pageable));
        return "stock";
    }

    @RequestMapping("/stock/sell")
    public String getSellableStocks(Model model,
                                    Principal principal,
                                    Pageable pageable) {
        model.addAttribute(STOCKS_ATTRIBUTE, userStockService.findByUsername(principal.getName(), pageable));
        return "stock";
    }

    public String getStocksAttribute() {
        return STOCKS_ATTRIBUTE;
    }
}
