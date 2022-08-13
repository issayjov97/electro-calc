package com.example.application.ui.views.customer.events;

import com.example.application.persistence.entity.CustomerEntity;
import com.example.application.ui.views.customer.CustomerForm;
import com.vaadin.flow.component.ComponentEvent;

public abstract class CustomerFormEvent extends ComponentEvent<CustomerForm> {
        private final CustomerEntity item;

        protected CustomerFormEvent(CustomerForm source, CustomerEntity item) {
            super(source, false);
            this.item = item;
        }

        public CustomerEntity getItem() {
            return item;
        }
    }