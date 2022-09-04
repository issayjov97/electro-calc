package com.example.application.ui.views.offer.events;

import com.example.application.persistence.entity.OfferEntity;
import com.example.application.ui.views.offer.OfferDetailsForm;
import com.example.application.ui.views.offer.OfferForm;

public  class SaveOfferDetailsEvent extends OfferFormDetailsEvent {
        public SaveOfferDetailsEvent(OfferDetailsForm source, OfferEntity offerEntity) {
            super(source, offerEntity);
        }
    }
