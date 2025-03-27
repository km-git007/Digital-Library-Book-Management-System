package com.library.bookmanagement.repository;

import com.library.bookmanagement.entity.Book;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Repository interface for managing Book entities.
 * Extends JpaRepository to provide CRUD operations and custom queries.
 */
public interface BookRepository extends JpaRepository<Book, UUID> {

    /**
     * Finds a book by its title (case-insensitive).
     *
     * @param title The exact title of the book.
     * @return An Optional containing the found book or empty if no match is found.
     */
    Optional<Book> findByTitleIgnoreCase(String title);

    /**
     * Checks whether a book exists by its ID.
     *
     * @param id The unique identifier of the book.
     * @return True if the book exists, false otherwise.
     */
    boolean existsById(UUID id);

    /**
     * Searches for books with titles containing a specific keyword (case-insensitive).
     *
     * @param title The keyword to search for within book titles.
     * @return A list of books that contain the given keyword in their title.
     */
    List<Book> findByTitleContainingIgnoreCase(String title);
}
