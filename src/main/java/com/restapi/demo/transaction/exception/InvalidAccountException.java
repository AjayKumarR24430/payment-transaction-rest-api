package com.restapi.demo.transaction.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class InvalidAccountException extends RuntimeException {
    /**
     * Exception for indicating that the given account is invalid.
     *
     * @param message the detail message
     */
    public InvalidAccountException(String message) {
        super(message);
    }
}
