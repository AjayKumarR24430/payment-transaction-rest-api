package com.restapi.demo.transaction.exception;

/**
 * This exception is thrown when a user attempts to perform a transaction that would cause their account balance to go
 * below zero, and they don't have sufficient funds to complete the transaction.
 */
public class InsufficientFundsException extends RuntimeException {
    public InsufficientFundsException(String message) {
        super(message);
    }
}
