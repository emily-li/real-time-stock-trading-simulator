package com.liemily.realtimestocktradingsimulator.web.controller;

import com.liemily.stock.StockService;
import com.liemily.user.UserStockService;
import com.liemily.user.domain.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Created by Emily Li on 20/09/2017.
 */
@Controller
@RequestMapping("/stock")
public class StockController {
    private StockService stockService;
    private UserStockService userStockService;

    @Autowired
    public StockController(StockService stockService, UserStockService userStockService) {
        this.stockService = stockService;
        this.userStockService = userStockService;
    }

    @RequestMapping("/stock")
    public String requestStocks(Model model) {
        return "redirect:/stock/buy";
    }

    @RequestMapping("/stock/buy")
    public String addBuyableStocks(Model model) {
        model.addAttribute("stocks", stockService.findStocksWithVolume());
        return "stock";
    }

    @RequestMapping("/stock/sell")
    public String addSellableStocks(Model model,
                                    @ModelAttribute User user) {
        model.addAttribute("stocks", userStockService.findByUsername(user.getUsername()));
        return "stock";
    }
}
