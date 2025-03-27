package com.library.bookmanagement.service;

import com.library.bookmanagement.dto.BookDTO;
import com.library.bookmanagement.entity.Book;
import com.library.bookmanagement.exception.BookNotFoundException;
import com.library.bookmanagement.exception.InvalidBookException;
import com.library.bookmanagement.repository.BookRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

/**
 * Service layer for managing books in the library system.
 * Provides CRUD operations and validation for book entities.
 */
@Service
@RequiredArgsConstructor
public class BookService {

    private final BookRepository bookRepository;

    /**
     * Creates a new book in the system.
     *
     * @param bookDTO The book DTO containing input details.
     * @return The saved book entity.
     */
    @Transactional
    public Book createBook(BookDTO bookDTO) {
        validateBookDTO(bookDTO);
        Book book = Book.builder()
                .id(UUID.randomUUID())  // Auto-generate UUID
                .title(bookDTO.getTitle())
                .author(bookDTO.getAuthor())
                .genre(bookDTO.getGenre())
                .status(bookDTO.getStatus())
                .build();
        return bookRepository.save(book);
    }

    /**
     * Retrieves all books in the library.
     *
     * @return A list of all books.
     */
    public List<Book> getAllBooks() {
        return bookRepository.findAll();
    }

    /**
     * Retrieves a book by its ID.
     *
     * @param id The unique identifier of the book.
     * @return The found book entity.
     */
    public Book getBookById(UUID id) {
        return bookRepository.findById(id)
                .orElseThrow(() -> new BookNotFoundException(id));
    }

    /**
     * Searches for books by title, performing a case-insensitive search.
     *
     * @param title The title keyword to search for.
     * @return A list of books matching the search criteria.
     */
    public List<Book> searchBooksByTitle(String title) {
        return bookRepository.findByTitleContainingIgnoreCase(title);
    }

    /**
     * Updates an existing book's details.
     *
     * @param id The unique identifier of the book to be updated.
     * @param bookDTO The updated book details.
     * @return The updated book entity.
     */
    @Transactional
    public Book updateBook(UUID id, BookDTO bookDTO) {
        Book existingBook = getBookById(id);

        existingBook.setTitle(bookDTO.getTitle());
        existingBook.setAuthor(bookDTO.getAuthor());
        existingBook.setGenre(bookDTO.getGenre());
        existingBook.setStatus(bookDTO.getStatus());

        return bookRepository.save(existingBook);
    }

    /**
     * Deletes a book from the system.
     *
     * @param id The unique identifier of the book to be deleted.
     */
    @Transactional
    public void deleteBook(UUID id) {
        if (!bookRepository.existsById(id)) {
            throw new BookNotFoundException(id);
        }
        bookRepository.deleteById(id);
    }

    /**
     * Validates that the book DTO has all required fields.
     *
     * @param bookDTO The book DTO to validate.
     * @throws InvalidBookException If any required fields are missing.
     */
    private void validateBookDTO(BookDTO bookDTO) {
        if (bookDTO.getTitle() == null || bookDTO.getTitle().isBlank()) {
            throw new InvalidBookException("Title is required");
        }
        if (bookDTO.getAuthor() == null || bookDTO.getAuthor().isBlank()) {
            throw new InvalidBookException("Author is required");
        }
        if (bookDTO.getStatus() == null) {
            throw new InvalidBookException("Availability status is required");
        }
    }
}
