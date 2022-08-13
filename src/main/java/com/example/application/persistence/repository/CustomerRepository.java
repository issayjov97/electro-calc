package com.example.application.persistence.repository;

import com.example.application.persistence.entity.CustomerEntity;
import com.example.application.persistence.entity.PatternEntity;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Set;

public interface CustomerRepository extends JpaRepository<CustomerEntity, Long>, JpaSpecificationExecutor<CustomerEntity> {

    Set<CustomerEntity> findCustomersByFirmEntityId(Long id);

    @Override
    List<CustomerEntity> findAll(Specification<CustomerEntity> specification);

    @Query("SELECT COUNT(c) FROM CustomerEntity c WHERE c.firmEntity.id = ?1")
    int count(Long firmId);
}
