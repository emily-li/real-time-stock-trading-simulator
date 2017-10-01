package com.liemily.broker;

import com.liemily.broker.exception.BrokerException;
import com.liemily.broker.exception.InsufficientCreditException;
import com.liemily.broker.exception.InsufficientStockException;
import com.liemily.stock.domain.Stock;
import com.liemily.stock.service.StockService;
import com.liemily.trade.domain.Trade;
import com.liemily.trade.service.TradeService;
import com.liemily.user.domain.User;
import com.liemily.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

/**
 * Orchestrator for trades
 * All trades and validation must go through the Broker
 * Created by Emily Li on 01/10/2017.
 */
@Component
@Lazy
public class Broker {
    private StockService stockService;
    private TradeService tradeService;
    private UserService userService;

    @Autowired
    public Broker(StockService stockService, TradeService tradeService, UserService userService) {
        this.stockService = stockService;
        this.tradeService = tradeService;
        this.userService = userService;
    }

    @Transactional
    public void process(Trade trade) throws BrokerException {
        String stockSymbol = trade.getStockSymbol();
        Stock stock = stockService.getStock(stockSymbol);

        if (stock.getVolume() < 1) {
            throw new InsufficientStockException("Insufficient stock for trade " + trade);
        }

        String username = trade.getUsername();
        User user = userService.getUser(username);
        if (user.getCredits().compareTo(new BigDecimal(1)) == -1) {
            throw new InsufficientCreditException("Insufficient credits for trade " + trade);
        }
        tradeService.save(trade);
    }
}
