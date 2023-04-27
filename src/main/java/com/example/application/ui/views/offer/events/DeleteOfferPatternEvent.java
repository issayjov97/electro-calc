package com.example.application.ui.views.offer.events;

import com.example.application.persistence.entity.OfferPattern;
import com.example.application.ui.views.offer.OfferPatternsView;

public class DeleteOfferPatternEvent extends OfferPatternsGridEvent {
    public DeleteOfferPatternEvent(OfferPatternsView source, OfferPattern offerEntity) {
        super(source, offerEntity);
    }
}