package com.example.application.ui.views.offer.events;

import com.example.application.persistence.entity.OfferPattern;
import com.example.application.ui.views.offer.OfferPatternsView;

public class UpdateOfferPatternEvent extends OfferPatternsGridEvent {
    public UpdateOfferPatternEvent(OfferPatternsView source, OfferPattern offerEntity) {
        super(source, offerEntity);
    }
}