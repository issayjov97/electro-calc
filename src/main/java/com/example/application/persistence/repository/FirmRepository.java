package com.example.application.persistence.repository;

import com.example.application.persistence.entity.FirmEntity;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import java.util.List;
import java.util.Optional;

public interface FirmRepository extends JpaRepository<FirmEntity, Long>, JpaSpecificationExecutor<FirmEntity> {

    @Override
    List<FirmEntity> findAll();


    @EntityGraph(attributePaths = {"defaultPatterns"})
    FirmEntity getById(Long id);

    @EntityGraph(attributePaths = {"defaultPatterns","patterns"})
    FirmEntity findWithAllPatternsById(Long id);

    @Override
    @EntityGraph(attributePaths = {"defaultPatterns","patterns"})
    Optional<FirmEntity> findOne(Specification<FirmEntity> specification);

    @Override
    @EntityGraph(attributePaths = {"defaultPatterns","patterns"})
    List<FirmEntity> findAll(Specification<FirmEntity> specification);

}
