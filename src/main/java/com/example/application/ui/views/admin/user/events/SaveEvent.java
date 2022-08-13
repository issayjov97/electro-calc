package com.example.application.ui.views.admin.user.events;

import com.example.application.persistence.entity.UserEntity;
import com.example.application.ui.views.admin.user.UserForm;

public class SaveEvent extends UserFormEvent {
    public SaveEvent(UserForm source, UserEntity contact) {
        super(source, contact);
    }
}

