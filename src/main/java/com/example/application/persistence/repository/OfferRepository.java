package com.example.application.persistence.repository;

import com.example.application.persistence.entity.OfferEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Set;

public interface OfferRepository extends JpaRepository<OfferEntity, Long>, JpaSpecificationExecutor<OfferEntity> {

    @EntityGraph(attributePaths = {"offerPatterns","customerEntity"})
    OfferEntity getById(Long id);

    @EntityGraph(attributePaths = {"offerPatterns","firmEntity","customerEntity"})
    OfferEntity findFullOfferById(Long id);

    @Query("FROM OfferEntity o where o.customerEntity.id  =?1")
    @EntityGraph(attributePaths = {"offerPatterns","firmEntity","customerEntity"})
    Set<OfferEntity> getCustomerOffers(Long id);

    @Override
    @EntityGraph(attributePaths = {"offerPatterns", "firmEntity", "customerEntity"})
    List<OfferEntity> findAll(Specification<OfferEntity> specification);

    @Override
    @EntityGraph(attributePaths = {"offerPatterns", "firmEntity", "customerEntity"})
    Page<OfferEntity> findAll(Specification<OfferEntity> specification, Pageable pageable);
}
