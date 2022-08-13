package com.example.application.persistence.repository;

import com.example.application.persistence.entity.FirmEntity;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface FirmRepository extends JpaRepository<FirmEntity, Long> {

    @Override
    List<FirmEntity> findAll();


    @EntityGraph(attributePaths = {"defaultPatterns"})
    FirmEntity getById(Long id);


}
