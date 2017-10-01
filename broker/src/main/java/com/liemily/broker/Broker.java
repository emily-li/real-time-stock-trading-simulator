package com.liemily.broker;

import com.liemily.broker.exception.InsufficientStockException;
import com.liemily.stock.domain.Stock;
import com.liemily.stock.service.StockService;
import com.liemily.trade.domain.Trade;
import com.liemily.trade.service.TradeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 * Orchestrator for trades
 * All trades and validation must go through the Broker
 * Created by Emily Li on 01/10/2017.
 */
@Component
@Lazy
public class Broker {
    @Autowired
    private StockService stockService;
    @Autowired
    private TradeService tradeService;

    @Transactional
    public void process(Trade trade) throws InsufficientStockException {
        trade.getUser();
        String stockSymbol = trade.getStockSymbol();
        Stock stock = stockService.getStock(stockSymbol);

        if (stock.getVolume() < 1) {
            throw new InsufficientStockException("Insufficient stock for trade " + trade);
        }

        tradeService.save(trade);
    }
}
