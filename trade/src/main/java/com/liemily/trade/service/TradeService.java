package com.liemily.trade.service;

import com.liemily.stock.domain.Stock;
import com.liemily.stock.service.StockService;
import com.liemily.trade.domain.Trade;
import com.liemily.trade.repository.TradeRepository;
import com.liemily.user.domain.UserStock;
import com.liemily.user.service.UserStockService;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by Emily Li on 25/09/2017.
 */
@Service
@Lazy
public class TradeService {
    private StockService stockService;
    private UserStockService userStockService;
    private TradeRepository tradeRepository;

    public TradeService(StockService stockService, UserStockService userStockService, TradeRepository tradeRepository) {
        this.stockService = stockService;
        this.userStockService = userStockService;
        this.tradeRepository = tradeRepository;
    }

    @Transactional
    public void save(Trade trade) {
        Stock stock = stockService.getStock(trade.getStockSymbol());
        trade.setValue(stock.getValue());
        tradeRepository.save(trade);
        UserStock userStock = new UserStock(trade.getUsername(), trade.getStockSymbol(), trade.getValue(), trade.getVolume());
        userStockService.save(userStock);
    }

    public Trade getTrade(String stock, String username) {
        return tradeRepository.findByStockSymbolAndUsername(stock, username);
    }
}
