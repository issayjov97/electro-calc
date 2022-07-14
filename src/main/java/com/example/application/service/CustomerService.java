package com.example.application.service;

import com.example.application.dto.CustomerDTO;
import com.example.application.dto.OrderDTO;
import com.example.application.mapper.CustomerMapper;
import com.example.application.mapper.OrderMapper;
import com.example.application.persistence.entity.CustomerEntity;
import com.example.application.persistence.repository.CustomerRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class CustomerService {

    private final CustomerRepository customerRepository;
    private final UserService        userService;

    public CustomerService(CustomerRepository customerRepository, UserService userService) {
        this.customerRepository = customerRepository;
        this.userService = userService;
    }

    public Set<CustomerDTO> findUserOrders() {
        return customerRepository.findAll().stream().map(CustomerMapper::convertToDTO)
                .collect(Collectors.toSet());
    }

    @Transactional(readOnly = true)
    public Set<CustomerDTO> findUserCustomers() {
        return userService.getByUsername(AuthService.getUsername())
                .getCustomerEntities()
                .stream().map(CustomerMapper::convertToDTO)
                .collect(Collectors.toSet());
    }

    public CustomerDTO findById(long customerId) {
        final CustomerEntity customerEntity = customerRepository.findById(customerId)
                .orElseThrow(() -> new RuntimeException("Customer not found"));
        return CustomerMapper.convertToDTO(customerEntity);
    }
    public List<OrderDTO> getCustomerOrders(long customerId) {
        final CustomerEntity customerEntity = customerRepository.findById(customerId)
                .orElseThrow(() -> new RuntimeException("Customer not found"));
        return customerEntity.getOrders().stream().map(OrderMapper::convertToDTO).collect(Collectors.toList());
    }


    public void save(CustomerDTO customerDTO) {
        var userEntity = userService.getByUsername(AuthService.getUsername());
        final CustomerEntity customerEntity = customerRepository.findById(customerDTO.getId())
                .orElse(new CustomerEntity());
        customerEntity.setName(customerDTO.getName());
        customerEntity.setEmail(customerDTO.getEmail());
        customerEntity.setPhone(customerDTO.getPhone());
        customerEntity.setUserEntity(userEntity);
        customerRepository.saveAndFlush(customerEntity);
    }

    public void delete(CustomerDTO customerDTO) {
        final CustomerEntity customerEntity = customerRepository.findById(customerDTO.getId())
                .orElseThrow(() -> new RuntimeException("Customer not found"));
        customerRepository.delete(customerEntity);
    }

    public Set<CustomerDTO> filter(String name, String email) {
        Set<CustomerDTO> result = findUserCustomers();
        if (name != null && !name.isBlank()) {
            CustomerPredicate.withName(name);
        }
        if (email != null && !email.isBlank()) {
            CustomerPredicate.withEmail(email);
        }
        return CustomerPredicate.filterCustomers(result);
    }
}
