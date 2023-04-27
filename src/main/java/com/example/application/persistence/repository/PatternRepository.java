package com.example.application.persistence.repository;

import com.example.application.persistence.entity.PatternEntity;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface PatternRepository extends JpaRepository<PatternEntity, Long>, JpaSpecificationExecutor<PatternEntity> {
    @Query("FROM PatternEntity p WHERE p.firmEntity is null and p.name =?1")
    PatternEntity findByName(String name);
    @Query("SELECT COUNT(p) FROM PatternEntity p WHERE p.firmEntity.id = ?1 OR  p.firmEntity.id is null")
    int count(Long firmId);
    @EntityGraph(attributePaths = {"offerPatterns", "firmEntities"})
    PatternEntity findFullPatternById(Long id);
    PatternEntity findPatternById(Long id);

    @Override
    long count(Specification<PatternEntity> specification);
    @Override
    @EntityGraph(attributePaths = {"firmEntity"})
    PatternEntity getOne(Long id);

}
