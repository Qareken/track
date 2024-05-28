package com.example.tracker.security;

import com.example.tracker.entity.User;
import com.example.tracker.service.impl.UserServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.ReactiveUserDetailsService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements ReactiveUserDetailsService {
    protected final UserServiceImpl userService;
    @Override
    public Mono<UserDetails> findByUsername(String username) {
        return userService.findByEmail(username).map(user -> (UserDetails) new AppUserDetails(user)).switchIfEmpty(Mono.error(new UsernameNotFoundException("User not found")));
    }
}
