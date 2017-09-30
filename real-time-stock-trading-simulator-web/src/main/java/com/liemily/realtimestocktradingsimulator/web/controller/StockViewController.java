package com.liemily.realtimestocktradingsimulator.web.controller;

import com.liemily.stock.domain.StockView;
import com.liemily.stock.service.StockViewService;
import com.liemily.user.UserStockService;
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
public class StockViewController {
    private static final String STOCKS_ATTRIBUTE = "stocks";
    private static final String STOCKS_PAGE = "stock";
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

    @RequestMapping("/buy")
    String getBuyableStocks(Model model,
                            Pageable pageable,
                            @RequestParam String symbol) {
        Pageable stocksPageable = pageable == null ? new PageRequest(0, pageStockDefaultSize) : pageable;
        List<StockView> stockViews;
        if (symbol != null) {
            stockViews = stockViewService.getStocksWithVolumeBySymbol(symbol, stocksPageable);
        } else {
            stockViews = stockViewService.getStocksWithVolume(stocksPageable);
        }
        model.addAttribute(STOCKS_ATTRIBUTE, stockViews);
        return STOCKS_PAGE;
    }

    @RequestMapping("/sell")
    String getSellableStocks(Model model,
                             Principal principal,
                             Pageable pageable) {
        Pageable stocksPageable = pageable == null ? new PageRequest(0, pageStockDefaultSize) : pageable;
        model.addAttribute(STOCKS_ATTRIBUTE, userStockService.getUserStocks(principal.getName(), stocksPageable));
        return STOCKS_PAGE;
    }

    String getStocksAttribute() {
        return STOCKS_ATTRIBUTE;
    }
}
