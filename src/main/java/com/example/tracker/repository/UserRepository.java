package com.example.tracker.repository;

import com.example.tracker.entity.User;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
public interface UserRepository extends ReactiveMongoRepository<User,String> {
    Mono<User> findUserByUsernameAndEmail(String username, String email);
    Mono<User> findUserByUsername(String userName);
}
