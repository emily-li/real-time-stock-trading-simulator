package com.liemily.realtimestocktradingsimulator.web.controller;

import com.liemily.stock.domain.StockItem;
import com.liemily.stock.domain.StockView;
import com.liemily.stock.service.StockViewService;
import com.liemily.user.UserStockService;
import com.liemily.user.domain.UserStock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Emily Li on 07/10/2017.
 */
@RestController
@RequestMapping("api/v1")
public class StocksAPIController {
    private StockViewService stockViewService;
    private UserStockService userStockService;
    private int pageStockDefaultSize;

    @Autowired
    public StocksAPIController(@Value("${page.stock.defaultSize}") int pageStockDefaultSize,
                               StockViewService stockViewService,
                               UserStockService userStockService) {
        this.pageStockDefaultSize = pageStockDefaultSize;
        this.stockViewService = stockViewService;
        this.userStockService = userStockService;
    }

    @RequestMapping("buy")
    List<StockItem> getBuyableStocks(Pageable pageable,
                                     @RequestParam(required = false) String symbol,
                                     @RequestParam(required = false) String name,
                                     @RequestParam(required = false) String op,
                                     @RequestParam(required = false) BigDecimal gains,
                                     @RequestParam(required = false) BigDecimal value,
                                     @RequestParam(required = false) Integer volume) {
        Pageable stocksPageable = pageable == null ? new PageRequest(0, pageStockDefaultSize) : pageable;
        List<StockItem> stockViews = null;

        if (symbol != null) {
            stockViews = new ArrayList<>(stockViewService.getStocksWithVolumeBySymbol(symbol, stocksPageable));
        } else if (name != null) {
            stockViews = new ArrayList<>(stockViewService.getStocksWithVolumeByName(name, stocksPageable));
        } else if (op != null) {
            stockViews = searchBuyableStocksWithOperator(op, gains, value, volume, stocksPageable);
        }

        stockViews = stockViews == null ? new ArrayList<>(stockViewService.getStocksWithVolume(stocksPageable)) : stockViews;
        return stockViews;
    }

    @RequestMapping("sell")
    List<StockItem> getSellableStocks(Principal principal,
                                      Pageable pageable,
                                      @RequestParam(required = false) String symbol,
                                      @RequestParam(required = false) String name,
                                      @RequestParam(required = false) String op,
                                      @RequestParam(required = false) BigDecimal gains,
                                      @RequestParam(required = false) BigDecimal value,
                                      @RequestParam(required = false) Integer volume) {
        String username = principal.getName();
        Pageable stocksPageable = pageable == null ? new PageRequest(0, pageStockDefaultSize) : pageable;
        List<StockItem> userStocks = null;

        if (symbol != null) {
            userStocks = new ArrayList<>(userStockService.getUserStocksBySymbol(username, symbol, stocksPageable));
        } else if (name != null) {
            userStocks = new ArrayList<>(userStockService.getUserStocksByName(username, name, stocksPageable));
        } else if (op != null) {
            userStocks = searchSellableStocksWithOperator(principal, op, gains, value, volume, stocksPageable);
        }

        userStocks = userStocks == null ? new ArrayList<>(userStockService.getUserStocks(username, stocksPageable)) : userStocks;
        return userStocks;
    }

    private List<StockItem> searchBuyableStocksWithOperator(String op, BigDecimal gains, BigDecimal value, Integer volume, Pageable stocksPageable) {
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
        return new ArrayList<>(stockViews);
    }

    private List<StockItem> searchSellableStocksWithOperator(Principal principal, String op, BigDecimal gains, BigDecimal value, Integer volume, Pageable stocksPageable) {
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
        return new ArrayList<>(userStocks);
    }
}
