package com.liemily.user.exception;

/**
 * Created by Emily Li on 08/10/2017.
 */
public class InvalidUserTokenException extends Exception {
    public InvalidUserTokenException(String message) {
        super(message);
    }
}
