package com.example.application.persistence.repository;

import com.example.application.persistence.entity.AuthorityEntity;
import com.example.application.persistence.entity.UserEntity;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.Set;

public interface AuthorityRepository extends JpaRepository<AuthorityEntity, Long> {

    Set<AuthorityEntity> findByNameIn(Set<String> names);
}
