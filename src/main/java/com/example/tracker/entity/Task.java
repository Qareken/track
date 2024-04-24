package com.example.tracker.entity;

import com.example.tracker.entity.enumurate.TaskStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.ReadOnlyProperty;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "task")
public class Task {
    @Id
    private String id;
    private String name;
    private String description;
    private Instant createdAt;
    private Instant updatedAt;

    private TaskStatus status;
    private String authorId;
    private String assigneeId;
    private Set<String> observerIds;
    @ReadOnlyProperty
    @Transient
    private transient User author;
    @ReadOnlyProperty
    @Transient
    private transient User assignee;
    @ReadOnlyProperty
    @Transient
    private transient Set<User>observers;
}
