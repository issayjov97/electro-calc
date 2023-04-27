package com.example.application.ui.views.offer.events;

import com.example.application.persistence.entity.OfferEntity;
import com.example.application.persistence.entity.OfferPattern;
import com.example.application.ui.views.offer.OfferDetailsForm;
import com.example.application.ui.views.offer.OfferPatternsView;
import com.vaadin.flow.component.ComponentEvent;

public abstract class OfferPatternsGridEvent extends ComponentEvent<OfferPatternsView> {
    private final OfferPattern item;

    protected OfferPatternsGridEvent(OfferPatternsView source, OfferPattern item) {
        super(source, false);
        this.item = item;
    }

    public OfferPattern getItem() {
        return item;
    }
}
