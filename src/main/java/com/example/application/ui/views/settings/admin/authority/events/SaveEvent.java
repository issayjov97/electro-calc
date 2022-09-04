package com.example.application.ui.views.settings.admin.authority.events;

import com.example.application.persistence.entity.AuthorityEntity;
import com.example.application.ui.views.settings.admin.authority.AuthorityForm;

public class SaveEvent extends AuthorityFormEvent {
    public SaveEvent(AuthorityForm source, AuthorityEntity item) {
        super(source, item);
    }
}
