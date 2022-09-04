package com.example.application.ui.views.settings.admin.firm.events;

import com.example.application.persistence.entity.FirmEntity;
import com.example.application.ui.views.settings.admin.firm.FirmForm;

public class DeleteEvent extends FirmFormEvent {
    public DeleteEvent(FirmForm source, FirmEntity contact) {
        super(source, contact);
    }
}