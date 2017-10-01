package com.liemily.broker;

import com.liemily.trade.domain.Trade;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

/**
 * Orchestrator for trades
 * All trades and validation must go through the Broker
 * Created by Emily Li on 01/10/2017.
 */
@Component
@Lazy
public class Broker {
    public void process(Trade trade) {
        trade.getUser();
    }
}
