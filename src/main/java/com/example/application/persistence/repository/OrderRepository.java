package com.example.application.persistence.repository;

import com.example.application.persistence.entity.DemandEntity;
import com.example.application.persistence.entity.OfferEntity;
import com.example.application.persistence.entity.OrderEntity;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Set;

public interface OrderRepository extends JpaRepository<OrderEntity, Long>, JpaSpecificationExecutor<OrderEntity> {

    Set<OrderEntity> findOrderByCustomerEntityId(Long id);

    Set<OrderEntity> findOrderByFirmEntityId(Long id);

    @EntityGraph(attributePaths = {"orderPatterns"})
    OrderEntity getById(Long id);
}
