package com.example.application.service;

import com.example.application.persistence.entity.AbstractEntity;
import org.springframework.data.jpa.domain.Specification;

import java.util.Set;

public interface FilterableCrudService<T extends AbstractEntity> extends CrudService<T> {

    long countAnyMatching(Specification<T> specification);

    Set<T> filter(Specification<T> specifications);

    Set<T> filter(Specification<T> specifications, int offset, int size);
}
