package com.liemily.trade.repository;

import com.liemily.trade.domain.Trade;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Date;

/**
 * Created by Emily Li on 22/09/2017.
 */
public interface TradeRepository extends JpaRepository<Trade, Integer> {
    @Query("SELECT MAX(tradeDateTime) FROM Trade WHERE stockSymbol = ?1")
    Date getLastTradeDateTime(String stockSymbol);
}
