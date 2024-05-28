package com.example.tracker.service;

import com.example.tracker.dto.UserDto;
import com.example.tracker.entity.User;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Optional;

public interface UserService {
    Flux<UserDto> findAll();
    Mono<UserDto> findById(String id);
    Mono<UserDto>createUser(User user);
    Mono<UserDto> updateUser(String userId,User user);
    Mono<Void> deleteUser(String userId);
    Mono<User> createOrUpdate(User user);
    Mono<User> findByEmail(String email);
}
