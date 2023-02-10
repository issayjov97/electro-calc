package com.example.application.service;

import com.example.application.persistence.entity.OfferEntity;
import com.example.application.persistence.entity.OfferPattern;
import com.example.application.persistence.entity.VATEntity;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Service
public class FinancialService {
    private final FirmSettingsService firmSettingsService;
    public static final BigDecimal ONE_HUNDRED = BigDecimal.valueOf(100);

    public FinancialService(FirmSettingsService firmSettingsService) {
        this.firmSettingsService = firmSettingsService;
    }

    public BigDecimal calculatePriceWithVat(VATEntity source) {
        final BigDecimal priceWithoutVat = source.getPriceWithoutVAT();
        final double rate = firmSettingsService.getFirmSettings().getDph() / 100.0;
        final BigDecimal VAT = priceWithoutVat.multiply(BigDecimal.valueOf(rate)).setScale(2, RoundingMode.HALF_UP);
        return priceWithoutVat.add(VAT);
    }

    public BigDecimal summaryMaterialCost(OfferEntity source) {
        return source.getOfferPatterns().stream()
                .map(this::materialCost)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public BigDecimal materialCost(OfferPattern offerPattern) {
        var firmSettings = firmSettingsService.getFirmSettings();
        var cost = offerPattern.getPatternEntity().getPriceWithoutVAT().multiply(BigDecimal.valueOf(offerPattern.getCount()));
        if (offerPattern.getPatternEntity().getName().contains("kabel")) {
            cost = cost.add(cost.multiply(BigDecimal.valueOf(firmSettings.getIncision())).divide(ONE_HUNDRED, 2, RoundingMode.HALF_UP));
        }
        return cost;
    }

    public BigDecimal summaryWorkCost(OfferEntity source) {
        var firmSettings = firmSettingsService.getFirmSettings();
        return BigDecimal.valueOf(source.getWorkDuration() / 60.0 * firmSettings.getChargePerHour());
    }

    public BigDecimal workCost(OfferPattern source) {
        var firmSettings = firmSettingsService.getFirmSettings();
        return BigDecimal.valueOf(source.getPatternEntity().getDuration() / 60.0 * source.getCount() * firmSettings.getChargePerHour());
    }

    public BigDecimal transportationCost(OfferEntity source) {
        var firmSettings = firmSettingsService.getFirmSettings();
        var tmp = (int) Math.ceil((source.getWorkDuration() / 60.0 / firmSettings.getWorkingHours()));
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

    public double summaryWorkDuration(OfferEntity source) {
        return source.getOfferPatterns().stream().mapToDouble(this::workDuration).sum();
    }

    public double workDuration(OfferPattern offerPattern) {
        return offerPattern.getPatternEntity().getDuration() * offerPattern.getCount();
    }
}

