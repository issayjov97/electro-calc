package com.example.application.mapper;

import com.example.application.dto.UserDTO;
import com.example.application.persistence.entity.AuthorityEntity;
import com.example.application.persistence.entity.UserEntity;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.stream.Collectors;

public class UserMapper {

    public static UserDTO convertToDTO(UserEntity source) {
        final UserDTO userDTO = new UserDTO();
        userDTO.setId(source.getId());
        userDTO.setCreatedAt(source.getCreatedAt());
        userDTO.setEmail(source.getEmail());
        userDTO.setFirstName(source.getFirstName());
        userDTO.setLastName(source.getLastName());
        userDTO.setEnabled(source.getEnabled());
        userDTO.setUsername(source.getUsername());
        userDTO.setPassword(source.getPassword());
        userDTO.setAuthorities(source.getAuthorityEntities().stream().map(AuthorityEntity::getName)
                .collect(Collectors.toSet()));
        return userDTO;
    }

    public static User convertToUser(UserEntity source) {
        return new User(
                source.getUsername(),
                source.getPassword(),
                source.getAuthorityEntities().stream()
                        .map(it -> new SimpleGrantedAuthority(it.getName()))
                        .collect(Collectors.toList()));
    }

}
