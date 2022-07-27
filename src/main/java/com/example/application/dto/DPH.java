package com.example.application.dto;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public enum DPH {
    BASIC_RATE(21),
    FIRST_REDUCED_RATE(15),
    SECOND_REDUCED_RATE(10);

    private final int value;

    DPH(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    public static List<Integer> getRates() {
        return Arrays.stream(DPH.values()).map(DPH::getValue).collect(Collectors.toList());
    }
}
