package com.example.application.service;

import com.example.application.persistence.entity.AbstractEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import javax.persistence.EntityNotFoundException;
import java.util.List;

public interface CrudService<T extends AbstractEntity> {

    JpaRepository<T, Long> getRepository();

    default T save(T entity) {
        return getRepository().saveAndFlush(entity);
    }

    default void delete(T entity) {
        if (entity == null) {
            throw new EntityNotFoundException();
        }
        getRepository().delete(entity);
    }

    default void deleteById(long id) {
        delete(load(id));
    }

    default long count() {
        return getRepository().count();
    }

    default T load(long id) {
        return getRepository().findById(id).orElseThrow(() -> new EntityNotFoundException("Firma " + id + " neexistuje"));
    }

    default List<T> loadAll() {
        return getRepository().findAll();
    }
}
