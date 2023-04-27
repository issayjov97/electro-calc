package com.example.application.persistence.predicate;

import com.example.application.model.enums.OfferStatus;
import com.example.application.persistence.entity.CustomerEntity;
import com.example.application.persistence.entity.FirmEntity;
import com.example.application.persistence.entity.OfferEntity;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;

public class OfferSpecification implements Specification<OfferEntity> {
    private final Long firmId;
    private final String name;
    private final OfferStatus status;
    private String customerName;

    public OfferSpecification(Long firmId, String name, OfferStatus status, String customerName) {
        this.firmId = firmId;
        this.name = name;
        this.status = status;
        this.customerName = customerName;
    }

    @Override
    public Predicate toPredicate(Root<OfferEntity> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
        final ArrayList<Predicate> predicates = new ArrayList<>();
        if (name != null && !name.isBlank()) {
            predicates.add(criteriaBuilder.like(root.get("name"), "%" + name + "%"));
        }
        if (customerName != null && !customerName.isBlank()) {
            predicates.add(criteriaBuilder.equal(criteriaBuilder.treat(root.get("customerEntity"), CustomerEntity.class).get("name"), customerName));
        }
        if (status != null && status != OfferStatus.NONE) {
            predicates.add(criteriaBuilder.equal(root.get("status"), status));
        }
        predicates.add(criteriaBuilder.equal(criteriaBuilder.treat(root.get("firmEntity"), FirmEntity.class).get("id"), firmId));
        criteriaQuery.orderBy(criteriaBuilder.desc(root.get("createdDate")));

        return criteriaBuilder.and(predicates.toArray(Predicate[]::new));
    }
}