package com.example.application.service;

import com.example.application.persistence.entity.AbstractServiceEntity;
import com.example.application.persistence.entity.PatternEntity;
import com.example.application.persistence.entity.VATEntity;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class FinancialService {

    public static BigDecimal calculatePriceWithVat(VATEntity source) {
        final BigDecimal priceWithoutVat = source.getPriceWithoutVAT();
        final double rate = source.getVAT() / 100.0;
        final BigDecimal VAT = priceWithoutVat.multiply(new BigDecimal(rate)).setScale(2, RoundingMode.HALF_UP);
        return priceWithoutVat.add(VAT);
    }

    public static BigDecimal calculateServicePriceWithoutVAT(AbstractServiceEntity source) {
        return source.getMaterialsCost().add(source.getTransportationCost())
                .add(
                        source.getPatterns().stream().map(PatternEntity::getPriceWithoutVAT)
                                .reduce(BigDecimal.ZERO, BigDecimal::add)
                );
    }
}

