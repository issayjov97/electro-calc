package com.example.application.service;

import com.example.application.persistence.entity.FirmEntity;
import com.example.application.persistence.entity.PatternEntity;
import com.example.application.persistence.repository.PatternRepository;
import com.example.application.predicate.PatternPredicate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class PatternService implements CrudService<PatternEntity> {
    private final PatternRepository patternRepository;

    public PatternService(PatternRepository patternRepository, UserService userService) {
        this.patternRepository = patternRepository;
    }

    @Override
    public JpaRepository<PatternEntity, Long> getRepository() {
        return patternRepository;
    }

    @Override
    public PatternEntity save(PatternEntity entity) {
        return CrudService.super.save(entity);
    }

    @Override
    public void delete(PatternEntity entity) {
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
    public PatternEntity load(long id) {
        return CrudService.super.load(id);
    }

    @Override
    public List<PatternEntity> loadAll() {
        return CrudService.super.loadAll();
    }

    @Transactional(readOnly = true)
    public Set<PatternEntity> getAll(FirmEntity firmEntity) {
        Set<PatternEntity> customerPatternEntities = firmEntity.getPatterns();
        Set<PatternEntity> defaultPatternEntities = customerPatternEntities.isEmpty() ?
                getDefaultPatterns() : getFirmDefaultPatterns(customerPatternEntities);

        return Stream.of(customerPatternEntities, defaultPatternEntities).flatMap(Collection::stream)
                .collect(Collectors.toSet());
    }

    @Transactional(readOnly = true)
    public Set<PatternEntity> getDefaultPatterns() {
        return patternRepository.findDefaultPattern();
    }

    @Transactional(readOnly = true)
    public Set<PatternEntity> getFirmDefaultPatterns(Set<PatternEntity> customerPatternEntities) {
        return patternRepository.findDefaultPatternsNotIn(
                customerPatternEntities.stream().map(PatternEntity::getName).collect(Collectors.toList()));
    }

    @Transactional(readOnly = true)
    public PatternEntity getPattern(String name) {
        return patternRepository.findByName(name);
    }

    public Set<PatternEntity> filter(FirmEntity firmEntity, String name, Double price, Double duration) {
        Set<PatternEntity> result = getAll(firmEntity);
        if (name != null && !name.isBlank()) {
            PatternPredicate.withName(name);
        }
        if (price != null) {
            PatternPredicate.withPrice(price);
        }
        if (duration != null) {
            PatternPredicate.withDuration(duration);
        }
        return PatternPredicate.filterPatterns(result);
    }
}
