package com.example.application.ui.views.offer.events;

import com.example.application.persistence.entity.OfferEntity;
import com.example.application.ui.views.offer.OfferForm;

public class DeleteEvent extends OfferFormEvent {
    public DeleteEvent(OfferForm source, OfferEntity offerEntity) {
        super(source, offerEntity);
    }
}