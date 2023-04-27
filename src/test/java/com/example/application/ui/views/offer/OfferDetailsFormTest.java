package com.example.application.ui.views.offer;

import com.example.application.model.enums.OfferStatus;
import com.example.application.persistence.entity.CustomerEntity;
import com.example.application.persistence.entity.OfferEntity;
import com.example.application.ui.views.offer.events.SaveOfferDetailsEvent;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.atomic.AtomicReference;

import static org.junit.jupiter.api.Assertions.assertEquals;

class OfferDetailsFormTest {
    private final String name = "OFFER_NAME";
    private final String description = "OFFER_DESCRIPTION";
    private final double distance = 100.0;
    private final String note = "OFFER_NOTE";
    private final String customer = "tester";
    private final OfferStatus status = OfferStatus.NONE;

    private Set<CustomerEntity> customers = new HashSet<>();

    @BeforeEach
    void setUp() {
        final var customerEntity = new CustomerEntity();
        customerEntity.setName(customer);
        customers.add(customerEntity);
    }

    @Test
    void formFieldsPopulated() {
        OfferDetailsForm OfferDetailsForm = new OfferDetailsForm(customers);
        OfferEntity offerEntity = getOffer();

        OfferDetailsForm.setEntity(offerEntity);

        assertEquals(name, OfferDetailsForm.getName().getValue());
        assertEquals(description, OfferDetailsForm.getDescription().getValue());
        assertEquals(distance, OfferDetailsForm.getDistance().getValue());
        assertEquals(note, OfferDetailsForm.getNote().getValue());
        assertEquals(status, OfferDetailsForm.getStatusesSelect().getValue());
        assertEquals(customer, OfferDetailsForm.getCustomersSelect().getValue().getName());
    }

    @Test
    void saveEventHasCorrectValues() {
        OfferDetailsForm OfferDetailsForm = new OfferDetailsForm(customers);
        OfferEntity OfferEntity = new OfferEntity();
        OfferDetailsForm.setEntity(OfferEntity);
        OfferDetailsForm.getName().setValue(name);
        OfferDetailsForm.getDescription().setValue(description);
        OfferDetailsForm.getNote().setValue(note);
        OfferDetailsForm.getStatusesSelect().setValue(status);
        OfferDetailsForm.getDistance().setValue(distance);
        OfferDetailsForm.getCustomersSelect().setValue(customers.stream().findFirst().orElseGet(null));

        AtomicReference<OfferEntity> savedContactRef = new AtomicReference<>(null);
        OfferDetailsForm.addListener(SaveOfferDetailsEvent.class, e -> {
            savedContactRef.set(e.getItem());
        });

        OfferDetailsForm.getSaveButton().click();

        OfferEntity savedOfferEntity = savedContactRef.get();

        assertEquals(name, savedOfferEntity.getName());
        assertEquals(description, savedOfferEntity.getDescription());
        assertEquals(distance, savedOfferEntity.getDistance());
        assertEquals(note, savedOfferEntity.getNote());
        assertEquals(status, savedOfferEntity.getStatus());
        assertEquals(customer, savedOfferEntity.getCustomerEntity().getName());
    }

    private OfferEntity getOffer() {
        OfferEntity offerEntity = new OfferEntity();
        offerEntity.setName(name);
        offerEntity.setDescription(description);
        offerEntity.setStatus(status);
        offerEntity.setDistance(distance);
        offerEntity.setNote(note);
        offerEntity.setCustomerEntity(customers.stream().findFirst().orElseGet(null));
        return offerEntity;
    }

}