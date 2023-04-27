package com.example.application.persistence.repository;

import com.example.application.persistence.entity.FirmEntity;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

public interface FirmRepository extends JpaRepository<FirmEntity, Long>, JpaSpecificationExecutor<FirmEntity> {

    @Override
    List<FirmEntity> findAll();

    @EntityGraph(attributePaths = {"defaultPatterns"})
    FirmEntity findWithDefaultPatternsById(Long id);

    @EntityGraph(attributePaths = {"patterns"})
    FirmEntity findWithCustomPatternsById(Long id);
}
