package com.example.application.ui.views.admin.firm.events;

import com.example.application.persistence.entity.FirmEntity;
import com.example.application.ui.views.admin.firm.FirmForm;
import com.vaadin.flow.component.ComponentEvent;

public abstract class FirmFormEvent extends ComponentEvent<FirmForm> {
    private final FirmEntity item;

    protected FirmFormEvent(FirmForm source, FirmEntity item) {
        super(source, false);
        this.item = item;
    }

    public FirmEntity getItem() {
        return item;
    }
}