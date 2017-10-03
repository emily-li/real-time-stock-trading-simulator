package com.liemily.realtimestocktradingsimulator.web.controller;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

/**
 * Created by Emily Li on 04/10/2017.
 */
@ControllerAdvice
public class ControllerExceptionHandler {
    @ExceptionHandler(Exception.class)
    public void handleException(Exception e) {
        System.out.println("Caught exception");
    }
}
