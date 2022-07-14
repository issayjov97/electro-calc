package com.example.application.service;

import com.example.application.dto.UserDTO;
import com.example.application.mapper.UserMapper;
import com.example.application.persistence.entity.AuthorityEntity;
import com.example.application.persistence.entity.UserEntity;
import com.example.application.persistence.repository.AuthorityRepository;
import com.example.application.persistence.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserService {

    private final UserRepository      userRepository;
    private final PasswordEncoder     passwordEncoder;
    private final AuthorityRepository authorityRepository;

    public UserService(
            UserRepository userRepository,
            PasswordEncoder passwordEncoder,
            AuthorityRepository authorityRepository
    ) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.authorityRepository = authorityRepository;
    }

    @Transactional
    public void save(UserDTO userDTO) {
        final UserEntity userEntity = userRepository.findById(userDTO.getId())
                .orElse(new UserEntity());
        userEntity.setCreatedAt(LocalDateTime.now());
        userEntity.setUsername(userDTO.getUsername());
        userEntity.setFirstName(userDTO.getFirstName());
        userEntity.setLastName(userDTO.getLastName());
        userEntity.setEmail(userDTO.getEmail());
        userEntity.setEnabled(userDTO.getEnabled());
        // TODO: if no authorities
        if (!userDTO.getAuthorities().isEmpty()) {
            var userAuthorities = authorityRepository.findByNameIn(userDTO.getAuthorities());
            userEntity.setAuthorityEntities(userAuthorities);
        }
        if (userEntity.getPassword() == null)
            userEntity.setPassword(passwordEncoder.encode(userDTO.getPassword()));
        userRepository.save(userEntity);
    }

    @Transactional
    public void saveUserEntity(UserEntity userEntity) {
        userRepository.saveAndFlush(userEntity);
    }

    public List<UserDTO> getAll() {
        return userRepository.findAll().stream().map(UserMapper::convertToDTO)
                .collect(Collectors.toList());
    }

    public List<AuthorityEntity> getAuthorities() {
        return authorityRepository.findAll();
    }


    public List<String> getMappedAuthorities() {
        return authorityRepository.findAll().stream().map(AuthorityEntity::getName).collect(Collectors.toList());
    }


    public void addAuthority(AuthorityEntity authorityEntity) {
        authorityRepository.save(authorityEntity);
    }

    public void deleteAuthority(Long id) {
        authorityRepository.deleteById(id);
    }


    public UserEntity getByUsername(String username) {
        return userRepository.findByUsername(username).orElseThrow(() -> {
            throw new RuntimeException("User not found");
        });
    }

    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }

    @Transactional
    public void updateUser(UserDTO userDTO) {
        UserEntity user = userRepository.findByUsername(AuthService.getUsername()).orElseThrow(() -> {
            throw new RuntimeException("User not found");
        });

        if (userDTO.getFirstName() != null && !userDTO.getFirstName().isBlank())
            user.setFirstName(userDTO.getFirstName());

        if (userDTO.getLastName() != null && !userDTO.getLastName().isBlank())
            user.setLastName(userDTO.getLastName());

        if (userDTO.getEmail() != null && !userDTO.getEmail().isBlank())
            user.setEmail(userDTO.getEmail());

        if (userDTO.getUsername() != null && !userDTO.getUsername().isBlank())
            user.setUsername(userDTO.getUsername());

        if (userDTO.getPassword() != null && !userDTO.getPassword().isBlank())
            user.setPassword(passwordEncoder.encode(userDTO.getPassword()));

        userRepository.save(user);
    }

    public void deletePattern(UserDTO userDTO) {
        userRepository.deleteById(userDTO.getId());
    }

}
