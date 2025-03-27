package com.library.bookmanagement.DTOs;

import com.library.bookmanagement.entity.AvailabilityStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

@Data
@Builder(toBuilder = true)
public class BookDTO {
    @NotBlank(message = "Title cannot be blank")
    private String title;

    @NotBlank(message = "Author cannot be blank")
    private String author;

    private String genre;

    @NotNull
    private AvailabilityStatus status;
}
