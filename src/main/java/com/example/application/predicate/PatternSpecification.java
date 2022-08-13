package com.example.application.predicate;

import com.example.application.persistence.entity.FirmEntity;
import com.example.application.persistence.entity.PatternEntity;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;

public class PatternSpecification implements Specification<PatternEntity> {
    private Long   firmId;
    private String PLU;
    private String name;

    public PatternSpecification() {

    }

    public PatternSpecification(Long firmId) {
        this.firmId = firmId;
    }

    public PatternSpecification(Long firmId, String PLU, String name) {
        this.firmId = firmId;
        this.PLU = PLU;
        this.name = name;
    }

    @Override
    public Predicate toPredicate(Root<PatternEntity> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
        final ArrayList<Predicate> predicates = new ArrayList<>();
        if (PLU != null && !PLU.isBlank()) {
            predicates.add(criteriaBuilder.like(root.get("PLU"), "%" + PLU + "%"));
        }
        if (name != null && !name.isBlank()) {
            predicates.add(criteriaBuilder.like(root.get("name"), "%" + name + "%"));

        }
        if (firmId != null) {
            Predicate firmEntities = criteriaBuilder.equal(criteriaBuilder.treat(root.get("firmEntity"), FirmEntity.class).get("id"), firmId);
            predicates.add(firmEntities);
        } else {
            Predicate defaultEntities = criteriaBuilder.treat(root.get("firmEntity"), FirmEntity.class).get("id").isNull();
            predicates.add(defaultEntities);
        }
        criteriaQuery.orderBy(criteriaBuilder.desc(criteriaBuilder.treat(root.get("firmEntity"), FirmEntity.class).get("id")));

        return criteriaBuilder.and(predicates.toArray(Predicate[]::new));
    }
}