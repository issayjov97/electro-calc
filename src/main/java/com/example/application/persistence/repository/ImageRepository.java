package com.example.application.persistence.repository;

import com.example.application.persistence.entity.ImageEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ImageRepository extends JpaRepository<ImageEntity, Long> {

    @Query("FROM ImageEntity  where imagePatterns IN(?1)")
    List<ImageEntity> find(List<Long> ids);
}
