package com.example.application.ui.views.demand.events;

import com.example.application.persistence.entity.DemandEntity;
import com.example.application.ui.views.demand.DemandForm;
import com.vaadin.flow.component.ComponentEvent;

public abstract class OfferFormEvent extends ComponentEvent<DemandForm> {
        private final DemandEntity item;

        protected OfferFormEvent(DemandForm source, DemandEntity item) {
            super(source, false);
            this.item = item;
        }

        public DemandEntity getItem() {
            return item;
        }
    }