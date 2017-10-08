package com.liemily.realtimestocktradingsimulator.web.domain;

/**
 * Created by Emily Li on 07/10/2017.
 */
public enum ControllerError {
    REGISTRATION_USERNAME_ALREADY_EXISTS_ERROR,
    REGISTRATION_EMAIL_ERROR,

    TRADE_INSUFFICIENT_STOCK_ERROR,
    TRADE_INSUFFICIENT_CREDITS_ERROR,
    TRADE_BROKER_ERROR,
}
