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
class StocksController {
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
                            @RequestParam(required = false) BigDecimal value,
                            @RequestParam(required = false) Integer volume) {
        Pageable stocksPageable = pageable == null ? new PageRequest(0, pageStockDefaultSize) : pageable;
        List<StockView> stockViews = null;

        if (symbol != null) {
            stockViews = stockViewService.getStocksWithVolumeBySymbol(symbol, stocksPageable);
        } else if (name != null) {
            stockViews = stockViewService.getStocksWithVolumeByName(name, stocksPageable);
        } else if (op != null) {
            stockViews = searchBuyableStocksWithOperator(op, gains, value, volume, stocksPageable);
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
                             @RequestParam(required = false) BigDecimal gains,
                             @RequestParam(required = false) BigDecimal value,
                             @RequestParam(required = false) Integer volume) {
        String username = principal.getName();
        Pageable stocksPageable = pageable == null ? new PageRequest(0, pageStockDefaultSize) : pageable;
        List<UserStock> userStocks = null;

        if (symbol != null) {
            userStocks = userStockService.getUserStocksBySymbol(username, symbol, stocksPageable);
        } else if (name != null) {
            userStocks = userStockService.getUserStocksByName(username, name, stocksPageable);
        } else if (op != null) {
            userStocks = searchSellableStocksWithOperator(principal, op, gains, value, volume, stocksPageable);
        }

        userStocks = userStocks == null ? userStockService.getUserStocks(username, stocksPageable) : userStocks;
        model.addAttribute(STOCKS_MODEL_ATTRIBUTE, userStocks);
        return STOCKS_PAGE;
    }

    String getStocksAttribute() {
        return STOCKS_MODEL_ATTRIBUTE;
    }

    private List<StockView> searchBuyableStocksWithOperator(String op, BigDecimal gains, BigDecimal value, Integer volume, Pageable stocksPageable) {
        List<StockView> stockViews = null;
        if (op.equalsIgnoreCase("lt")) {
            if (gains != null) {
                stockViews = stockViewService.getStocksWithVolumeByGainsLessThan(gains, stocksPageable);
            } else if (value != null) {
                stockViews = stockViewService.getStocksWithVolumeByValueLessThan(value, stocksPageable);
            } else if (volume != null) {
                stockViews = stockViewService.getStocksWithVolumeLessThan(volume, stocksPageable);
            }
        } else if (op.equalsIgnoreCase("gt")) {
            if (gains != null) {
                stockViews = stockViewService.getStocksWithVolumeByGainsGreaterThan(gains, stocksPageable);
            } else if (value != null) {
                stockViews = stockViewService.getStocksWithVolumeByValueGreaterThan(value, stocksPageable);
            } else if (volume != null) {
                stockViews = stockViewService.getStocksWithVolumeGreaterThan(volume, stocksPageable);
            }
        }
        return stockViews;
    }

    private List<UserStock> searchSellableStocksWithOperator(Principal principal, String op, BigDecimal gains, BigDecimal value, Integer volume, Pageable stocksPageable) {
        final String username = principal.getName();
        List<UserStock> userStocks = null;

        if (op.equalsIgnoreCase("lt")) {
            if (gains != null) {
                userStocks = userStockService.getUserStocksByGainsLessThan(username, gains, stocksPageable);
            } else if (value != null) {
                userStocks = userStockService.getUserStocksByValueLessThan(username, value, stocksPageable);
            } else if (volume != null) {
                userStocks = userStockService.getUserStocksByVolumeLessThan(username, volume, stocksPageable);
            }
        } else if (op.equalsIgnoreCase("gt")) {
            if (gains != null) {
                userStocks = userStockService.getUserStocksByGainsGreaterThan(username, gains, stocksPageable);
            } else if (value != null) {
                userStocks = userStockService.getUserStocksByValueGreaterThan(username, value, stocksPageable);
            } else if (volume != null) {
                userStocks = userStockService.getUserStocksByVolumeGreaterThan(username, volume, stocksPageable);
            }

        }
        return userStocks;
    }

}
