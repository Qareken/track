package com.example.tracker.controller;

import com.example.tracker.dto.UserDto;
import com.example.tracker.mapper.UserMapper;
import com.example.tracker.service.impl.TaskServiceImpl;
import com.example.tracker.service.impl.UserServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
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
    @PreAuthorize("hasAnyRole('ROLE_USER', 'ROLE_MANAGER')")
    public Flux<UserDto> findAll(@AuthenticationPrincipal UserDetails currentUser){
        return userService.findAll();
    }
    @GetMapping("/by-id/{id}")
    @PreAuthorize("hasAnyRole('ROLE_USER', 'ROLE_MANAGER')")
    public Mono<ResponseEntity<UserDto>> findById(@PathVariable String id, @AuthenticationPrincipal UserDetails currentUser){
        return userService.findById(id).map(ResponseEntity::ok).defaultIfEmpty(ResponseEntity.notFound().build());
    }
   @PostMapping("/register")
   public Mono<ResponseEntity<UserDto>> createUser(@RequestBody UserDto userDto){
        return userService.createUser(userMapper.toEntity(userDto)).map(ResponseEntity::ok);
   }
   @PutMapping("/{id}")
   @PreAuthorize("hasAnyRole('ROLE_USER', 'ROLE_MANAGER')")
    public Mono<ResponseEntity<UserDto>> updateUser(@PathVariable String id, @RequestBody UserDto userDto, @AuthenticationPrincipal UserDetails currentUser){
        return userService.updateUser(id, userMapper.toEntity(userDto)).map(ResponseEntity::ok).defaultIfEmpty(ResponseEntity.notFound().build());
   }
   @DeleteMapping("/{id}")
   @PreAuthorize("hasAnyRole('ROLE_USER', 'ROLE_MANAGER')")
   public Mono<ResponseEntity<Void>> deleteUser(@PathVariable(name = "id") String id,  @AuthenticationPrincipal UserDetails currentUser){
        return taskService.deleteUser(id).then(Mono.just(ResponseEntity.noContent().build()));
   }

}
