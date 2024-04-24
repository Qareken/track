package com.example.tracker.service;

import com.example.tracker.dto.TaskDto;
import com.example.tracker.dto.UserDto;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface TaskService {
    Flux<TaskDto> findAll();
    Mono<TaskDto> findById(String id);
    Mono<TaskDto> create(TaskDto taskDto);
    Mono<TaskDto> addObservers(String taskId, UserDto userDto);
    Mono<Void> deleteById(String id);
    Mono<TaskDto> update(String id, TaskDto taskDto);
    Mono<Void> deleteUser(String userId);
}
