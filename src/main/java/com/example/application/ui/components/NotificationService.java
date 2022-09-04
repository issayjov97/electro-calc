package com.example.application.ui.components;

import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;

public class NotificationService {

    public static void success(String message) {
        var notification = Notification.show(message);
        notification.addThemeVariants(NotificationVariant.LUMO_SUCCESS);
        notification.setPosition(com.vaadin.flow.component.notification.Notification.Position.TOP_CENTER);
        notification.add(VaadinIcon.CHECK_CIRCLE.create());
    }

    public static void success() {
        success("");
    }

    public static void error(String message) {
        var notification = com.vaadin.flow.component.notification.Notification.show(message);
        notification.addThemeVariants(NotificationVariant.LUMO_ERROR);
        notification.setPosition(com.vaadin.flow.component.notification.Notification.Position.TOP_CENTER);
    }
}
