package com.example.backend.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class UserRegisterDTO {
    @NotNull(message = "Full name is required")
    private String fullName;

    @Email(message = "Invalid email format")
    @NotNull(message = "Username is required")
    private String username;

    @Size(min = 8, message = "Password must be at least 8 characters")
    @NotNull(message = "Password is required")
    private String password;

    private boolean verifiedEmail;
}
