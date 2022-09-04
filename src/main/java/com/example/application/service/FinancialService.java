package com.example.application.service;

import com.example.application.persistence.entity.OfferEntity;
import com.example.application.persistence.entity.VATEntity;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Service
public class FinancialService {
    private final       FirmSettingsService firmSettingsService;
    public static final BigDecimal          ONE_HUNDRED = new BigDecimal(100);


    public FinancialService(FirmSettingsService firmSettingsService) {
        this.firmSettingsService = firmSettingsService;
    }

    public BigDecimal calculatePriceWithVat(VATEntity source) {
        final BigDecimal priceWithoutVat = source.getPriceWithoutVAT();
        final double rate = firmSettingsService.getFirmSettings().getDph() / 100.0;
        final BigDecimal VAT = priceWithoutVat.multiply(new BigDecimal(rate)).setScale(2, RoundingMode.HALF_UP);
        return priceWithoutVat.add(VAT);
    }

    public BigDecimal materialCost(OfferEntity source) {
        return source.getMaterialsCost().add(source.getPatterns().stream()
                        .map(it -> it.getPatternEntity().getPriceWithoutVAT().multiply(BigDecimal.valueOf(it.getCount())))
                        .reduce(BigDecimal.ZERO, BigDecimal::add));
    }

    public BigDecimal workCost(OfferEntity source) {
        var firmSettings = firmSettingsService.getFirmSettings();
        return BigDecimal.valueOf(source.getWorkDuration() / 60.0 * firmSettings.getChargePerHour());
    }

    public BigDecimal transportationCost(OfferEntity source) {
        var firmSettings = firmSettingsService.getFirmSettings();
        var tmp = (int) Math.ceil((source.getWorkDuration() / firmSettings.getWorkingHours()));
        return BigDecimal.valueOf(source.getDistance() * tmp * firmSettings.getCostPerKm());
    }

    public BigDecimal offerPriceWithoutVAT(OfferEntity source) {
        return source.getTransportationCost().add(source.getMaterialsCost()).add(source.getWorkCost());
    }

    public BigDecimal offerTotalPriceWithDPH(OfferEntity source) {
        return source.getPriceWithVAT().subtract(offerSaleValueWithDPH(source));
    }

    public BigDecimal offerTotalPriceWithoutDPH(OfferEntity source) {
        return source.getPriceWithoutVAT().subtract(offerSaleValueWithoutDPH(source));
    }

    public BigDecimal offerSaleValueWithDPH(OfferEntity source) {
        var firmSettings = firmSettingsService.getFirmSettings();
        var priceWithVAT = source.getPriceWithVAT();
        return priceWithVAT.multiply(BigDecimal.valueOf(firmSettings.getSale())).divide(ONE_HUNDRED, RoundingMode.HALF_UP);
    }

    public BigDecimal offerSaleValueWithoutDPH(OfferEntity source) {
        var firmSettings = firmSettingsService.getFirmSettings();
        var priceWithoutVAT = source.getPriceWithoutVAT();
        return priceWithoutVAT.multiply(BigDecimal.valueOf(firmSettings.getSale())).divide(ONE_HUNDRED, RoundingMode.HALF_UP);
    }

    public double workDuration(OfferEntity source) {
        return source.getPatterns().stream()
                .mapToDouble(e -> e.getPatternEntity().getDuration() * e.getCount()).sum();
    }
}

