package com.example.application.ui.views.jobOrder.events;

import com.example.application.persistence.entity.JobOrderEntity;
import com.example.application.ui.views.jobOrder.JobOrderForm;
import com.vaadin.flow.component.ComponentEvent;

public abstract class OfferFormEvent extends ComponentEvent<JobOrderForm> {
    private final JobOrderEntity item;

    protected OfferFormEvent(JobOrderForm source, JobOrderEntity item) {
        super(source, false);
        this.item = item;
    }

    public JobOrderEntity getItem() {
        return item;
    }
}