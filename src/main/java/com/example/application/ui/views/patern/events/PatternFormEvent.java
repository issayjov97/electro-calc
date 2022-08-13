package com.example.application.ui.views.patern.events;

import com.example.application.persistence.entity.PatternEntity;
import com.example.application.ui.views.patern.PatternForm;
import com.vaadin.flow.component.ComponentEvent;

public abstract class PatternFormEvent extends ComponentEvent<PatternForm> {
    private final PatternEntity item;

    protected PatternFormEvent(PatternForm source, PatternEntity item) {
        super(source, false);
        this.item = item;
    }

    public PatternEntity getItem() {
        return item;
    }
}