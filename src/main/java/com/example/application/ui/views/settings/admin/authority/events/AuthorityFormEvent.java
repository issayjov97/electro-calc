package com.example.application.ui.views.settings.admin.authority.events;

import com.example.application.persistence.entity.AuthorityEntity;
import com.example.application.ui.views.settings.admin.authority.AuthorityForm;
import com.vaadin.flow.component.ComponentEvent;

public abstract class AuthorityFormEvent extends ComponentEvent<AuthorityForm> {
    private final AuthorityEntity item;

    protected AuthorityFormEvent(AuthorityForm source, AuthorityEntity item) {
        super(source, false);
        this.item = item;
    }

    public AuthorityEntity getItem() {
        return item;
    }
}