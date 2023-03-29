package com.restapi.demo.transaction.exception;

/**
 * Thrown when an account is not found in the system.
 */
public class AccountNotFoundException extends RuntimeException {

    /**
     * Constructs an {@code AccountNotFoundException} with the specified detail message.
     *
     * @param message the detail message.
     */
    public AccountNotFoundException(String message) {
        super(message);
    }
}
