package com.library.bookmanagement.controller;

import com.library.bookmanagement.dto.BookDTO;
import com.library.bookmanagement.entity.Book;
import com.library.bookmanagement.service.BookService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

/**
 * REST controller for managing book-related operations.
 * Handles API requests for creating, retrieving, updating, and deleting books.
 */
@RestController
@RequestMapping("/api/books")
@RequiredArgsConstructor
public class BookController {

    private final BookService bookService;

    /**
     * Creates a new book in the system.
     *
     * @param bookDTO The book DTO containing input details.
     * @return ResponseEntity containing the created book with HTTP 201 (Created) status.
     */
    @PostMapping("/create")
    public ResponseEntity<Book> createBook(@RequestBody BookDTO bookDTO) {
        return ResponseEntity.status(HttpStatus.CREATED).body(bookService.createBook(bookDTO));
    }

    /**
     * Retrieves all books from the system.
     *
     * @return ResponseEntity containing a list of all books with HTTP 200 (OK) status.
     */
    @GetMapping("/all")
    public ResponseEntity<List<Book>> getAllBooks() {
        return ResponseEntity.ok(bookService.getAllBooks());
    }

    /**
     * Retrieves a specific book by its ID.
     *
     * @param id The unique identifier of the book.
     * @return ResponseEntity containing the requested book with HTTP 200 (OK) status.
     */
    @GetMapping("/{id}")
    public ResponseEntity<Book> getBookById(@PathVariable UUID id) {
        return ResponseEntity.ok(bookService.getBookById(id));
    }

    /**
     * Searches for books by title (case-insensitive search).
     *
     * @param title The keyword to search for in book titles.
     * @return ResponseEntity containing a list of matching books with HTTP 200 (OK) status.
     */
    @GetMapping("/search")
    public ResponseEntity<List<Book>> searchBooksByTitle(@RequestParam String title) {
        return ResponseEntity.ok(bookService.searchBooksByTitle(title));
    }

    /**
     * Updates an existing book's details.
     *
     * @param id The unique identifier of the book to be updated.
     * @param bookDTO The updated book details.
     * @return ResponseEntity containing the updated book with HTTP 200 (OK) status.
     */
    @PutMapping("/{id}")
    public ResponseEntity<Book> updateBook(@PathVariable UUID id, @RequestBody BookDTO bookDTO) {
        Book updatedBook = bookService.updateBook(id, bookDTO);
        return ResponseEntity.ok(updatedBook);
    }

    /**
     * Deletes a book from the system.
     *
     * @param id The unique identifier of the book to be deleted.
     * @return ResponseEntity with HTTP 204 (No Content) status.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBook(@PathVariable UUID id) {
        bookService.deleteBook(id);
        return ResponseEntity.noContent().build();
    }
}
