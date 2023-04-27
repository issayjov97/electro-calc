package com.example.application.ui.views.offer;

import com.example.application.persistence.entity.OfferEntity;
import com.example.application.ui.views.offer.events.DeleteEvent;
import com.example.application.ui.views.offer.events.SaveEvent;
import org.junit.jupiter.api.Test;

import java.util.concurrent.atomic.AtomicReference;

import static org.junit.jupiter.api.Assertions.assertEquals;

class OfferFormTest {

    private final String name = "OFFER_NAME";
    private final String description = "OFFER_DESCRIPTION";

    @Test
    void formFieldsPopulated() {
        OfferForm offerForm = new OfferForm();
        OfferEntity OfferEntity = getOffer();

        offerForm.setEntity(OfferEntity);

        assertEquals(name, offerForm.getName().getValue());
        assertEquals(description, offerForm.getDescription().getValue());
    }

    @Test
    void saveEventHasCorrectValues() {
        OfferForm offerForm = new OfferForm();
        OfferEntity OfferEntity = new OfferEntity();
        offerForm.setEntity(OfferEntity);
        offerForm.getName().setValue(name);
        offerForm.getDescription().setValue(description);

        AtomicReference<OfferEntity> savedContactRef = new AtomicReference<>(null);
        offerForm.addListener(SaveEvent.class, e -> {
            savedContactRef.set(e.getItem());
        });

        offerForm.getSaveButton().click();

        OfferEntity savedOfferEntity = savedContactRef.get();

        assertEquals(name, savedOfferEntity.getName());
        assertEquals(description, savedOfferEntity.getDescription());
    }

    @Test
    void deleteEventHasCorrectValues() {
        OfferForm offerForm = new OfferForm();
        OfferEntity OfferEntity = getOffer();

        AtomicReference<OfferEntity> deleteRef = new AtomicReference<>(null);
        offerForm.addListener(DeleteEvent.class, e -> {
            deleteRef.set(e.getItem());
        });

        offerForm.setEntity(OfferEntity);
        offerForm.getDeleteButton().click();

        OfferEntity deletedOfferEntity = deleteRef.get();

        assertEquals(name, deletedOfferEntity.getName());
        assertEquals(description, deletedOfferEntity.getDescription());
    }

    private OfferEntity getOffer() {
        OfferEntity OfferEntity = new OfferEntity();
        OfferEntity.setName(name);
        OfferEntity.setDescription(description);
        return OfferEntity;
    }

}