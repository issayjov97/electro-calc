package com.example.application.ui.views.jobOrder.events;

import com.example.application.persistence.entity.JobOrderEntity;
import com.example.application.ui.views.jobOrder.JobOrderForm;

public class SaveEvent extends OfferFormEvent {
    public SaveEvent(JobOrderForm source, JobOrderEntity contact) {
        super(source, contact);
    }
}