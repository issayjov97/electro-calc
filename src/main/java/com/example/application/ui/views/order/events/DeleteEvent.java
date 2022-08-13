package com.example.application.ui.views.order.events;

import com.example.application.persistence.entity.OrderEntity;
import com.example.application.ui.views.order.OrderForm;

public class DeleteEvent extends OrderFormEvent {
    public DeleteEvent(OrderForm source, OrderEntity contact) {
        super(source, contact);
    }
}