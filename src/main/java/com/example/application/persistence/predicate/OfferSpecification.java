package com.example.application.persistence.predicate;

import com.example.application.model.enums.OrderStatus;
import com.example.application.persistence.entity.FirmEntity;
import com.example.application.persistence.entity.OfferEntity;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;

public class OfferSpecification implements Specification<OfferEntity> {
    private Long firmId;
    private String name;
    private OrderStatus status;

    public OfferSpecification(Long firmId, String name, OrderStatus status) {
        this.firmId = firmId;
        this.name = name;
        this.status = status;
    }

    @Override
    public Predicate toPredicate(Root<OfferEntity> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
        final ArrayList<Predicate> predicates = new ArrayList<>();
        if (name != null && !name.isBlank()) {
            predicates.add(criteriaBuilder.like(root.get("name"), "%" + name + "%"));
        }
        if (status != null && status != OrderStatus.NONE) {
            predicates.add(criteriaBuilder.equal(root.get("status"), status));
        }
        predicates.add(criteriaBuilder.equal(criteriaBuilder.treat(root.get("firmEntity"), FirmEntity.class).get("id"), firmId));
        criteriaQuery.orderBy(criteriaBuilder.desc(root.get("createdDate")));

        return criteriaBuilder.and(predicates.toArray(Predicate[]::new));
    }
}