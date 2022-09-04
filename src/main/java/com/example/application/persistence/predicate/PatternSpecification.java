package com.example.application.persistence.predicate;

import com.example.application.persistence.entity.FirmEntity;
import com.example.application.persistence.entity.PatternEntity;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;

public class PatternSpecification implements Specification<PatternEntity> {
    private Long   firmId;
    private String name;

    public PatternSpecification() {
    }

    public PatternSpecification(Long firmId) {
        this.firmId = firmId;
    }

    public PatternSpecification(Long firmId, String name) {
        this.firmId = firmId;
        this.name = name;
    }

    @Override
    public Predicate toPredicate(Root<PatternEntity> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
        final ArrayList<Predicate> predicates = new ArrayList<>();
        if (name != null && !name.isBlank()) {
            predicates.add(criteriaBuilder.like(root.get("name"), "%" + name + "%"));
        }
        Predicate firmEntities = criteriaBuilder.equal(criteriaBuilder.treat(root.get("firmEntity"), FirmEntity.class).get("id"), firmId);
        Join<PatternEntity, FirmEntity> defaultEntities = root.join("firmEntities", JoinType.LEFT);
        Predicate defaultPatternFirmPredicate = criteriaBuilder.equal(defaultEntities.get("id"), firmId);
        predicates.add(criteriaBuilder.or(firmEntities, defaultPatternFirmPredicate));

        criteriaQuery.orderBy(criteriaBuilder.desc(criteriaBuilder.treat(root.get("firmEntity"), FirmEntity.class).get("id")));
        return criteriaBuilder.and(predicates.toArray(Predicate[]::new));
    }
}