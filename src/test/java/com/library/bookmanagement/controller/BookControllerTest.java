// src/test/java/com/library/bookmanagement/controller/BookControllerTest.java
package com.library.bookmanagement.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.library.bookmanagement.DTOs.BookDTO; // Ensure correct import for BookDTO
import com.library.bookmanagement.entity.AvailabilityStatus;
import com.library.bookmanagement.entity.Book;
import com.library.bookmanagement.exception.BookNotFoundException;
import com.library.bookmanagement.service.BookService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.List;
import java.util.UUID;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for {@link BookController} containing unit tests for API endpoints.
 */
class BookControllerTest {

    @Mock
    private BookService bookService; // Mocked service dependency

    @InjectMocks
    private BookController bookController; // Controller under test

    private MockMvc mockMvc; // MockMvc instance for testing HTTP requests

    private ObjectMapper objectMapper; // Jackson ObjectMapper for JSON serialization

    private BookDTO validBookDTO; // Valid DTO instance for testing

    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper(); // Initialize ObjectMapper
        validBookDTO = BookDTO.builder()
                .title("Clean Code")
                .author("Robert C. Martin")
                .genre("Programming")
                .status(AvailabilityStatus.AVAILABLE)
                .build(); // Create a valid BookDTO instance
        mockMvc = MockMvcBuilders.standaloneSetup(bookController).build(); // Set up MockMvc with the controller
    }

    /**
     * Test case: Successfully create a new book via API endpoint.
     * Expectation: Returns HTTP 201 Created and the created book details in response body.
     */
    @Test
    void createBook_ShouldReturnCreated() throws Exception {
        // Arrange
        Book validBook = Book.builder()
                .id(UUID.randomUUID()) // Mock generated ID
                .title(validBookDTO.getTitle())
                .author(validBookDTO.getAuthor())
                .genre(validBookDTO.getGenre())
                .status(validBookDTO.getStatus())
                .build(); // Create a valid Book object

        when(bookService.createBook(any(BookDTO.class))).thenReturn(validBook); // Mock service method to return Book

        mockMvc.perform(post("/api/books/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(validBookDTO))) // Serialize DTO to JSON string
                .andExpect(status().isCreated()) // Expect HTTP status 201 Created
                .andExpect(jsonPath("$.title").value(validBook.getTitle())) // Check title in response body
                .andExpect(jsonPath("$.author").value(validBook.getAuthor())); // Check author in response body
    }

    /**
     * Test case: Attempt to retrieve a non-existent book by ID via API endpoint.
     * Expectation: Returns HTTP 404 Not Found with appropriate error message.
     */
    @Test
    void getNonExistentBook_ShouldReturnNotFound() throws Exception {
        UUID nonExistentId = UUID.randomUUID(); // Generate a random UUID for testing
        when(bookService.getBookById(nonExistentId)).thenThrow(new BookNotFoundException(nonExistentId)); // Stub service method

        mockMvc.perform(get("/api/books/{id}", nonExistentId)) // Perform GET request to retrieve the non-existent book
                .andExpect(status().isNotFound()) // Expect HTTP status 404 Not Found
                .andExpect(content().string(containsString(nonExistentId.toString()))); // Check response contains the ID in error message
    }

    /**
     * Test case: Successfully retrieve all books via API endpoint when no books exist.
     * Expectation: Returns HTTP 200 OK and an empty list in response body.
     */
    @Test
    void getAllBooks_WhenNoBooksExist_ShouldReturnEmptyList() throws Exception {
        when(bookService.getAllBooks()).thenReturn(List.of()); // Stub service method to return an empty list

        mockMvc.perform(get("/api/books/all")) // Perform GET request to retrieve all books
                .andExpect(status().isOk()) // Expect HTTP status 200 OK
                .andExpect(jsonPath("$").isEmpty()); // Check that the response body is empty
    }
}
