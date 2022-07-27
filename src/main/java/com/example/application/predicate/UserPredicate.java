package com.example.application.predicate;

import com.example.application.persistence.entity.UserEntity;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class UserPredicate {
    private static final List<Predicate<UserEntity>> predicates = new ArrayList<>();

    public static void withUsername(String username) {
        predicates.add(userEntity-> userEntity.getUsername().startsWith(username));
    }

    public static void withEmail(String email) {
        predicates.add(userEntity -> userEntity.getEmail().startsWith(email));
    }
    public static Set<UserEntity> filterUsers(List<UserEntity> userEntities) {
        var filteredUsers = userEntities.stream()
                .filter(predicates.stream().reduce(x -> true, Predicate::and))
                .collect(Collectors.<UserEntity>toSet());
        predicates.clear();
        return filteredUsers;
    }
}
