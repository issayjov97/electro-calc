package com.example.application.service;

import com.example.application.persistence.entity.OfferEntity;
import com.example.application.persistence.entity.OfferPattern;
import com.example.application.persistence.entity.PatternEntity;
import com.example.application.persistence.repository.OfferPatternRepository;
import com.example.application.persistence.repository.OfferRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class OfferService implements FilterableCrudService<OfferEntity> {
    private final OfferRepository offerRepository;
    private final OfferPatternRepository offerPatternRepository;
    private final UserService userService;
    private final FinancialService financialService;

    public OfferService(
            OfferRepository offerRepository,
            OfferPatternRepository offerPatternRepository,
            UserService userService,
            FinancialService financialService) {
        this.offerRepository = offerRepository;
        this.offerPatternRepository = offerPatternRepository;
        this.userService = userService;
        this.financialService = financialService;
    }

    @Transactional
    @Override
    public OfferEntity save(OfferEntity offerEntity) {
        if (offerEntity.getId() == null) {
            offerEntity.setFirmEntity(userService.getUserFirm(AuthService.getUsername()));
        }
        var offer = FilterableCrudService.super.save(offerEntity);
        calculateOfferSummary(offer);
        return offer;
    }

    @Override
    public void delete(OfferEntity entity) {
        var offer = offerRepository.findFullOfferById(entity.getId());
        offer.getOfferPatterns().clear();
        offer.setCustomerEntity(null);
        offer.setFirmEntity(null);
        FilterableCrudService.super.delete(offer);
    }

    @Override
    public JpaRepository<OfferEntity, Long> getRepository() {
        return offerRepository;
    }

    @Override
    public long countAnyMatching(Specification<OfferEntity> specification) {
        return offerRepository.count(specification);
    }

    @Override
    public Set<OfferEntity> filter(Specification<OfferEntity> specifications) {
        return new LinkedHashSet<>(offerRepository.findAll(specifications)).stream().peek(this::calculateOfferSummary)
                .collect(Collectors.toSet());
    }

    @Override
    public Set<OfferEntity> filter(Specification<OfferEntity> specifications, int offset, int size) {
        return new LinkedHashSet<>(offerRepository.findAll(specifications, PageRequest.of(offset / size, size)).getContent())
                .stream().peek(this::calculateOfferSummary)
                .collect(Collectors.toSet());
    }

    @Transactional
    public void addOfferPattern(PatternEntity offerPattern, OfferEntity offer, int count) {
        var offerEntity = offerRepository.getById(offer.getId());
        offerEntity.addPatterns(offerPattern, count);
        save(offerEntity);
    }

    public OfferEntity getOfferWithBriefPatterns(Long id) {
        var offerEntity = offerRepository.getById(id);
        calculateOfferSummary(offerEntity);
        return offerEntity;
    }


    public OfferEntity getOfferWithFullPatterns(Long id) {
        var offerEntity = offerRepository.findFullOfferById(id);
        calculateOfferSummary(offerEntity);
        calculateOfferDetails(offerEntity);
        return offerEntity;
    }

    public Set<OfferEntity> getCustomerOffers(Long id) {
        return offerRepository.getCustomerOffers(id);
    }

    public void updateOfferPatternsCount(OfferPattern offerPattern) {
        offerPatternRepository.save(offerPattern);
    }


    @Transactional
    public OfferEntity deleteOfferPattern(OfferPattern offerPatternEntity) {
        var offerPattern = offerPatternRepository.getOne(offerPatternEntity.getId());
        var offerEntity = offerPatternEntity.getOfferEntity();
        offerEntity.removePattern(offerPattern.getPatternEntity());
        return save(offerEntity);
    }

    public void calculateOfferSummary(OfferEntity offer) {
        offer.setMaterialsCost(financialService.summaryMaterialCost(offer));
        offer.setWorkDuration(Double.parseDouble(String.format("%.2f", financialService.summaryWorkDuration(offer))));
        offer.setWorkCost(financialService.summaryWorkCost(offer));
        offer.setTransportationCost(financialService.transportationCost(offer));
        offer.setPriceWithoutVAT(financialService.offerPriceWithoutVAT(offer));
        offer.setPriceWithVAT(financialService.calculatePriceWithVat(offer));
        offer.setTotalPriceWithoutVAT(financialService.offerTotalPriceWithoutDPH(offer));
        offer.setTotalPriceWithVAT(financialService.offerTotalPriceWithDPH(offer));
    }

    public void calculateOfferDetails(OfferEntity offer) {
        offer.getOfferPatterns().forEach(it -> {
            it.setMaterialsCost(financialService.materialCost(it));
            it.setWorkCost(financialService.workCost(it));
        });
    }
}