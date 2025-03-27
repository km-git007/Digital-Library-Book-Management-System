package com.library.bookmanagement.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Book {
    @Id
    @NotBlank(message = "Book ID cannot be blank")
    private String id;

    @NotBlank(message = "Title cannot be blank")
    @Column(nullable = false)
    private String title;

    @NotBlank(message = "Author cannot be blank")
    @Column(nullable = false)
    private String author;

    private String genre;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private AvailabilityStatus status;
}


