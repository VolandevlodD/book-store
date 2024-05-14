package org.example.bookstore.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import org.example.bookstore.annotation.validation.FieldMatch;

@FieldMatch(first = "password", second = "repeatPassword", message = "Passwords do not match")
public record CreateUserRequestDto(
        @Email(message = "Invalid email format")
        @NotBlank(message = "Email is required")
        String email,

        @Size(min = 8, max = 20, message = "Password length should be between 8 and 20 characters")
        @NotBlank(message = "Password is required")
        String password,

        @NotBlank(message = "Please confirm your password")
        String repeatPassword,

        @NotBlank(message = "First name is required")
        String firstName,

        @NotBlank(message = "Last name is required")
        String lastName,

        String shippingAddress
) {
}
