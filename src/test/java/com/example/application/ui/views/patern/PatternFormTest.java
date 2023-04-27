package com.example.application.ui.views.patern;

import com.example.application.persistence.entity.PatternEntity;
import com.example.application.ui.views.patern.events.DeleteEvent;
import com.example.application.ui.views.patern.events.SaveEvent;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.concurrent.atomic.AtomicReference;

import static org.junit.jupiter.api.Assertions.assertEquals;


class PatternFormTest {

    private final String name = "PATTERN";
    private final String description = "PATTERN_DESCRIPTION";
    private final Double duration = 0.00;
    private final BigDecimal priceWithoutVAT = BigDecimal.valueOf(100);

    @Test
    void formFieldsPopulated() {
        PatternForm patternForm = new PatternForm();
        PatternEntity patternEntity = getPattern();
        patternForm.setEntity(patternEntity);

        assertEquals(name, patternForm.getNameField().getValue());
        assertEquals(description, patternForm.getDescriptionField().getValue());
        assertEquals(duration, patternForm.getDurationField().getValue());
        assertEquals(priceWithoutVAT, patternForm.getPriceWithoutVatField().getValue());
    }

    @Test
    void saveEventHasCorrectValues() {
        PatternForm patternForm = new PatternForm();
        PatternEntity pattern = new PatternEntity();
        patternForm.setEntity(pattern);
        patternForm.getNameField().setValue(name);
        patternForm.getDescriptionField().setValue(description);
        patternForm.getDurationField().setValue(duration);
        patternForm.getPriceWithoutVatField().setValue(priceWithoutVAT);

        AtomicReference<PatternEntity> savedContactRef = new AtomicReference<>(null);
        patternForm.addListener(SaveEvent.class, e -> {
            savedContactRef.set(e.getItem());
        });
        patternForm.getSaveButton().click();

        PatternEntity savedPattern = savedContactRef.get();

        assertEquals(name, savedPattern.getName());
        assertEquals(description, savedPattern.getDescription());
        assertEquals(duration, savedPattern.getDuration());
        assertEquals(priceWithoutVAT, savedPattern.getPriceWithoutVAT());
    }

    @Test
    void deleteEventHasCorrectValues() {
        PatternForm patternForm = new PatternForm();
        PatternEntity pattern = getPattern();
        patternForm.setEntity(pattern);

        AtomicReference<PatternEntity> deleteUserRef = new AtomicReference<>(null);
        patternForm.addListener(DeleteEvent.class, e -> {
            deleteUserRef.set(e.getItem());
        });

        patternForm.getDeleteButton().click();
        PatternEntity deletedPatternEntity = deleteUserRef.get();

        assertEquals(name, deletedPatternEntity.getName());
        assertEquals(description, deletedPatternEntity.getDescription());
        assertEquals(duration, deletedPatternEntity.getDuration());
        assertEquals(priceWithoutVAT, deletedPatternEntity.getPriceWithoutVAT());
    }

    private PatternEntity getPattern() {
        PatternEntity patternEntity = new PatternEntity();
        patternEntity.setName(name);
        patternEntity.setDescription(description);
        patternEntity.setDuration(duration);
        patternEntity.setPriceWithoutVAT(priceWithoutVAT);
        return patternEntity;
    }
}