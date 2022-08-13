package com.example.application.ui.views.demand.events;

import com.example.application.persistence.entity.DemandEntity;
import com.example.application.ui.views.demand.DemandForm;

public class DeleteEvent extends OfferFormEvent {
        public DeleteEvent(DemandForm source, DemandEntity contact) {
            super(source, contact);
        }
    }
