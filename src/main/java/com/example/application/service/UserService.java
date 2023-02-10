package com.example.application.service;

import com.example.application.persistence.entity.AuthorityEntity;
import com.example.application.persistence.entity.FirmEntity;
import com.example.application.persistence.entity.UserEntity;
import com.example.application.persistence.repository.UserRepository;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@Service
public class UserService implements CrudService<UserEntity> {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(
            UserRepository userRepository,
            PasswordEncoder passwordEncoder
    ) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional(readOnly = true)
    @Override
    public List<UserEntity> loadAll() {
        return userRepository.findAll();
    }

    @Override
    public JpaRepository<UserEntity, Long> getRepository() {
        return userRepository;
    }

    @Transactional
    @CacheEvict(value = "firms", allEntries = true)
    @Override
    public UserEntity save(UserEntity userEntity) {
        userEntity.setCreatedAt(LocalDateTime.now());
        if (userEntity.getId() == null)
            userEntity.setPassword(passwordEncoder.encode(userEntity.getPassword()));
        return CrudService.super.save(userEntity);
    }

    @Transactional(readOnly = true)
    public UserEntity getByUsername(String username) {
        return userRepository.findFullUserByUsername(username).orElseThrow(() -> {
            throw new EntityNotFoundException("User not found");
        });
    }

    @Transactional(readOnly = true)
    @Cacheable(value = "firms", key = "#username")
    public FirmEntity getUserFirm(String username) {
        var user = userRepository.findBriefUserByUsername(username).orElseThrow(() -> {
            throw new EntityNotFoundException("User not found");
        });
        return user.getFirmEntity();
    }

    @Transactional(readOnly = true)
    public Set<AuthorityEntity> getUserAuthorities() {
        var user = userRepository.findBriefUserByUsername(AuthService.getUsername()).orElseThrow(() -> {
            throw new EntityNotFoundException("User not found");
        });
        return user.getAuthorityEntities();
    }

}
