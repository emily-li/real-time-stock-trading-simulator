package com.liemily.broker;

import com.liemily.trade.domain.Trade;
import org.junit.Before;
import org.junit.Test;

import static org.mockito.Mockito.*;

/**
 * Broker unit tests based on FDM05-05 Functional Test Plan
 * See above in ../docs/FDM05-05 Functional Test Plan.doc
 * Created by Emily Li on 01/10/2017.
 */
public class BrokerTest {
    private Broker broker;
    private Trade trade;

    @Before
    public void setup() {
        broker = new Broker();
        trade = spy(new Trade("symbol", "username"));
    }

    /**
     * S.B01 The broker should receive user requests
     */
    @Test
    public void testBrokerReceivesUserRequests() throws Exception {
        broker.process(trade);
        verify(trade, atLeastOnce()).getUser();
    }
}
