package com.example.application.predicate;

import com.example.application.persistence.entity.CustomerEntity;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class CustomerPredicate {
    private static final List<Predicate<CustomerEntity>> predicates = new ArrayList<>();

    public static void withName(String name) {
        predicates.add(customerEntity -> customerEntity.getName().startsWith(name));
    }

    public static void withEmail(String email) {
        predicates.add(customerEntity -> customerEntity.getEmail().startsWith(email));
    }

    public static Set<CustomerEntity> filterCustomers(Set<CustomerEntity> customerEntities) {
        Set<CustomerEntity> filteredCustomers = customerEntities.stream()
                .filter(predicates.stream().reduce(x -> true, Predicate::and))
                .collect(Collectors.<CustomerEntity>toSet());
        predicates.clear();
        return filteredCustomers;
    }
}
