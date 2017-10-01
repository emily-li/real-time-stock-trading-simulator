package com.liemily.trade.service;

import com.liemily.trade.domain.Trade;
import com.liemily.trade.repository.TradeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

/**
 * Created by Emily Li on 25/09/2017.
 */
@Service
@Lazy
public class TradeService {
    private TradeRepository tradeRepository;

    @Autowired
    public TradeService(TradeRepository tradeRepository) {
        this.tradeRepository = tradeRepository;
    }

    public void save(Trade trade) {
        tradeRepository.save(trade);
    }

    public Trade getTrade(String stock, String username) {
        return tradeRepository.findByStockSymbolAndUsername(stock, username);
    }
}
