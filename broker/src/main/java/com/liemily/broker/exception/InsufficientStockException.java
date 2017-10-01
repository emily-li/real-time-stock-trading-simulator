package com.liemily.broker.exception;

/**
 * Created by Emily Li on 01/10/2017.
 */
public class InsufficientStockException extends Exception {
    public InsufficientStockException(String message) {
        super(message);
    }
}
