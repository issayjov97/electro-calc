package com.example.application.persistence.repository;

import com.example.application.persistence.entity.DemandEntity;
import com.example.application.persistence.entity.JobOrderEntity;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Set;

public interface JobOrderRepository extends JpaRepository<JobOrderEntity, Long>, JpaSpecificationExecutor<JobOrderEntity> {

    Set<JobOrderEntity> findJobOrderByFirmEntityId(Long id);

    @EntityGraph(attributePaths = {"demandEntities"})
    JobOrderEntity findJobWithDemandsById(Long id);

    @EntityGraph(attributePaths = {"offerEntities"})
    JobOrderEntity findJobWithOffersById(Long id);
}
