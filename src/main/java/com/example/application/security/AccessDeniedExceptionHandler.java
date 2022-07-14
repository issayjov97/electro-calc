package com.example.application.security;

import com.example.application.ui.views.MainView;
import com.vaadin.flow.component.Tag;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.ErrorParameter;
import com.vaadin.flow.router.HasErrorParameter;
import com.vaadin.flow.router.ParentLayout;

import javax.servlet.http.HttpServletResponse;

@Tag(Tag.DIV)
@ParentLayout(MainView.class)
public class AccessDeniedExceptionHandler extends VerticalLayout implements HasErrorParameter<AccessDeniedException> {

    public AccessDeniedExceptionHandler() {
        setWidthFull();
    }

    @Override
    public int setErrorParameter(BeforeEnterEvent beforeEnterEvent, ErrorParameter<AccessDeniedException> errorParameter) {
        getElement().setText("Tried to navigate to a view without "
                + "correct access rights");
        var notification = Notification.show("403 - Access Forbidden");
        notification.addThemeVariants(NotificationVariant.LUMO_ERROR);
        notification.setPosition(Notification.Position.TOP_CENTER);
        return HttpServletResponse.SC_FORBIDDEN;
    }
}