package com.library.bookmanagement.exception;

import java.util.UUID;

/**
 * Custom exception thrown when a book with the given ID is not found in the system.
 */
public class BookNotFoundException extends RuntimeException {

    /**
     * Constructs a new BookNotFoundException with a detailed error message.
     *
     * @param id The unique identifier of the book that was not found.
     */
    public BookNotFoundException(UUID id) {
        super("Book not found with ID: " + id);
    }
}
