package com.liemily.broker.exception;

/**
 * Created by Emily Li on 01/10/2017.
 */
public class InsufficientCreditException extends BrokerException {
    public InsufficientCreditException(String message) {
        super(message);
    }
}
