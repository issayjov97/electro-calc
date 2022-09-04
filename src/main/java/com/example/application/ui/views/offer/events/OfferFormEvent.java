package com.example.application.ui.views.offer.events;

import com.example.application.persistence.entity.OfferEntity;
import com.example.application.ui.views.offer.OfferDetailsForm;
import com.example.application.ui.views.offer.OfferForm;
import com.vaadin.flow.component.ComponentEvent;

public abstract class OfferFormEvent extends ComponentEvent<OfferForm> {
    private final OfferEntity item;

    protected OfferFormEvent(OfferForm source, OfferEntity item) {
        super(source, false);
        this.item = item;
    }

    public OfferEntity getItem() {
        return item;
    }
}
