package com.example.application.service;

import com.example.application.mapper.PatternMapper;
import com.example.application.persistence.entity.PatternEntity;
import com.example.application.persistence.repository.PatternRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
public class PatternService implements FilterableCrudService<PatternEntity> {
    private final PatternRepository patternRepository;
    private final FirmService firmService;

    public PatternService(PatternRepository patternRepository, FirmService firmService) {
        this.patternRepository = patternRepository;
        this.firmService = firmService;
    }

    @Override
    public JpaRepository<PatternEntity, Long> getRepository() {
        return patternRepository;
    }

    @Override
    @Transactional
    @Async
    public PatternEntity save(PatternEntity entity) {
        if (PatternEntity.isDefaultPattern(entity)) {
            var pattern = patternRepository.findPatternById(entity.getId());
            if (!pattern.equals(entity)) {
                var firm = firmService.getFirmWithDefaultPatterns();
                firm.removeDefaultPattern(pattern);
                entity = PatternMapper.convertToEntity(entity, firm);
                entity.getFirmEntity().addPattern(entity);
                if (!pattern.getOfferPatterns().isEmpty()) {
                    var savedPattern = FilterableCrudService.super.save(entity);
                    pattern.getOfferPatterns().forEach(it -> it.getId().setPatternId(savedPattern.getId()));
                }
            }
        }
        return FilterableCrudService.super.save(entity);
    }

    public List<PatternEntity> saveAll(Collection<PatternEntity> patterns) {
        return patternRepository.saveAll(patterns);
    }

    @Override
    @Transactional
    public void delete(PatternEntity entity) {
        var pattern = patternRepository.findFullPatternById(entity.getId());
        if (PatternEntity.isDefaultPattern(pattern)) {
            var firm = firmService.getFirmWithDefaultPatterns();
            firm.removeDefaultPattern(pattern);
            firmService.save(firm);
        } else {
            var firm = firmService.getFirmWithCustomPatterns();
            firm.removePattern(pattern);
            FilterableCrudService.super.delete(pattern);
        }
    }

    @Override
    public long countAnyMatching(Specification<PatternEntity> specification) {
        return patternRepository.count(specification);
    }

    @Override
    public Set<PatternEntity> filter(Specification<PatternEntity> specifications, int offset, int size) {
        return new LinkedHashSet<>(patternRepository.findAll(specifications, PageRequest.of(offset / size, size)).getContent());
    }

    @Transactional(readOnly = true)
    public Set<PatternEntity> filter(Specification<PatternEntity> specifications) {
        return new LinkedHashSet<>(patternRepository.findAll(specifications));
    }

    @Transactional(readOnly = true)
    public PatternEntity getPattern(PatternEntity patternEntity) {
        return patternRepository.findByName(patternEntity.getName());
    }
}
