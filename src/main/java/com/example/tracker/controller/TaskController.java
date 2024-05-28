package com.example.tracker.controller;


import com.example.tracker.mapper.TaskMapper;
import com.example.tracker.mapper.UserMapper;
import com.example.tracker.publisher.TaskUpdatePublisher;
import com.example.tracker.dto.TaskDto;
import com.example.tracker.dto.UserDto;

import com.example.tracker.service.impl.TaskServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.codec.ServerSentEvent;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/v1/track/task")
@RequiredArgsConstructor
public class TaskController {

    private final TaskServiceImpl taskService;
    private final TaskUpdatePublisher taskUpdatePublisher;


    @GetMapping
    @PreAuthorize("hasAnyRole('ROLE_USER', 'ROLE_MANAGER')")
    public Flux<TaskDto> getAllItems(@AuthenticationPrincipal UserDetails currentUser) {

       taskService.findAll().collectList().subscribe(System.out::println);
        return taskService.findAll();
    }
    @GetMapping("/by-id/{id}")
    @PreAuthorize("hasAnyRole('ROLE_USER', 'ROLE_MANAGER')")
    public Mono<ResponseEntity<TaskDto>> findById(@PathVariable(name = "id")  String id,@AuthenticationPrincipal UserDetails currentUser ) {
        return taskService.findById(id).map(ResponseEntity::ok).defaultIfEmpty(ResponseEntity.notFound().build());
    }
    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ROLE_USER', 'ROLE_MANAGER')")
    public Mono<ResponseEntity<TaskDto>> addObserver(@PathVariable String id, @RequestBody UserDto userDto,@AuthenticationPrincipal UserDetails currentUser) {
        return taskService.addObservers(id, userDto).map(ResponseEntity::ok).defaultIfEmpty(ResponseEntity.notFound().build());
    }
    @PutMapping("/update/{id}")
    public Mono<ResponseEntity<TaskDto>>update(@PathVariable String id, @RequestBody TaskDto taskDto){
        return taskService.update(id, taskDto).map(ResponseEntity::ok).defaultIfEmpty(ResponseEntity.notFound().build());
    }
    @PostMapping
    @PreAuthorize("hasRole('ROLE_MANAGER')")
    public Mono<ResponseEntity<TaskDto>> createTask(@RequestBody TaskDto taskDto,@AuthenticationPrincipal UserDetails currentUser) {
        return taskService.create(taskDto).doOnSuccess(taskUpdatePublisher::publish).map(ResponseEntity::ok);
    }
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_MANAGER')")
    public Mono<ResponseEntity<Void>> deleteTask(@PathVariable String id,@AuthenticationPrincipal UserDetails currentUser) {
        return taskService.deleteById(id).then(Mono.just(ResponseEntity.noContent().build()));
    }
    @GetMapping(value = "/stream", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    @PreAuthorize("hasRole('ROLE_MANAGER')")
    public Flux<ServerSentEvent<TaskDto>> getTaskUpdates(@AuthenticationPrincipal UserDetails currentUser) {
        return taskUpdatePublisher.getUpdatesSinks().asFlux().map(item ->
                ServerSentEvent.builder(item).build());
    }
}

