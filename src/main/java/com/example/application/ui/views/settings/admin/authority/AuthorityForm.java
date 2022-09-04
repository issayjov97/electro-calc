package com.example.application.ui.views.settings.admin.authority;

import com.example.application.persistence.entity.AuthorityEntity;
import com.example.application.ui.events.CloseEvent;
import com.example.application.ui.views.AbstractForm;
import com.example.application.ui.views.settings.admin.authority.events.SaveEvent;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.ValidationException;

public class AuthorityForm extends AbstractForm<AuthorityEntity> {
    private TextField roleField = new TextField("Název");

    public AuthorityForm() {
        super(new BeanValidationBinder<>(AuthorityEntity.class));
        setBinder();
        dialog.add(getHeadline(), createDialogLayout(), createButtonsLayout());
    }

    @Override
    protected void setBinder() {
        binder.forField(roleField).asRequired("Název je povinny")
                .bind(AuthorityEntity::getName, AuthorityEntity::setName);
    }

    @Override
    protected Component createDialogLayout() {
        FormLayout formLayout = new FormLayout();
        formLayout.add(roleField);
        formLayout.setMaxWidth("400px");
        return formLayout;
    }

    @Override
    protected HorizontalLayout createButtonsLayout() {
        final HorizontalLayout buttonsMenu = new HorizontalLayout();
        buttonsMenu.setMargin(true);
        saveButton.addClickListener(event -> validateAndSave());
        cancelButton.addClickListener(event -> fireEvent(new CloseEvent(this, false)));
        buttonsMenu.setJustifyContentMode(FlexComponent.JustifyContentMode.CENTER);
        buttonsMenu.add(saveButton, cancelButton);
        return buttonsMenu;
    }

    @Override
    protected void validateAndSave() {
        try {
            binder.writeBean(getEntity());
            fireEvent(new SaveEvent(this, getEntity()));
        } catch (ValidationException e) {
            e.printStackTrace();
        }
    }
}