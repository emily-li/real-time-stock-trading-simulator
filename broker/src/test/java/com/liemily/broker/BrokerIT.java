package com.liemily.broker;

import com.liemily.broker.exception.InsufficientStockException;
import com.liemily.stock.domain.Stock;
import com.liemily.stock.service.StockService;
import com.liemily.trade.domain.Trade;
import com.liemily.trade.service.TradeService;
import com.liemily.user.domain.User;
import com.liemily.user.service.UserService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigDecimal;
import java.util.UUID;

import static org.junit.Assert.assertNotNull;

/**
 * S.B02 The broker should validate the user request
 * Broker integration tests that verify correct Broker transactions
 * See "../docs/FDM05-05 Functional Test Plan.doc"
 * Created by Emily Li on 01/10/2017.
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class BrokerIT {
    @Autowired
    private Broker broker;
    @Autowired
    private StockService stockService;
    @Autowired
    private TradeService tradeService;
    @Autowired
    private UserService userService;

    private String username;

    @Before
    public void setup() {
        username = UUID.randomUUID().toString();
        User user = new User(username);
        userService.save(user);
    }

    /**
     * S.B03 The broker should check there is sufficient stock for the request
     */
    @Test(expected = InsufficientStockException.class)
    public void testBrokerVerifiesStockVolume() throws Exception {
        Stock stock = new Stock(UUID.randomUUID().toString(), new BigDecimal(1), 0);
        stockService.save(stock);
        Trade trade = new Trade(stock.getSymbol(), username);
        broker.process(trade);
    }

    /**
     * Tests successful trade
     */
    @Test
    public void testSuccessfulTrade() throws Exception {
        Stock stock = new Stock(UUID.randomUUID().toString(), new BigDecimal(1), 1);
        stockService.save(stock);
        Trade trade = new Trade(stock.getSymbol(), username);
        broker.process(trade);

        Trade successfulTrade = tradeService.getTrade(stock.getSymbol(), username);
        assertNotNull(successfulTrade);
    }

    /**
     * S.B04 The broker should check if there is sufficient credits for the request
     */
    @Test
    public void testBrokerVerifiesCredits() {

    }

    /**
     * Transactions should be possible given the company symbol and desired stock quantity
     */
    @Test
    public void testTransaction() {

    }

}
