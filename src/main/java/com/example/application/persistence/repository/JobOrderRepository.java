package com.example.application.persistence.repository;

import com.example.application.persistence.entity.JobOrderEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Set;

public interface JobOrderRepository extends JpaRepository<JobOrderEntity, Long> {
}
