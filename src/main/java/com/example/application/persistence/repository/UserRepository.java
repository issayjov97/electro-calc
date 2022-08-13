package com.example.application.persistence.repository;

import com.example.application.persistence.entity.UserEntity;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<UserEntity, Long> {

    @EntityGraph(attributePaths = {"authorityEntities"})
    Optional<UserEntity> findFullUserByUsername(String username);

    @EntityGraph(attributePaths = {"firmEntity"})
    Optional<UserEntity> findBriefUserByUsername(String username);

    @Override
    @EntityGraph(attributePaths = {"authorityEntities"})
    List<UserEntity> findAll();
}
