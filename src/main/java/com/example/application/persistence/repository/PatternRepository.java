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

    @Query("FROM PatternEntity p WHERE p.firmEntity.id IS NULL AND p.name NOT IN (?1) order by p.PLU")
    Set<PatternEntity> findDefaultPatternsNotIn(List<String> excludedPatterns);

    @Query("FROM PatternEntity p WHERE p.firmEntity.id IS NULL order by p.PLU")
    Set<PatternEntity> findByDefaultPatternsFirmId();

    @Query("FROM PatternEntity p WHERE p.firmEntity.id = ?1 order by p.PLU")
    Set<PatternEntity> findFirmPatterns(Long id);

    @Query("FROM PatternEntity p WHERE p.firmEntity.id = ?1 order by p.PLU")
    Set<PatternEntity> findOrderPatterns(String userId);

    PatternEntity findByName(String name);

    List<PatternEntity> findAll(Specification<PatternEntity> specification);

    @Query("SELECT COUNT(DISTINCT p.PLU) FROM PatternEntity p WHERE p.firmEntity.id = ?1 OR  p.firmEntity.id is null")
    int count(Long firmId);

    @EntityGraph(attributePaths = {"orders", "demands", "offers"})
    PatternEntity findFullPatternById(Long id);

    @EntityGraph(attributePaths = {"offers"})
    PatternEntity findPatternWithOffersById(Long id);

    @Override
    long count(Specification<PatternEntity> specification);

    @Query("SELECT p FROM PatternEntity p JOIN FETCH p.firmEntities f WHERE f.id =?1")
    Set<PatternEntity> findByDefaultPatternsFirmEntityId(Long id);

    //
//    @Query("select p from PatternEntity p join p. join i.documents d where c.customerNumber = ?1 and i.itemNumber = ?2 and d.validDate = ?3")
//    Page<CustomerEntity> getCustomers(Pageable pageable);
}
