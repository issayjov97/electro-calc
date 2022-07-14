package com.example.application.persistence.repository;

import com.example.application.persistence.entity.PatternEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Set;

public interface PatternRepository extends JpaRepository<PatternEntity, Integer> {

    @Query("FROM PatternEntity p WHERE p.userEntity.id IS NULL AND p.name NOT IN (?1)")
    Set<PatternEntity> findDefaultPatternsNotIn(List<String> excludedPatterns);

    @Query("FROM PatternEntity p WHERE p.userEntity.id IS NULL")
    Set<PatternEntity> findDefaultPattern();

    @Query("FROM PatternEntity p WHERE p.userEntity.id = ?1")
    Set<PatternEntity> findUserPatterns(String userId);

    @Query("FROM PatternEntity p WHERE p.userEntity.id = ?1")
    Set<PatternEntity> findOrderPatterns(String userId);
}
