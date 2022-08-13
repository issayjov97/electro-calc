package com.example.application.service;

import com.example.application.mapper.PatternMapper;
import com.example.application.persistence.entity.OfferEntity;
import com.example.application.persistence.entity.PatternEntity;
import com.example.application.persistence.repository.OfferRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class OfferService implements FilterableCrudService<OfferEntity> {
    private final OfferRepository offerRepository;
    private final UserService     userService;
    private final FirmService     firmService;
    private final PatternService  patternService;

    public OfferService(OfferRepository offerRepository, UserService userService, FirmService firmService, PatternService patternService) {
        this.offerRepository = offerRepository;
        this.userService = userService;
        this.firmService = firmService;
        this.patternService = patternService;
    }

    @Transactional
    @Override
    public OfferEntity save(OfferEntity offerEntity) {
        if (offerEntity.getId() == null)
            offerEntity.setCreatedAt(LocalDateTime.now());
        offerEntity.setFirmEntity(userService.getUserFirm());
        return FilterableCrudService.super.save(offerEntity);
    }

    @Override
    public void delete(OfferEntity entity) {
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
    public OfferEntity load(long id) {
        return offerRepository.getById(id);
    }

    @Override
    public JpaRepository<OfferEntity, Long> getRepository() {
        return offerRepository;
    }

    @Transactional(readOnly = true)
    public Set<OfferEntity> getFirmOffers() {
        var firm = userService.getUserFirm();
        return offerRepository.findOfferByFirmEntityId(firm.getId());
    }

    @Override
    public long countAnyMatching(Specification<OfferEntity> specification) {
        return offerRepository.count(specification);
    }

    @Override
    public Set<OfferEntity> filter(Specification<OfferEntity> specifications) {
        return new HashSet<>(offerRepository.findAll(specifications));
    }

    @Override
    public Set<OfferEntity> filter(Specification<OfferEntity> specifications, int offset, int size) {
        return new HashSet<>(offerRepository.findAll(specifications, PageRequest.of(offset / size, size)).getContent());
    }

    @Transactional
    public void addOfferPatterns(Set<PatternEntity> offerPatterns, OfferEntity offer) {
        var offerEntity = offerRepository.getById(offer.getId());
        offerEntity.getPatterns().addAll(offerPatterns);
        save(offerEntity);
    }
}
