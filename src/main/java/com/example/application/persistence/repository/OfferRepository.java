package com.example.application.persistence.repository;

import com.example.application.persistence.entity.OfferEntity;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Set;

public interface OfferRepository extends JpaRepository<OfferEntity, Long>, JpaSpecificationExecutor<OfferEntity> {

    Set<OfferEntity> findOfferByFirmEntityId(Long id);

    @EntityGraph(attributePaths = {"offerPatterns"})
    OfferEntity getById(Long id);
}
