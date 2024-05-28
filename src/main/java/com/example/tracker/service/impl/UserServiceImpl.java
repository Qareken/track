package com.example.tracker.service.impl;

import com.example.tracker.dto.UserDto;
import com.example.tracker.entity.User;
import com.example.tracker.mapper.UserMapper;
import com.example.tracker.repository.UserRepository;
import com.example.tracker.service.UserService;

import lombok.RequiredArgsConstructor;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;
    @Override
    public Flux<UserDto> findAll() {
        return userRepository.findAll().map(userMapper::toDto);
    }

    @Override
    public Mono<UserDto> findById(String id) {
        return userRepository.findById(id).map(userMapper::toDto);
    }

    @Override
    public Mono<UserDto> createUser(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepository.findUserByUsernameAndEmail(user.getUsername(), user.getEmail()).switchIfEmpty(userRepository.save(user)).map(userMapper::toDto);
    }


    @Override
    public Mono<UserDto> updateUser(String userId, User user) {
        return userRepository.findById(userId).flatMap(existedUser->{
            if(user.getUsername()!=null) existedUser.setUsername(user.getUsername());
            if(user.getEmail()!=null) existedUser.setEmail(user.getEmail());
            if(user.getPassword()!=null) existedUser.setPassword(user.getPassword());
            return createUser(existedUser);
        });

    }

    @Override
    public Mono<Void> deleteUser(String userId) {
        return userRepository.deleteById(userId);
    }

    @Override
    public Mono<User> createOrUpdate(User user) {
        return userRepository.findUserByUsernameAndEmail(user.getUsername(), user.getEmail()).switchIfEmpty(userRepository.save(user));
    }

    @Override
    public Mono<User> findByEmail(String email) {
        return userRepository.findUserByEmail(email);
    }
}
