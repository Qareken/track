package com.example.tracker.entity;

import com.example.tracker.entity.enumurate.RoleType;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.validation.annotation.Validated;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "user")
@Validated
public class User {
    @Id
    private String id;
    @NotEmpty(message = "Username must not be empty")
    private String username;
    @Email(message = "Email should be valid")
    private String email;
    private String password;
    @Field("roles")
    private Set<RoleType> roles = new HashSet<>();
   public Collection<GrantedAuthority> toAuthority(){
        return roles.stream().map(roleType -> new SimpleGrantedAuthority(roleType.name())).collect(Collectors.toSet());
    }
}
