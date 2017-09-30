package com.liemily.realtimestocktradingsimulator.web.controller;

import com.liemily.stock.domain.StockView;
import com.liemily.stock.service.StockViewService;
import com.liemily.user.UserStockService;
import com.liemily.user.domain.UserStock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.security.Principal;
import java.util.List;

/**
 * Created by Emily Li on 20/09/2017.
 */
@Controller
@RequestMapping("stock")
public class StocksController {
    private static final String STOCKS_MODEL_ATTRIBUTE = "stocks";
    private static final String STOCKS_PAGE = "stock";
    private StockViewService stockViewService;
    private UserStockService userStockService;
    private int pageStockDefaultSize;

    @Autowired
    public StocksController(StockViewService stockViewService,
                            UserStockService userStockService,
                            @Value("${page.stock.defaultSize}") int pageStockDefaultSize) {
        this.stockViewService = stockViewService;
        this.userStockService = userStockService;
        this.pageStockDefaultSize = pageStockDefaultSize;
    }

    @RequestMapping("buy")
    String getBuyableStocks(Model model,
                            Pageable pageable,
                            @RequestParam(required = false) String symbol,
                            @RequestParam(required = false) String name) {
        Pageable stocksPageable = pageable == null ? new PageRequest(0, pageStockDefaultSize) : pageable;
        List<StockView> stockViews;
        if (symbol != null) {
            stockViews = stockViewService.getStocksWithVolumeBySymbol(symbol, stocksPageable);
        } else if (name != null) {
            stockViews = stockViewService.getStocksWithVolumeByName(name, stocksPageable);
        } else {
            stockViews = stockViewService.getStocksWithVolume(stocksPageable);
        }
        model.addAttribute(STOCKS_MODEL_ATTRIBUTE, stockViews);
        return STOCKS_PAGE;
    }

    @RequestMapping("sell")
    String getSellableStocks(Model model,
                             Principal principal,
                             Pageable pageable,
                             @RequestParam(required = false) String symbol) {
        String username = principal.getName();
        Pageable stocksPageable = pageable == null ? new PageRequest(0, pageStockDefaultSize) : pageable;
        List<UserStock> userStocks;
        if (symbol == null) {
            userStocks = userStockService.getUserStocks(username, stocksPageable);
        } else {
            userStocks = userStockService.getUserStocksBySymbol(username, symbol, stocksPageable);
        }
        model.addAttribute(STOCKS_MODEL_ATTRIBUTE, userStocks);
        return STOCKS_PAGE;
    }

    String getStocksAttribute() {
        return STOCKS_MODEL_ATTRIBUTE;
    }
}
