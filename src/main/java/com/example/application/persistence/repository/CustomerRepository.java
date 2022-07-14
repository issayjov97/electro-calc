package com.example.application.persistence.repository;

import com.example.application.persistence.entity.CustomerEntity;
import org.springframework.data.jpa.repository.JpaRepository;
public interface CustomerRepository extends JpaRepository<CustomerEntity, Long> {
}
