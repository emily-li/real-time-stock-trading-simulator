package com.liemily.broker;

import com.liemily.stock.domain.Stock;
import com.liemily.stock.service.StockService;
import com.liemily.trade.domain.Trade;
import com.liemily.trade.service.TradeService;
import com.liemily.user.domain.User;
import com.liemily.user.service.UserService;
import org.junit.Before;
import org.junit.Test;

import java.math.BigDecimal;

import static org.mockito.Mockito.*;

/**
 * Broker unit tests based on FDM05-05 Functional Test Plan
 * See above in ../docs/FDM05-05 Functional Test Plan.doc
 * Created by Emily Li on 01/10/2017.
 */
@SuppressWarnings("WeakerAccess")
public class BrokerTest {
    private Broker broker;
    private Trade trade;

    @Before
    public void setup() {
        Stock stock = new Stock("symbol", new BigDecimal(1), 1);
        StockService stockService = mock(StockService.class);
        when(stockService.getStock(stock.getSymbol())).thenReturn(stock);

        User user = new User("username", "pwd");
        user.setCredits(new BigDecimal(1));
        UserService userService = mock(UserService.class);
        when(userService.getUser(user.getUsername())).thenReturn(user);

        trade = spy(new Trade(stock.getSymbol(), user.getUsername(), new BigDecimal(1), 1));
        broker = new Broker(stockService, mock(TradeService.class), userService);
    }

    /**
     * S.B01 The broker should receive user requests
     */
    @SuppressWarnings("ResultOfMethodCallIgnored")
    @Test
    public void testBrokerReceivesUserRequests() throws Exception {
        broker.process(trade);
        verify(trade, atLeastOnce()).getUsername();
    }
}
