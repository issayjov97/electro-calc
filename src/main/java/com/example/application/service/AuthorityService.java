package com.example.application.service;

import com.example.application.persistence.entity.AuthorityEntity;
import com.example.application.persistence.repository.AuthorityRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AuthorityService implements CrudService<AuthorityEntity> {
    private final AuthorityRepository authorityRepository;

    public AuthorityService(
            AuthorityRepository authorityRepository
    ) {
        this.authorityRepository = authorityRepository;
    }

    @Override
    public JpaRepository<AuthorityEntity, Long> getRepository() {
        return authorityRepository;
    }

    @Override
    public AuthorityEntity save(AuthorityEntity entity) {
        return CrudService.super.save(entity);
    }

    @Override
    public void delete(AuthorityEntity entity) {
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
    public AuthorityEntity load(long id) {
        return CrudService.super.load(id);
    }

    @Override
    public List<AuthorityEntity> loadAll() {
        return CrudService.super.loadAll();
    }
}
