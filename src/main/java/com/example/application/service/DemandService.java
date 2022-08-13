package com.example.application.service;

import com.example.application.persistence.entity.DemandEntity;
import com.example.application.persistence.entity.PatternEntity;
import com.example.application.persistence.repository.DemandRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Service
public class DemandService implements FilterableCrudService<DemandEntity> {
    private final DemandRepository demandRepository;
    private final UserService      userService;

    public DemandService(DemandRepository demandRepository, UserService userService, FirmService firmService) {
        this.demandRepository = demandRepository;
        this.userService = userService;
    }

    @Transactional
    @Override
    public DemandEntity save(DemandEntity demandEntity) {
        if (demandEntity.getId() == null) {
            demandEntity.setCreatedAt(LocalDateTime.now());
            demandEntity.setFirmEntity(userService.getUserFirm());
        }
        return FilterableCrudService.super.save(demandEntity);
    }

    @Override
    public void delete(DemandEntity entity) {
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
    public DemandEntity load(long id) {
        return FilterableCrudService.super.load(id);
    }

    @Override
    public JpaRepository<DemandEntity, Long> getRepository() {
        return demandRepository;
    }

    @Transactional(readOnly = true)
    public Set<DemandEntity> getFirmDemands() {
        var firm = userService.getUserFirm();
        return demandRepository.findDemandByFirmEntityId(firm.getId());
    }

    @Override
    public long countAnyMatching(Specification<DemandEntity> specification) {
        return demandRepository.count(specification);
    }

    @Override
    public Set<DemandEntity> filter(Specification<DemandEntity> specifications) {
        return new HashSet<>(demandRepository.findAll(specifications));
    }

    @Override
    public Set<DemandEntity> filter(Specification<DemandEntity> specifications, int offset, int size) {
        return new HashSet<>(demandRepository.findAll(specifications, PageRequest.of(offset / size, size)).getContent());
    }

    @Transactional
    public void addDemandPatterns(Set<PatternEntity> demandPatterns, DemandEntity demandEntity) {
        var demand = demandRepository.getById(demandEntity.getId());
        demand.getPatterns().addAll(demandPatterns);
        save(demand);
    }

    public DemandEntity getById(long id) {
        return FilterableCrudService.super.load(id);
    }
}
