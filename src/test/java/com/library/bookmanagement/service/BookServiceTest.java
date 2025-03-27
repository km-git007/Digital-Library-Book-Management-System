package com.library.bookmanagement.service;

import com.library.bookmanagement.DTOs.BookDTO;
import com.library.bookmanagement.entity.Book;
import com.library.bookmanagement.entity.AvailabilityStatus;
import com.library.bookmanagement.exception.BookNotFoundException;
import com.library.bookmanagement.exception.InvalidBookException;
import com.library.bookmanagement.repository.BookRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * Test class for {@link BookService} containing unit tests for all service operations.
 * Uses Mockito for mocking dependencies and AssertJ for assertions.
 */
@ExtendWith(MockitoExtension.class)
class BookServiceTest {

    @Mock
    private BookRepository bookRepository; // Mocked BookRepository dependency

    @InjectMocks
    private BookService bookService; // Service under test

    private BookDTO validBookDTO; // Valid book data transfer object
    private UUID testId; // Test ID for book entities

    /**
     * Initializes common test data before each test method execution.
     */
    @BeforeEach
    void setUp() {
        testId = UUID.randomUUID(); // Generate a random UUID for testing
        validBookDTO = BookDTO.builder()
                .title("Clean Code")
                .author("Robert C. Martin")
                .genre("Programming")
                .status(AvailabilityStatus.AVAILABLE)
                .build(); // Create a valid BookDTO instance
    }

    /**
     * Creates a persisted book entity with generated UUID for mock repository responses.
     */
    private Book createPersistedBook() {
        return Book.builder()
                .id(testId)
                .title(validBookDTO.getTitle())
                .author(validBookDTO.getAuthor())
                .genre(validBookDTO.getGenre())
                .status(validBookDTO.getStatus())
                .build(); // Create a Book entity based on the valid DTO
    }

    /**
     * Test case: Successfully create a valid book.
     * Expectation: Returns saved book with generated UUID.
     */
    @Test
    void createBook_WithValidDTO_ReturnsSavedBookWithGeneratedId() {
        // Arrange
        Book expectedBook = createPersistedBook();
        when(bookRepository.save(any(Book.class))).thenReturn(expectedBook); // Stub repository save method

        // Act
        Book result = bookService.createBook(validBookDTO); // Call service method with valid DTO

        // Assert
        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(testId);
        assertThat(result.getTitle()).isEqualTo(validBookDTO.getTitle());

        // Verify repository interaction
        ArgumentCaptor<Book> bookCaptor = ArgumentCaptor.forClass(Book.class);
        verify(bookRepository).save(bookCaptor.capture());
        assertThat(bookCaptor.getValue().getId()).isNotNull(); // Ensure ID is generated
    }

    /**
     * Test case: Attempt to create a book with missing title.
     * Expectation: Throws InvalidBookException with appropriate message.
     */
    @Test
    void createBook_WithMissingTitle_ThrowsValidationException() {
        // Arrange
        BookDTO invalidDTO = validBookDTO.toBuilder().title("").build(); // Create DTO with blank title

        // Act & Assert
        assertThatThrownBy(() -> bookService.createBook(invalidDTO))
                .isInstanceOf(InvalidBookException.class)
                .hasMessageContaining("Title cannot be blank"); // Check exception message
    }

    /**
     * Test case: Attempt to retrieve non-existent book by ID.
     * Expectation: Throws BookNotFoundException with correct ID.
     */
    @Test
    void getBookById_WithInvalidId_ThrowsNotFoundException() {
        // Arrange
        when(bookRepository.findById(testId)).thenReturn(Optional.empty()); // Stub repository to return empty

        // Act & Assert
        assertThatThrownBy(() -> bookService.getBookById(testId))
                .isInstanceOf(BookNotFoundException.class)
                .hasMessageContaining(testId.toString()); // Check exception message contains ID
    }

    /**
     * Test case: Successfully update existing book details.
     * Expectation: Returns updated book with new values.
     */
    @Test
    void updateBook_WithValidData_ReturnsUpdatedBook() {
        // Arrange
        Book existingBook = createPersistedBook(); // Create existing book entity
        BookDTO updateDTO = validBookDTO.toBuilder()
                .title("Refactoring") // New title for update
                .status(AvailabilityStatus.CHECKED_OUT) // New status for update
                .build();

        when(bookRepository.findById(testId)).thenReturn(Optional.of(existingBook)); // Stub find by ID
        when(bookRepository.save(any(Book.class))).thenAnswer(invocation -> invocation.getArgument(0)); // Stub save method

        // Act
        Book updatedBook = bookService.updateBook(testId, updateDTO); // Call update method

        // Assert
        assertThat(updatedBook.getTitle()).isEqualTo("Refactoring"); // Check updated title
        assertThat(updatedBook.getStatus()).isEqualTo(AvailabilityStatus.CHECKED_OUT); // Check updated status
    }

    /**
     * Test case: Attempt to delete non-existent book.
     * Expectation: Throws BookNotFoundException.
     */
    @Test
    void deleteBook_WithInvalidId_ThrowsNotFoundException() {
        // Arrange
        when(bookRepository.existsById(testId)).thenReturn(false); // Stub existence check to return false

        // Act & Assert
        assertThatThrownBy(() -> bookService.deleteBook(testId))
                .isInstanceOf(BookNotFoundException.class)
                .hasMessageContaining(testId.toString()); // Check exception message contains ID
    }

    /**
     * Test case: Search books by title returns matching results.
     * Expectation: Returns list of books containing search term.
     */
    @Test
    void searchBooksByTitle_WithExistingTitle_ReturnsMatchingBooks() {
        // Arrange
        List<Book> mockResults = List.of(createPersistedBook()); // Create mock results list
        when(bookRepository.findByTitleContainingIgnoreCase("clean")).thenReturn(mockResults); // Stub search method

        // Act
        List<Book> results = bookService.searchBooksByTitle("clean"); // Call search method

        // Assert
        assertThat(results).hasSize(1);
        assertThat(results.get(0).getTitle()).containsIgnoringCase("clean"); // Check title matches search term
    }

    /**
     * Test case: Retrieve all books when none exist.
     * Expectation: Returns empty list.
     */
    @Test
    void getAllBooks_WhenNoBooksExist_ReturnsEmptyList() {
        // Arrange
        when(bookRepository.findAll()).thenReturn(List.of()); // Stub find all to return empty list

        // Act
        List<Book> results = bookService.getAllBooks();

        // Assert
        assertThat(results).isEmpty();  // Check that the result is an empty list
    }

    /**
     * Test case: Attempt to create a book with missing status.
     * Expectation: Throws InvalidBookException.
     */
    @Test
    void createBook_WithMissingStatus_ThrowsValidationException() {
        // Arrange
        BookDTO invalidDTO = validBookDTO.toBuilder().status(null).build();  // Create DTO with null status

        // Act & Assert
        assertThatThrownBy(() -> bookService.createBook(invalidDTO))
                .isInstanceOf(InvalidBookException.class)
                .hasMessageContaining("Availability status is required");  // Check exception message
    }
}
