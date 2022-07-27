package com.example.application.persistence.repository;

import com.example.application.persistence.entity.FirmEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FirmRepository extends JpaRepository<FirmEntity, Long> {

}
