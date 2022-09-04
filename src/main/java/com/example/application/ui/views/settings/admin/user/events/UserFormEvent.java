package com.example.application.ui.views.settings.admin.user.events;

import com.example.application.persistence.entity.UserEntity;
import com.example.application.ui.views.settings.admin.user.UserForm;
import com.vaadin.flow.component.ComponentEvent;

public abstract class UserFormEvent extends ComponentEvent<UserForm> {
    private final UserEntity item;

    protected UserFormEvent(UserForm source, UserEntity item) {
        super(source, false);
        this.item = item;
    }

    public UserEntity getItem() {
        return item;
    }
}