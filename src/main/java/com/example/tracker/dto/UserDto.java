package com.example.tracker.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserDto {
    private String id;
    @NotEmpty(message = "Username must not be empty")
    private String username;
    @Email(message = "Email should be valid")
    private String email;
}
