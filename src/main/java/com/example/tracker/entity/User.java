package com.example.tracker.entity;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.validation.annotation.Validated;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "user")
@Validated
public class User {
    @Id
    private String id;
    @NotEmpty(message = "Username must not be empty")
    private String username;
    @Email(message = "Email should be valid")
    private String email;
}
