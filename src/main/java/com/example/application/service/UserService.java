package com.example.application.service;

import com.example.application.persistence.entity.FirmEntity;
import com.example.application.persistence.entity.UserEntity;
import com.example.application.persistence.repository.UserRepository;
import com.example.application.predicate.UserPredicate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@Service
public class UserService implements CrudService<UserEntity> {
    private final UserRepository  userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(
            UserRepository userRepository,
            PasswordEncoder passwordEncoder
    ) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public JpaRepository<UserEntity, Long> getRepository() {
        return userRepository;
    }

    @Transactional
    @Override
    public UserEntity save(UserEntity userEntity) {
        userEntity.setCreatedAt(LocalDateTime.now());
        if (userEntity.getId() == null)
            userEntity.setPassword(passwordEncoder.encode(userEntity.getPassword()));
        return CrudService.super.save(userEntity);
    }

    @Override
    public void delete(UserEntity entity) {
        CrudService.super.delete(entity);
    }

    @Override
    public void deleteById(long id) {
        CrudService.super.deleteById(id);
    }

    @Override
    public long count() {
        return CrudService.super.count();
    }

    @Override
    public UserEntity load(long id) {
        return CrudService.super.load(id);
    }

    @Transactional(readOnly = true)
    @Override
    public List<UserEntity> loadAll() {
        return CrudService.super.loadAll();
    }

    @Transactional(readOnly = true)
    public UserEntity getByUsername(String username) {
        return userRepository.findByUsername(username).orElseThrow(() -> {
            throw new RuntimeException("User not found");
        });
    }

    @Transactional(readOnly = true)
    public FirmEntity getUserFirm() {
        var user = userRepository.findByUsername(AuthService.getUsername()).orElseThrow(() -> {
            throw new RuntimeException("User not found");
        });
        return user.getFirmEntity();
    }

    public Set<UserEntity> filter(String username, String email) {
        List<UserEntity> result = loadAll();
        if (username != null && !username.isBlank()) {
            UserPredicate.withUsername(username);
        }
        if (email != null && !email.isBlank()) {
            UserPredicate.withEmail(email);
        }
        return UserPredicate.filterUsers(result);
    }
}
