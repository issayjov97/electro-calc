package com.example.application.ui.views.admin.user.events;

import com.example.application.persistence.entity.UserEntity;
import com.example.application.ui.views.admin.user.UserForm;

public class DeleteEvent extends UserFormEvent {
    public DeleteEvent(UserForm source, UserEntity contact) {
        super(source, contact);
    }
}