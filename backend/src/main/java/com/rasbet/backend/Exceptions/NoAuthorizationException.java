package com.rasbet.backend.Exceptions;

public class NoAuthorizationException extends Exception {
    public NoAuthorizationException(String message) {
        super(message);
    }

}
