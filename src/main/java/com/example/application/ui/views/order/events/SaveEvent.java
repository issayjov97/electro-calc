package com.example.application.ui.views.order.events;

import com.example.application.persistence.entity.OrderEntity;
import com.example.application.ui.views.order.OrderForm;

public class SaveEvent extends OrderFormEvent {
    public SaveEvent(OrderForm source, OrderEntity orderEntity) {
        super(source, orderEntity);
    }
}