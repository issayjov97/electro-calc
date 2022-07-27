package com.example.application.persistence.repository;

import com.example.application.persistence.entity.FileEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Set;

public interface FileRepository extends JpaRepository<FileEntity, Long> {

    Set<FileEntity> findByOrderEntityId(Long id);
    Set<FileEntity> findByOfferEntityId(Long id);
}
