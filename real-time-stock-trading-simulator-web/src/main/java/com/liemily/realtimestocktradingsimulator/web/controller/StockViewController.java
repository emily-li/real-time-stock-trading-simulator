package com.liemily.realtimestocktradingsimulator.web.controller;

import com.liemily.stock.service.StockViewService;
import com.liemily.user.UserStockService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import java.security.Principal;

/**
 * Created by Emily Li on 20/09/2017.
 */
@Controller
public class StockViewController {
    private final String STOCKS_ATTRIBUTE = "stocks";
    private StockViewService stockViewService;
    private UserStockService userStockService;
    private int pageStockDefaultSize;

    @Autowired
    public StockViewController(StockViewService stockViewService,
                               UserStockService userStockService,
                               @Value("${page.stock.defaultSize}") int pageStockDefaultSize) {
        this.stockViewService = stockViewService;
        this.userStockService = userStockService;
        this.pageStockDefaultSize = pageStockDefaultSize;
    }

    @RequestMapping("/stock")
    public String requestStocks() {
        return "redirect:/stock/buy";
    }

    @RequestMapping("/stock/buy")
    public String getBuyableStocks(Model model, Pageable pageable) {
        pageable = pageable == null ? new PageRequest(0, pageStockDefaultSize) : pageable;
        model.addAttribute(STOCKS_ATTRIBUTE, stockViewService.getStocksWithVolume(pageable));
        return "stock";
    }

    @RequestMapping("/stock/sell")
    public String getSellableStocks(Model model,
                                    Principal principal,
                                    Pageable pageable) {
        pageable = pageable == null ? new PageRequest(0, pageStockDefaultSize) : pageable;
        model.addAttribute(STOCKS_ATTRIBUTE, userStockService.getUserStocks(principal.getName(), pageable));
        return "stock";
    }

    public String getStocksAttribute() {
        return STOCKS_ATTRIBUTE;
    }
}
