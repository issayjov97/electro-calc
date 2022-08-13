package com.example.application.service;

import com.example.application.mapper.PatternMapper;
import com.example.application.persistence.entity.FirmEntity;
import com.example.application.persistence.entity.PatternEntity;
import com.example.application.persistence.repository.PatternRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class PatternService implements FilterableCrudService<PatternEntity> {
    private final PatternRepository patternRepository;
    private final UserService       userService;

    public PatternService(PatternRepository patternRepository, UserService userService, UserService userService1) {
        this.patternRepository = patternRepository;
        this.userService = userService1;
    }

    @Override
    public JpaRepository<PatternEntity, Long> getRepository() {
        return patternRepository;
    }

    @Override
    public PatternEntity save(PatternEntity entity) {
        final var firm = userService.getUserFirm();
        if (PatternEntity.isDefaultPattern(entity)) {
            entity = PatternMapper.convertToEntity(entity, firm);
        }
        return FilterableCrudService.super.save(entity);
    }

    @Override
    @Transactional
    public void delete(PatternEntity entity) {
        var pattern = patternRepository.findFullPatternById(entity.getId());
        pattern.getDemands().forEach(it -> it.removePattern(pattern));
        pattern.getOrders().forEach(it -> it.removePattern(pattern));
        pattern.getOffers().forEach(it -> it.removePattern(pattern));
        pattern.getFirmEntity().removePattern(pattern);
        FilterableCrudService.super.delete(pattern);
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
    public PatternEntity load(long id) {
        return FilterableCrudService.super.load(id);
    }

    @Override
    public List<PatternEntity> loadAll() {
        return FilterableCrudService.super.loadAll();
    }

    @Transactional(readOnly = true)
    public int patternsCount(Specification<PatternEntity> specifications) {
        return new HashSet<>(patternRepository.findAll(specifications)).size();
    }

    @Transactional(readOnly = true)
    public int firmPatternsCount(FirmEntity firmEntity) {
        return patternRepository.count(firmEntity.getId());
    }

    @Transactional(readOnly = true)
    public Set<PatternEntity> getFirmDefaultPatterns() {
        var firm = userService.getUserFirm();
        return patternRepository.findByDefaultPatternsFirmEntityId(firm.getId());
    }

    @Transactional(readOnly = true)
    public Set<PatternEntity> getFirmPatterns() {
        var firm = userService.getUserFirm();
        return patternRepository.findFirmPatterns(firm.getId());
    }

    @Override
    public long countAnyMatching(Specification<PatternEntity> specification) {
        return patternRepository.count(specification);
    }

    @Override
    public Set<PatternEntity> filter(Specification<PatternEntity> specifications, int offset, int size) {
        return new HashSet<>(patternRepository.findAll(specifications, PageRequest.of(offset / size, size)).getContent());
    }

    @Transactional(readOnly = true)
    public Set<PatternEntity> filter(Specification<PatternEntity> specifications) {
        return new HashSet<>(patternRepository.findAll(specifications));
    }


    @Transactional(readOnly = true)
    public PatternEntity getPattern(String name) {
        return patternRepository.findByName(name);
    }

    @Transactional(readOnly = true)
    public PatternEntity getPatternWithOffers(Long id) {
        return patternRepository.findPatternWithOffersById(id);
    }

}
