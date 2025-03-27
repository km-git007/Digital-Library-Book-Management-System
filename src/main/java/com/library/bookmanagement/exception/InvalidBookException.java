package com.library.bookmanagement.exception;

/**
 * Custom exception thrown when a book entity fails validation due to invalid data.
 */
public class InvalidBookException extends RuntimeException {

    /**
     * Constructs a new InvalidBookException with a specific error message.
     *
     * @param message A detailed message explaining the validation error.
     */
    public InvalidBookException(String message) {
        super(message);
    }
}
