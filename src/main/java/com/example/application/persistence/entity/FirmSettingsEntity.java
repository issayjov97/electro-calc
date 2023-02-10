package com.example.application.persistence.entity;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToOne;
import javax.persistence.Table;


@Entity
@Table(name = "firmSettings")
public class FirmSettingsEntity extends AbstractEntity {

    private double costPerKm = 0;
    private double dph = 21.0;
    private double chargePerHour = 0;
    private double sale = 0;
    private double workingHours = 1;
    private int incision = 0;

    @OneToOne(fetch = FetchType.LAZY)
    private FirmEntity firmEntity;


    public FirmEntity getFirmEntity() {
        return firmEntity;
    }

    public void setFirmEntity(FirmEntity firmEntity) {
        this.firmEntity = firmEntity;
    }

    public double getCostPerKm() {
        return costPerKm;
    }

    public void setCostPerKm(double costPerKm) {
        this.costPerKm = costPerKm;
    }

    public double getDph() {
        return dph;
    }

    public void setDph(double dph) {
        this.dph = dph;
    }

    public double getChargePerHour() {
        return chargePerHour;
    }

    public void setChargePerHour(double chargePerHour) {
        this.chargePerHour = chargePerHour;
    }

    public double getSale() {
        return sale;
    }

    public void setSale(double sale) {
        this.sale = sale;
    }

    public int getIncision() {
        return incision;
    }

    public void setIncision(int incision) {
        this.incision = incision;
    }

    public double getWorkingHours() {
        return workingHours;
    }

    public void setWorkingHours(double workingHours) {
        this.workingHours = workingHours;
    }
}