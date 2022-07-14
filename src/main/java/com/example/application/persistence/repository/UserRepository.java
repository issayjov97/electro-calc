package com.example.application.persistence.repository;

import com.example.application.persistence.entity.UserEntity;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<UserEntity, Long> {

    @EntityGraph(attributePaths = {"patterns", "authorityEntities"})
    Optional<UserEntity> findByUsername(String username);
}
