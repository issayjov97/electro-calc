package com.example.application.persistence.repository;

import com.example.application.persistence.entity.PatternEntity;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Set;

public interface PatternRepository extends JpaRepository<PatternEntity, Long>, JpaSpecificationExecutor<PatternEntity> {

    @Query("FROM PatternEntity p WHERE p.firmEntity.id IS NULL AND p.name NOT IN (?1)")
    Set<PatternEntity> findDefaultPatternsNotIn(List<String> excludedPatterns);

    @Query("FROM PatternEntity p WHERE p.firmEntity.id IS NULL")
    Set<PatternEntity> findByDefaultPatternsFirmId();

    @Query("FROM PatternEntity p WHERE p.firmEntity.id = ?1")
    Set<PatternEntity> findFirmPatterns(Long id);

    @Query("FROM PatternEntity p WHERE p.firmEntity.id = ?1")
    Set<PatternEntity> findOrderPatterns(String userId);

    @Query("FROM PatternEntity p WHERE p.firmEntity is null and p.name =?1 and p.description =?2 and p.duration = ?3")
    PatternEntity findByName(String name, String description, double duration);

    List<PatternEntity> findAll(Specification<PatternEntity> specification);

    @Query("SELECT COUNT(p) FROM PatternEntity p WHERE p.firmEntity.id = ?1 OR  p.firmEntity.id is null")
    int count(Long firmId);

    @EntityGraph(attributePaths = {"offerPatterns", "firmEntities"})
    PatternEntity findFullPatternById(Long id);

    @EntityGraph(attributePaths = {"firmEntities"})
    PatternEntity findPatternWithFirmsById(Long id);

    @EntityGraph(attributePaths = {"offers"})
    PatternEntity findPatternWithOffersById(Long id);

    @Override
    long count(Specification<PatternEntity> specification);

    @Query("SELECT p FROM PatternEntity p")
    Set<PatternEntity> findByDefaultPatternsFirmEntityId(Long id);

    @Override
    @EntityGraph(attributePaths = {"firmEntity"})
    PatternEntity getOne(Long aLong);

}
