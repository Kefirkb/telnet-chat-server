package com.kefirkb.exceptions;

/**
 * Exception for authorization is failed
 */
public class AuthException extends Exception {

    public AuthException(String message) {
        super(message);
    }
}
