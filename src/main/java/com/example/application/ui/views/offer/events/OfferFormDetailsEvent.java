package com.example.application.ui.views.offer.events;

import com.example.application.persistence.entity.OfferEntity;
import com.example.application.ui.views.offer.OfferDetailsForm;
import com.vaadin.flow.component.ComponentEvent;

public abstract class OfferFormDetailsEvent extends ComponentEvent<OfferDetailsForm> {
    private final OfferEntity item;

    protected OfferFormDetailsEvent(OfferDetailsForm source, OfferEntity item) {
        super(source, false);
        this.item = item;
    }

    public OfferEntity getItem() {
        return item;
    }
}
