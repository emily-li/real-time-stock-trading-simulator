package com.liemily.user.exception;

/**
 * Created by Emily Li on 08/10/2017.
 */
public class UserAlreadyExistsException extends Exception {
    public UserAlreadyExistsException(String message) {
        super(message);
    }
}
