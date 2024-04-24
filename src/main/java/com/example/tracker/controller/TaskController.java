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
    public Flux<TaskDto> getAllItems() {

       taskService.findAll().collectList().subscribe(System.out::println);
        return taskService.findAll();
    }
    @GetMapping("/by-id/{id}")
    public Mono<ResponseEntity<TaskDto>> findById(@PathVariable(name = "id")  String id) {
        return taskService.findById(id).map(ResponseEntity::ok).defaultIfEmpty(ResponseEntity.notFound().build());
    }
    @PutMapping("/{id}")
    public Mono<ResponseEntity<TaskDto>> addObserver(@PathVariable String id, @RequestBody UserDto userDto) {
        return taskService.addObservers(id, userDto).map(ResponseEntity::ok).defaultIfEmpty(ResponseEntity.notFound().build());
    }
    @PutMapping("/update/{id}")
    public Mono<ResponseEntity<TaskDto>>update(@PathVariable String id, @RequestBody TaskDto taskDto){
        return taskService.update(id, taskDto).map(ResponseEntity::ok).defaultIfEmpty(ResponseEntity.notFound().build());
    }
    @PostMapping
    public Mono<ResponseEntity<TaskDto>> createTask(@RequestBody TaskDto taskDto) {
        return taskService.create(taskDto).doOnSuccess(taskUpdatePublisher::publish).map(ResponseEntity::ok);
    }
    @DeleteMapping("/{id}")
    public Mono<ResponseEntity<Void>> deleteTask(@PathVariable String id) {
        return taskService.deleteById(id).then(Mono.just(ResponseEntity.noContent().build()));
    }
    @GetMapping(value = "/stream", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<ServerSentEvent<TaskDto>> getTaskUpdates() {
        return taskUpdatePublisher.getUpdatesSinks().asFlux().map(item ->
                ServerSentEvent.builder(item).build());
    }
}

