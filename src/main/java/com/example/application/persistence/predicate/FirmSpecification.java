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

public class FirmSpecification implements Specification<FirmEntity> {
    private Long   firmId;
    private String PLU;
    private String name;

    public FirmSpecification() {

    }

    public FirmSpecification(Long firmId) {
        this.firmId = firmId;
    }

    public FirmSpecification(Long firmId, String PLU, String name) {
        this.firmId = firmId;
        this.PLU = PLU;
        this.name = name;
    }

    @Override
    public Predicate toPredicate(Root<FirmEntity> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
        final ArrayList<Predicate> predicates = new ArrayList<>();

        predicates.add(criteriaBuilder.equal(root.get("id"),firmId));
        Join<FirmEntity, PatternEntity> defaultPatternsJoin = root.join("defaultPatterns", JoinType.INNER);
        var nameDPredicate = criteriaBuilder.like(defaultPatternsJoin.get("name"), "%" + name + "%");
        predicates.add(criteriaBuilder.and(nameDPredicate));

        Join<FirmEntity, PatternEntity> firmPatternsJoin = root.join("patterns", JoinType.INNER);
        var namePredicate = criteriaBuilder.like(firmPatternsJoin.get("name"), "%" + name + "%");
        predicates.add(criteriaBuilder.and(namePredicate));
        return criteriaBuilder.or(predicates.toArray(Predicate[]::new));
    }
}