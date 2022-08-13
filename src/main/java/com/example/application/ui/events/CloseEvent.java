package com.example.application.ui.events;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.ComponentEvent;

public class CloseEvent extends ComponentEvent<Component> {

    public CloseEvent(Component source, boolean fromClient) {
        super(source, fromClient);
    }
}