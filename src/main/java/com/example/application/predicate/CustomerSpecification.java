package com.example.application.predicate;

import com.example.application.persistence.entity.CustomerEntity;
import com.example.application.persistence.entity.FirmEntity;
import com.example.application.persistence.entity.PatternEntity;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;

public class CustomerSpecification implements Specification<CustomerEntity> {
    private Long   firmId;
    private String email;
    private String name;

    public CustomerSpecification(Long firmId) {
        this.firmId = firmId;
    }

    public CustomerSpecification(Long firmId, String email, String name) {
        this.firmId = firmId;
        this.email = email;
        this.name = name;
    }

    @Override
    public Predicate toPredicate(Root<CustomerEntity> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
        final ArrayList<Predicate> predicates = new ArrayList<>();
        if (email != null && !email.isBlank()) {
            predicates.add(criteriaBuilder.like(root.get("email"), "%" + email + "%"));
        }
        if (name != null && !name.isBlank()) {
            predicates.add(criteriaBuilder.like(root.get("name"), "%" + name + "%"));

        }
        Predicate firmEntities = criteriaBuilder.equal(criteriaBuilder.treat(root.get("firmEntity"), FirmEntity.class).get("id"), firmId);
        Predicate defaultEntities = criteriaBuilder.treat(root.get("firmEntity"), FirmEntity.class).get("id").isNull();
        Predicate patterns = criteriaBuilder.or(firmEntities, defaultEntities);
        predicates.add(patterns);
        criteriaQuery.orderBy(criteriaBuilder.desc(criteriaBuilder.treat(root.get("firmEntity"), FirmEntity.class).get("id")));

        return criteriaBuilder.and(predicates.toArray(Predicate[]::new));
    }
}