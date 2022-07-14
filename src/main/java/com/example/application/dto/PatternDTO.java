package com.example.application.dto;

import java.math.BigDecimal;

public class PatternDTO {

    private int    id;
    private String name;
    private String     description;
    private double     duration;
    private BigDecimal priceWithoutVat;

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public double getDuration() {
        return duration;
    }

    public BigDecimal getPriceWithoutVat() {
        return priceWithoutVat;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setDuration(double duration) {
        this.duration = duration;
    }

    public void setPriceWithoutVat(BigDecimal priceWithoutVat) {
        this.priceWithoutVat = priceWithoutVat;
    }
}
