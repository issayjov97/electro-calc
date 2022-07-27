package com.example.application.ui.views.patern;

import com.example.application.persistence.entity.PatternEntity;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.concurrent.atomic.AtomicReference;

import static org.junit.jupiter.api.Assertions.assertEquals;


class PatternFormTest {

    @Test
    public void saveEventHasCorrectValues() {
        final String name = "test_pattern";
        final String description = "Test pattern description";
        final Double duration = 12.0;
        final BigDecimal priceWithoutVAT = new BigDecimal(12);

        PatternForm patternForm = new PatternForm();
        PatternEntity pattern = new PatternEntity();
        patternForm.setEntity(pattern);
        patternForm.getNameField().setValue(name);
        patternForm.getDescriptionField().setValue(description);
        patternForm.getDurationField().setValue(duration);
        patternForm.getPriceWithoutVatField().setValue(priceWithoutVAT);

        AtomicReference<PatternEntity> savedContactRef = new AtomicReference<>(null);
        patternForm.addListener(PatternForm.SaveEvent.class, e -> {
            savedContactRef.set(e.getItem());
        });
        patternForm.getSaveButton().click();

        PatternEntity savedPattern = savedContactRef.get();

        assertEquals(name, savedPattern.getName());
        assertEquals(description, savedPattern.getDescription());
        assertEquals(duration, savedPattern.getDuration());
        assertEquals(priceWithoutVAT, savedPattern.getPriceWithoutVAT());
    }

}