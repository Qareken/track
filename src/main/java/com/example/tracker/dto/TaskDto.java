package com.example.tracker.dto;

import com.example.tracker.entity.User;
import com.example.tracker.entity.enumurate.TaskStatus;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.Set;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class TaskDto {

    private String id;
    @NotEmpty(message = "name must not be empty")
    private String name;
    @NotEmpty(message = "description must not be empty")
    private String description;
    private Instant createdAt;
    private Instant updatedAt;
    private TaskStatus status;
    private  User author;
    private  User assignee;
    private  Set<User>observers;
}
