package com.example.application.service;

import com.example.application.dto.CustomerDTO;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class CustomerPredicate {
    private static final List<Predicate<CustomerDTO>> predicates = new ArrayList<>();

    public static void withName(String name) {
        predicates.add(customerEntity -> customerEntity.getName().startsWith(name));
    }

    public static void withEmail(String email) {
        predicates.add(customerEntity -> customerEntity.getEmail().startsWith(email));
    }

    public static Set<CustomerDTO> filterCustomers(Set<CustomerDTO> employees) {
        var filteredCustomers = employees.stream()
                .filter(predicates.stream().reduce(x -> true, Predicate::and))
                .collect(Collectors.<CustomerDTO>toSet());
        predicates.clear();
        return filteredCustomers;
    }
}
