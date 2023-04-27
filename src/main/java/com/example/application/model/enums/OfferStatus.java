package com.example.application.model.enums;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public enum OfferStatus {
    NONE(""),
    IN_PROGRESS("Rozpracovaná"),
    CANCELED("Stornovaná"),
    SENT("Odeslaná"),
    DONE("Uzavřená");

    private final String value;

    OfferStatus(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public static List<String> getStatuses() {
        return Arrays.stream(OfferStatus.values()).map(OfferStatus::getValue).collect(Collectors.toList());
    }

    @Override
    public String toString() {
        return value;
    }
}
