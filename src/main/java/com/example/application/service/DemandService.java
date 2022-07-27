package com.example.application.service;

import com.example.application.mapper.PatternMapper;
import com.example.application.persistence.entity.DemandEntity;
import com.example.application.persistence.entity.PatternEntity;
import com.example.application.persistence.repository.DemandRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class DemandService implements CrudService<DemandEntity> {
    private final DemandRepository demandRepository;
    private final UserService      userService;
    private final FirmService firmService;

    public DemandService(DemandRepository demandRepository, UserService userService, FirmService firmService) {
        this.demandRepository = demandRepository;
        this.userService = userService;
        this.firmService = firmService;
    }

    @Transactional
    @Override
    public DemandEntity save(DemandEntity demandEntity) {
        if (demandEntity.getId() == null)
            demandEntity.setCreatedAt(LocalDateTime.now());
        demandEntity.setFirmEntity(userService.getUserFirm());
        return CrudService.super.save(demandEntity);
    }

    @Override
    public void delete(DemandEntity entity) {
        entity.setCustomerEntity(null);
        CrudService.super.delete(entity);
    }

    @Override
    public void deleteById(long id) {
        CrudService.super.deleteById(id);
    }

    @Override
    public long count() {
        return CrudService.super.count();
    }

    @Override
    public DemandEntity load(long id) {
        return CrudService.super.load(id);
    }

    @Override
    public JpaRepository<DemandEntity, Long> getRepository() {
        return demandRepository;
    }

    @Transactional(readOnly = true)
    public Set<DemandEntity> getUserOffers() {
        return userService.getUserFirm().getDemands();
    }

    @Transactional
    public void addDemandPatterns(Set<PatternEntity> orderPatterns, DemandEntity demandEntity) {
        var firmEntity = userService.getUserFirm();
        var addedFirmPatterns = orderPatterns.stream().filter(it -> it.getFirmEntity() == null)
                .map(it -> PatternMapper.convertToEntity(it, firmEntity))
                .collect(Collectors.toSet());
        firmEntity.getPatterns().addAll(addedFirmPatterns);
        demandEntity.addPatterns(orderPatterns);
        firmService.save(firmEntity);
        save(demandEntity);
    }
}
