package com.example.application.service;

import com.example.application.persistence.entity.CustomerEntity;
import com.example.application.persistence.entity.JobOrderEntity;
import com.example.application.persistence.repository.CustomerRepository;
import com.example.application.predicate.CustomerPredicate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;

@Service
public class CustomerService implements CrudService<CustomerEntity> {
    private final UserService        userService;
    private final CustomerRepository customerRepository;

    public CustomerService(CustomerRepository customerRepository, UserService userService) {
        this.customerRepository = customerRepository;
        this.userService = userService;
    }

    @Transactional(readOnly = true)
    public Set<CustomerEntity> findFirmCustomers() {
        return userService.getUserFirm().getCustomerEntities();
    }

    @Override
    public JpaRepository<CustomerEntity, Long> getRepository() {
        return customerRepository;
    }

    @Transactional
    @Override
    public CustomerEntity save(CustomerEntity customerEntity) {
        var firmEntity = userService.getUserFirm();
        customerEntity.setFirmEntity(firmEntity);
        return CrudService.super.save(customerEntity);
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
    public CustomerEntity load(long id) {
        return CrudService.super.load(id);
    }

    @Override
    public List<CustomerEntity> loadAll() {
        return CrudService.super.loadAll();
    }

    @Transactional(readOnly = true)
    public Set<CustomerEntity> getFirmCustomers() {
        return userService.getUserFirm().getCustomerEntities();
    }

    public Set<CustomerEntity> filter(String name, String email) {
        Set<CustomerEntity> result = findFirmCustomers();
        if (name != null && !name.isBlank()) {
            CustomerPredicate.withName(name);
        }
        if (email != null && !email.isBlank()) {
            CustomerPredicate.withEmail(email);
        }
        return CustomerPredicate.filterCustomers(result);
    }
}
