package com.example.application.ui.views.order.events;

import com.example.application.persistence.entity.OrderEntity;
import com.example.application.ui.views.order.OrderForm;
import com.vaadin.flow.component.ComponentEvent;

public abstract class OrderFormEvent extends ComponentEvent<OrderForm> {
    private final OrderEntity item;

    protected OrderFormEvent(OrderForm source, OrderEntity item) {
        super(source, false);
        this.item = item;
    }

    public OrderEntity getItem() {
        return item;
    }
}