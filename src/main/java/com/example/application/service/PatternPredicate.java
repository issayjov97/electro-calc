package com.example.application.service;

import com.example.application.dto.PatternDTO;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class PatternPredicate {
    private static final double                      EPSILON    = 0.000001d;
    private static final List<Predicate<PatternDTO>> predicates = new ArrayList<>();

    public static void withName(String name) {
        predicates.add(patternEntity -> patternEntity.getName().startsWith(name));
    }

    public static void withDuration(Double duration) {
        predicates.add(patternEntity -> Math.abs(patternEntity.getDuration() - duration) < EPSILON);
    }

    public static void withPrice(Double price) {
        predicates.add(patternEntity -> patternEntity.getPriceWithoutVat().compareTo(new BigDecimal(price)) == 0);
    }

    public static Set<PatternDTO> filterPatterns(Set<PatternDTO> employees) {
        var filteredPatterns = employees.stream()
                .filter(predicates.stream().reduce(x -> true, Predicate::and))
                .collect(Collectors.<PatternDTO>toSet());
        predicates.clear();
        return filteredPatterns;
    }
}
