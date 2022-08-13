package com.example.application.ui.views.jobOrder.events;

import com.example.application.persistence.entity.JobOrderEntity;
import com.example.application.ui.views.jobOrder.JobOrderForm;

public class DeleteEvent extends OfferFormEvent {
    public DeleteEvent(JobOrderForm source, JobOrderEntity contact) {
        super(source, contact);
    }
}