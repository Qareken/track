package com.example.tracker.controller;

import com.example.tracker.dto.UserDto;
import com.example.tracker.mapper.UserMapper;
import com.example.tracker.service.UserService;
import com.example.tracker.service.impl.TaskServiceImpl;
import com.example.tracker.service.impl.UserServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/v1/track/user")
@RequiredArgsConstructor
public class UserController {
    private final UserServiceImpl userService;
    private final TaskServiceImpl taskService;
    private final UserMapper userMapper;
    @GetMapping
    public Flux<UserDto> findAll(){
        return userService.findAll();
    }
    @GetMapping("/by-id/{id}")
    public Mono<ResponseEntity<UserDto>> findById(@PathVariable String id){
        return userService.findById(id).map(ResponseEntity::ok).defaultIfEmpty(ResponseEntity.notFound().build());
    }
   @PostMapping
   public Mono<ResponseEntity<UserDto>> createUser(@RequestBody UserDto userDto){
        return userService.createUser(userMapper.toEntity(userDto)).map(ResponseEntity::ok);
   }
   @PutMapping("/{id}")
    public Mono<ResponseEntity<UserDto>> updateUser(@PathVariable String id, @RequestBody UserDto userDto){
        return userService.updateUser(id, userMapper.toEntity(userDto)).map(ResponseEntity::ok).defaultIfEmpty(ResponseEntity.notFound().build());
   }
   @DeleteMapping("/{id}")
   public Mono<ResponseEntity<Void>> deleteUser(@PathVariable(name = "id") String id){
        return taskService.deleteUser(id).then(Mono.just(ResponseEntity.noContent().build()));
   }
}
