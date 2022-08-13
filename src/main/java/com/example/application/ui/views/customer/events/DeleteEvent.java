package com.example.application.ui.views.customer.events;

import com.example.application.persistence.entity.CustomerEntity;
import com.example.application.ui.views.customer.CustomerForm;

public class DeleteEvent extends CustomerFormEvent {
        public DeleteEvent(CustomerForm source, CustomerEntity item) {
            super(source, item);
        }
    }