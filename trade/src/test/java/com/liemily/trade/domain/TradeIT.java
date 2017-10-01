package com.liemily.trade.domain;

import com.liemily.trade.repository.TradeRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Date;
import java.util.UUID;

import static org.junit.Assert.assertTrue;

/**
 * Created by Emily Li on 22/09/2017.
 */
@SuppressWarnings("WeakerAccess")
@RunWith(SpringRunner.class)
@SpringBootTest
public class TradeIT {
    @Autowired
    private TradeRepository tradeRepository;

    @Test
    public void testTradeDateTime() {
        String username = UUID.randomUUID().toString();
        Trade trade1 = new Trade(UUID.randomUUID().toString(), username);
        tradeRepository.save(trade1);
        Date timeAfterFirstTrade = new Date();
        Trade trade2 = new Trade(UUID.randomUUID().toString(), username);
        tradeRepository.save(trade2);

        assertTrue(timeAfterFirstTrade.after(trade1.getTradeDateTime()));
        assertTrue(trade2.getTradeDateTime().after(trade1.getTradeDateTime()));
    }
}
