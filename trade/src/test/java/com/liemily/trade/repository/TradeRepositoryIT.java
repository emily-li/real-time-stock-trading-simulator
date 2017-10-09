package com.liemily.trade.repository;

import com.liemily.trade.domain.Trade;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigDecimal;
import java.util.Date;
import java.util.UUID;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

/**
 * Created by Emily Li on 22/09/2017.
 */
@SuppressWarnings("WeakerAccess")
@RunWith(SpringRunner.class)
@SpringBootTest
public class TradeRepositoryIT {
    @Autowired
    private TradeRepository tradeRepository;

    /**
     * C.S11 Stock data should be displayed with fields: Stock Symbol, Stock Name, Last Trade, Gains, Value, Volume, Open, Close
     *
     * Tests last trade functionality
     */
    @Test
    public void testGetLastTrade() {
        String stockSymbol = UUID.randomUUID().toString();
        String username = UUID.randomUUID().toString();
        Trade trade1 = new Trade(stockSymbol, username, new BigDecimal(1), 1);
        tradeRepository.save(trade1);
        Trade trade2 = new Trade(stockSymbol, username, new BigDecimal(1), 1);
        tradeRepository.save(trade2);
        Trade trade3 = new Trade(UUID.randomUUID().toString(), username, new BigDecimal(1), 1);
        tradeRepository.save(trade3);

        Date lastTradeDateTime = tradeRepository.getLastTradeDateTime(stockSymbol);

        assertNotEquals(trade1.getTradeDateTime(), lastTradeDateTime);
        assertEquals(trade2.getTradeDateTime(), lastTradeDateTime);
    }
}
