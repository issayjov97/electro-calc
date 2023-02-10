package com.example.application.service;

import com.example.application.persistence.entity.CustomerEntity;
import com.example.application.persistence.repository.CustomerRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

@Service
public class CustomerService implements FilterableCrudService<CustomerEntity> {
    private final UserService userService;
    private final CustomerRepository customerRepository;

    public CustomerService(CustomerRepository customerRepository, UserService userService) {
        this.customerRepository = customerRepository;
        this.userService = userService;
    }

    @Transactional(readOnly = true)
    public Set<CustomerEntity> findFirmCustomers() {
        var firm = userService.getUserFirm(AuthService.getUsername());
        return customerRepository.findCustomersByFirmEntityIdOrderByName(firm.getId());
    }

    @Override
    public JpaRepository<CustomerEntity, Long> getRepository() {
        return customerRepository;
    }

    @Transactional
    @Override
    public CustomerEntity save(CustomerEntity customerEntity) {
        var firmEntity = userService.getUserFirm(AuthService.getUsername());
        customerEntity.setFirmEntity(firmEntity);
        return FilterableCrudService.super.save(customerEntity);
    }

    @Override
    public void deleteById(long id) {
        customerRepository.deleteById(id);
    }

    @Override
    public long count() {
        return FilterableCrudService.super.count();
    }

    @Override
    public CustomerEntity load(long id) {
        return FilterableCrudService.super.load(id);
    }

    @Override
    public List<CustomerEntity> loadAll() {
        return FilterableCrudService.super.loadAll();
    }

    @Transactional(readOnly = true)
    public Set<CustomerEntity> getFirmCustomers() {
        var firm = userService.getUserFirm(AuthService.getUsername());
        return customerRepository.findCustomersByFirmEntityIdOrderByName(firm.getId());
    }

    @Transactional(readOnly = true)
    @Override
    public Set<CustomerEntity> filter(Specification<CustomerEntity> specifications) {
        return new LinkedHashSet<>(customerRepository.findAll(specifications));
    }

    @Transactional(readOnly = true)
    @Override
    public Set<CustomerEntity> filter(Specification<CustomerEntity> specifications, int offset, int size) {
        return new LinkedHashSet<>(customerRepository.findAll(specifications, PageRequest.of(offset / size, size)).getContent());
    }

    @Transactional(readOnly = true)
    @Override
    public long countAnyMatching(Specification<CustomerEntity> specification) {
        return customerRepository.count(specification);
    }
}
