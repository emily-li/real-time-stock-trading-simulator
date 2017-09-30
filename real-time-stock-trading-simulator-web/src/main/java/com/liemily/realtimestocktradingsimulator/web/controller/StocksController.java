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

import java.math.BigDecimal;
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
                            @RequestParam(required = false) String name,
                            @RequestParam(required = false) String op,
                            @RequestParam(required = false) BigDecimal gains,
                            @RequestParam(required = false) BigDecimal value) {
        Pageable stocksPageable = pageable == null ? new PageRequest(0, pageStockDefaultSize) : pageable;
        List<StockView> stockViews = null;

        if (symbol != null) {
            stockViews = stockViewService.getStocksWithVolumeBySymbol(symbol, stocksPageable);
        } else if (name != null) {
            stockViews = stockViewService.getStocksWithVolumeByName(name, stocksPageable);
        } else if (op != null) {
            stockViews = searchBuyableStocksWithOperator(op, gains, value, stocksPageable);
        }

        stockViews = stockViews == null ? stockViewService.getStocksWithVolume(stocksPageable) : stockViews;
        model.addAttribute(STOCKS_MODEL_ATTRIBUTE, stockViews);
        return STOCKS_PAGE;
    }

    @RequestMapping("sell")
    String getSellableStocks(Model model,
                             Principal principal,
                             Pageable pageable,
                             @RequestParam(required = false) String symbol,
                             @RequestParam(required = false) String name,
                             @RequestParam(required = false) String op,
                             @RequestParam(required = false) BigDecimal gains) {
        String username = principal.getName();
        Pageable stocksPageable = pageable == null ? new PageRequest(0, pageStockDefaultSize) : pageable;
        List<UserStock> userStocks = null;

        if (symbol != null) {
            userStocks = userStockService.getUserStocksBySymbol(username, symbol, stocksPageable);
        } else if (name != null) {
            userStocks = userStockService.getUserStocksByName(username, name, stocksPageable);
        } else if (op != null && gains != null) {
            if (op.equalsIgnoreCase("lt")) {
                userStocks = userStockService.getUserStocksByGainsLessThan(username, gains, stocksPageable);
            } else if (op.equalsIgnoreCase("gt")) {
                userStocks = userStockService.getUserStocksByGainsGreaterThan(username, gains, stocksPageable);
            }
        }

        userStocks = userStocks == null ? userStockService.getUserStocks(username, stocksPageable) : userStocks;
        model.addAttribute(STOCKS_MODEL_ATTRIBUTE, userStocks);
        return STOCKS_PAGE;
    }

    String getStocksAttribute() {
        return STOCKS_MODEL_ATTRIBUTE;
    }

    private List<StockView> searchBuyableStocksWithOperator(String op, BigDecimal gains, BigDecimal value, Pageable stocksPageable) {
        List<StockView> stockViews = null;
        if (op.equalsIgnoreCase("lt")) {
            if (gains != null) {
                stockViews = stockViewService.getStocksWithVolumeByGainsLessThan(gains, stocksPageable);
            } else if (value != null) {
                stockViews = stockViewService.getStocksWithVolumeByValueLessThan(value, stocksPageable);
            }
        } else if (op.equalsIgnoreCase("gt")) {
            if (gains != null) {
                stockViews = stockViewService.getStocksWithVolumeByGainsGreaterThan(gains, stocksPageable);
            } else if (value != null) {
                stockViews = stockViewService.getStocksWithVolumeByValueGreaterThan(value, stocksPageable);
            }
        }
        return stockViews;
    }
}
