package com.rasbet.backend.Exceptions;

public class BadPasswordException extends Exception {
    public BadPasswordException(String message) {
        super(message);
    }
}
