package com.example.application.model.enums;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public enum OrderStatus {
    NONE(""),
    IN_PROGRESS("Rozpracovaná"),
    CANCELED("Stornovaná"),
    SENT("Odeslaná"),
    DONE("Uzavřená"),
    EDITION("Korekce");


    private final String value;

    OrderStatus(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public static List<String> getStatuses() {
        return Arrays.stream(OrderStatus.values()).map(OrderStatus::getValue).collect(Collectors.toList());
    }

    @Override
    public String toString() {
        return value;
    }
}
