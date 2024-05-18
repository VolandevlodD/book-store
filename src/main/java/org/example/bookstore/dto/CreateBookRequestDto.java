package org.example.bookstore.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import java.math.BigDecimal;
import java.util.List;
import lombok.Data;
import org.hibernate.validator.constraints.ISBN;

@Data
public class CreateBookRequestDto {
    @NotBlank(message = "cannot be blank")
    private String title;
    @NotBlank(message = "cannot be blank")
    private String author;
    @NotNull(message = "cannot be null")
    @ISBN(message = "Invalid ISBN format")
    private String isbn;
    @NotNull(message = "cannot be null")
    @PositiveOrZero(message = "must be positive or zero")
    private BigDecimal price;
    private String description;
    private String coverImage;
    @NotEmpty(message = "Category IDs cannot be empty")
    private List<@PositiveOrZero(
            message = "Category ID must be positive or zero") Long> categoryIds;
}
