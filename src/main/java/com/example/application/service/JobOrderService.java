package com.example.application.service;

import com.example.application.persistence.entity.JobOrderEntity;
import com.example.application.persistence.repository.JobOrderRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Service
public class JobOrderService implements FilterableCrudService<JobOrderEntity> {
    private final JobOrderRepository jobOrderRepository;
    private final UserService        userService;

    public JobOrderService(JobOrderRepository jobOrderRepository, UserService userService) {
        this.jobOrderRepository = jobOrderRepository;
        this.userService = userService;
    }

    @Transactional
    @Override
    public JobOrderEntity save(JobOrderEntity jobOrderEntity) {
        if (jobOrderEntity.getId() == null)
            jobOrderEntity.setCreatedAt(LocalDate.now());
        jobOrderEntity.setFirmEntity(userService.getUserFirm());
        return FilterableCrudService.super.save(jobOrderEntity);
    }

    @Override
    public void delete(JobOrderEntity entity) {
        entity.setCustomerEntity(null);
        FilterableCrudService.super.delete(entity);
    }

    @Override
    public void deleteById(long id) {
        FilterableCrudService.super.deleteById(id);
    }

    @Override
    public long count() {
        return FilterableCrudService.super.count();
    }

    @Override
    public JobOrderEntity load(long id) {
        return FilterableCrudService.super.load(id);
    }

    @Override
    public JpaRepository<JobOrderEntity, Long> getRepository() {
        return jobOrderRepository;
    }

    @Transactional(readOnly = true)
    public Set<JobOrderEntity> getFirmJobOrders() {
        var firm = userService.getUserFirm();
        return jobOrderRepository.findJobOrderByFirmEntityId(firm.getId());
    }

    @Override
    public long countAnyMatching(Specification<JobOrderEntity> specification) {
        return jobOrderRepository.count(specification);
    }

    @Override
    public Set<JobOrderEntity> filter(Specification<JobOrderEntity> specifications) {
        return new HashSet<>(jobOrderRepository.findAll(specifications));
    }

    @Override
    public Set<JobOrderEntity> filter(Specification<JobOrderEntity> specifications, int offset, int size) {
        return new HashSet<>(jobOrderRepository.findAll(specifications, PageRequest.of(offset / size, size)).getContent());
    }

    public JobOrderEntity getJobOrderWithDemands(long id) {
        return jobOrderRepository.findJobWithDemandsById(id);
    }

    public JobOrderEntity getJobOrderWithOffers(long id) {
        return jobOrderRepository.findJobWithOffersById(id);
    }
}
