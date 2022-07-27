package com.example.application.service;

import com.example.application.mapper.PatternMapper;
import com.example.application.persistence.entity.OfferEntity;
import com.example.application.persistence.entity.PatternEntity;
import com.example.application.persistence.repository.OfferRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class OfferService implements CrudService<OfferEntity> {
    private final OfferRepository offerRepository;
    private final UserService     userService;
    private final FirmService     firmService;

    public OfferService(OfferRepository offerRepository, UserService userService, FirmService firmService) {
        this.offerRepository = offerRepository;
        this.userService = userService;
        this.firmService = firmService;
    }

    @Transactional
    @Override
    public OfferEntity save(OfferEntity offerEntity) {
        if (offerEntity.getId() == null)
            offerEntity.setCreatedAt(LocalDateTime.now());
        offerEntity.setFirmEntity(userService.getUserFirm());
        return CrudService.super.save(offerEntity);
    }

    @Override
    public void delete(OfferEntity entity) {
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
    public OfferEntity load(long id) {
        return CrudService.super.load(id);
    }

    @Override
    public JpaRepository<OfferEntity, Long> getRepository() {
        return offerRepository;
    }

    @Transactional(readOnly = true)
    public Set<OfferEntity> getFirmOffers() {
        return userService.getUserFirm().getOffers();
    }

    @Transactional
    public void addOfferPatterns(Set<PatternEntity> orderPatterns, OfferEntity offerEntity) {
        var firmEntity = userService.getUserFirm();
        var addedFirmPatterns = orderPatterns.stream().filter(it -> it.getFirmEntity() == null)
                .map(it -> PatternMapper.convertToEntity(it, firmEntity))
                .collect(Collectors.toSet());
        firmEntity.getPatterns().addAll(addedFirmPatterns);
        offerEntity.addPatterns(orderPatterns);
        firmService.save(firmEntity);
        save(offerEntity);
    }
}
