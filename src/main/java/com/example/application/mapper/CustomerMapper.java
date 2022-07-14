package com.example.application.mapper;

import com.example.application.dto.CustomerDTO;
import com.example.application.persistence.entity.CustomerEntity;
import com.example.application.persistence.entity.UserEntity;
import org.springframework.security.core.userdetails.User;

import java.util.Collections;
import java.util.stream.Collectors;

public class CustomerMapper {

    public static CustomerDTO convertToDTO(CustomerEntity source) {
        final CustomerDTO customerDTO = new CustomerDTO();
        customerDTO.setId(source.getId());
        customerDTO.setName(source.getName());
        customerDTO.setEmail(source.getEmail());
        customerDTO.setPhone(source.getPhone());
        customerDTO.setOrders(source.getOrders().stream().map(OrderMapper::convertToDTO)
                .collect(Collectors.toList()));
        return customerDTO;
    }

    public static User convertToUser(UserEntity source) {
        return new User(
                source.getUsername(),
                source.getPassword(),
                Collections.emptyList());
    }

    public static CustomerEntity convertToCustomerEntity(CustomerDTO customerDTO) {
        final CustomerEntity customerEntity = new CustomerEntity();
        customerEntity.setName(customerDTO.getName());
        customerEntity.setEmail(customerDTO.getEmail());
        customerEntity.setPhone(customerDTO.getPhone());
        return customerEntity;
    }

}
