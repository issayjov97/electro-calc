package com.example.application.ui.views.settings;

import com.example.application.persistence.entity.AuthorityEntity;
import com.example.application.service.AuthorityService;
import com.vaadin.flow.component.ComponentEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.shared.Registration;

public class AddAuthorityDialog extends Div {
    private final Dialog           dialog;
    private final AuthorityService authorityService;

    public AddAuthorityDialog(AuthorityService authorityService) {
        this.authorityService = authorityService;
        this.dialog = new Dialog();

        VerticalLayout dialogLayout = createDialogLayout();
        dialog.add(dialogLayout);
    }

    void openDialog() {
        this.dialog.open();
    }

    private VerticalLayout createDialogLayout() {
        H2 headline = new H2("Add new role");
        TextField roleField = new TextField("Name");
        VerticalLayout fieldLayout = new VerticalLayout(roleField);
        fieldLayout.setSpacing(false);
        fieldLayout.setPadding(false);
        fieldLayout.setAlignItems(FlexComponent.Alignment.STRETCH);

        Button cancelButton = new Button("Cancel", e -> dialog.close());
        Button saveButton = new Button("Save", e -> {
            if (roleField.getValue() != null && !roleField.getValue().isBlank()) {
                var authority = new AuthorityEntity();
                authority.setName(roleField.getValue());
                authorityService.save(authority);
                var notification = Notification.show("Authoirtyy was saved");
                notification.addThemeVariants(NotificationVariant.LUMO_SUCCESS);
                notification.setPosition(Notification.Position.TOP_CENTER);
                roleField.clear();
                dialog.close();
                fireEvent(new CloseEvent(this));
            }
        });

        HorizontalLayout buttonLayout = new HorizontalLayout(cancelButton, saveButton);
        buttonLayout.setJustifyContentMode(FlexComponent.JustifyContentMode.END);

        VerticalLayout dialogLayout = new VerticalLayout(headline, fieldLayout, buttonLayout);
        dialogLayout.setPadding(false);
        dialogLayout.setAlignItems(FlexComponent.Alignment.STRETCH);
        dialogLayout.getStyle().set("width", "300px").set("max-width", "100%");

        return dialogLayout;
    }

    public static abstract class AuthorityDialogEvent extends ComponentEvent<AddAuthorityDialog> {

        protected AuthorityDialogEvent(AddAuthorityDialog source) {
            super(source, false);
        }

    }

    public static class CloseEvent extends AddAuthorityDialog.AuthorityDialogEvent {
        CloseEvent(AddAuthorityDialog source) {
            super(source);
        }
    }

    public <T extends ComponentEvent<?>> Registration addListener(Class<T> eventType,
                                                                  ComponentEventListener<T> listener) {
        return getEventBus().addListener(eventType, listener);
    }

}