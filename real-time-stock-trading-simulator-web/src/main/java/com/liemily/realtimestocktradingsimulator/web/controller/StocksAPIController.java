package com.liemily.realtimestocktradingsimulator.web.controller;

import com.liemily.broker.Broker;
import com.liemily.broker.exception.BrokerException;
import com.liemily.broker.exception.InsufficientCreditException;
import com.liemily.broker.exception.InsufficientStockException;
import com.liemily.stock.domain.StockItem;
import com.liemily.stock.domain.StockView;
import com.liemily.stock.service.StockViewService;
import com.liemily.trade.domain.Trade;
import com.liemily.user.domain.UserStock;
import com.liemily.user.service.UserStockService;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.lang.invoke.MethodHandles;
import java.math.BigDecimal;
import java.security.Principal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Emily Li on 07/10/2017.
 */
@RestController
@RequestMapping("api/v1")
@Lazy
public class StocksAPIController {
    private static final Logger logger = LogManager.getLogger(MethodHandles.lookup().lookupClass());

    private StockViewService stockViewService;
    private UserStockService userStockService;
    private Broker broker;
    private int pageStockDefaultSize;

    @Autowired
    public StocksAPIController(@Value("${page.stock.defaultSize}") int pageStockDefaultSize,
                               StockViewService stockViewService,
                               UserStockService userStockService,
                               Broker broker) {
        this.pageStockDefaultSize = pageStockDefaultSize;
        this.stockViewService = stockViewService;
        this.userStockService = userStockService;
        this.broker = broker;
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
        if (stocksPageable.getSort() == null) {
            Sort sort = new Sort(Sort.Direction.ASC, "symbol");
            stocksPageable = new PageRequest(stocksPageable.getPageNumber(), stocksPageable.getPageSize(), sort);
        }
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

    @RequestMapping("buy/all")
    List<StockItem> getBuyableStocks() {
        return getBuyableStocks(new PageRequest(0, Integer.MAX_VALUE), null, null, null, null, null, null);
    }

    @RequestMapping(value = "buy", method = RequestMethod.POST)
    Map<String, String> buy(Trade trade) {
        final Map<String, String> response = new HashMap<>();
        final String success = "success";
        final String exceptionSuccess = "false";
        response.put(success, "true");
        response.put("msg", "Trades successful");

        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        trade.setUsername(username);
        logger.info("Received order for purchase " + trade);
        try {
            broker.process(trade);
            logger.info("Successfully processed trade");
        } catch (InsufficientStockException ise) {
            logger.info("Failed to process trade due to insufficient stock for trade: " + trade);
            response.put(success, exceptionSuccess);
            response.put("msg", "There are insufficient stocks to perform the trade for stock " + trade.getStockSymbol());
        } catch (InsufficientCreditException ice) {
            logger.info("Failed to process trade due to insufficient credits for trade: " + trade);
            response.put(success, exceptionSuccess);
            response.put("msg", "User has insufficient credits to perform the trade: " + trade);
        } catch (BrokerException be) {
            logger.info("Failed to process trade: " + trade);
            response.put(success, exceptionSuccess);
            response.put("msg", "Failed to process trade: " + trade);
        }
        return response;
    }

    @RequestMapping("sell/all")
    List<StockItem> getSellableStocks(Principal principal) {
        return getSellableStocks(principal, new PageRequest(0, Integer.MAX_VALUE), null, null, null, null, null, null);
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
            } else if (volume != null && volume > 1) {
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
        return stockViews == null ? new ArrayList<>() : new ArrayList<>(stockViews);
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
        return userStocks == null ? new ArrayList<>() : new ArrayList<>(userStocks);
    }
}
