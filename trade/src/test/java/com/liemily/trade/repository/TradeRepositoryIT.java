package com.liemily.trade.repository;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * Created by Emily Li on 22/09/2017.
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class TradeRepositoryIT {
    @Autowired
    private TradeRepository tradeRepository;

    /**
     * C.S11 Stock data should be displayed with fields: Stock Symbol, Stock Name, Last Trade, Gains, Value, Volume, Open, Close
     * <p>
     * Tests last trade functionality
     */
    @Test
    public void testGetLastTrade() {

    }
}
