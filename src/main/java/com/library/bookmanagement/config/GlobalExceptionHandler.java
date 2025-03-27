package com.library.bookmanagement.config;

import com.library.bookmanagement.exception.BookNotFoundException;
import com.library.bookmanagement.exception.InvalidBookException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * Global exception handler for handling application-wide exceptions.
 * Provides centralized exception handling for all REST controllers.
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * Handles exceptions when a requested book is not found.
     *
     * @param ex The exception thrown when a book is not found.
     * @return A {@link ResponseEntity} with HTTP status 404 (Not Found) and the exception message.
     */
    @ExceptionHandler(BookNotFoundException.class)
    public ResponseEntity<String> handleBookNotFound(BookNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
    }

    /**
     * Handles validation errors when an invalid book request is made.
     *
     * @param ex The exception thrown for invalid book data.
     * @return A {@link ResponseEntity} with HTTP status 400 (Bad Request) and the exception message.
     */
    @ExceptionHandler(InvalidBookException.class)
    public ResponseEntity<String> handleInvalidBook(InvalidBookException ex) {
        return ResponseEntity.badRequest().body(ex.getMessage());
    }

    /**
     * Handles all unexpected exceptions that are not explicitly handled elsewhere.
     * Provides a generic error message to avoid exposing internal application details.
     *
     * @param ex The exception thrown due to an unexpected error.
     * @return A {@link ResponseEntity} with HTTP status 500 (Internal Server Error)
     * and a generic error message.
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleGenericErrors(Exception ex) {
        return ResponseEntity.internalServerError()
                .body("An unexpected error occurred: " + ex.getMessage());
    }
}
