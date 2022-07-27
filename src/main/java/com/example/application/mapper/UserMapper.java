package com.example.application.mapper;

import com.example.application.persistence.entity.UserEntity;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.stream.Collectors;

public class UserMapper {

    public static User convertToUser(UserEntity source) {
        return new User(
                source.getUsername(),
                source.getPassword(),
                source.getAuthorityEntities().stream()
                        .map(it -> new SimpleGrantedAuthority(it.getName()))
                        .collect(Collectors.toList()));
    }

}