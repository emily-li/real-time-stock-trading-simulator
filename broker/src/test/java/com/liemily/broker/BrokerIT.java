package com.liemily.broker;

import com.liemily.broker.config.BrokerConfig;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * Broker integration tests that verify correct Broker transactions
 * Created by Emily Li on 01/10/2017.
 */
@RunWith(SpringRunner.class)
@ContextConfiguration(classes = {BrokerConfig.class})
public class BrokerIT {

    /**
     * S.B03 The broker should check there is sufficient stock for the request
     */
    @Test
    public void testBrokerVerifiesStock() {

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
