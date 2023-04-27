package com.example.application.error;

import com.example.application.error.AccessDeniedException;
import com.example.application.ui.components.NotificationService;
import com.example.application.ui.views.MainView;
import com.vaadin.flow.component.Tag;
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
        getElement().setText("Pokusili jste se přejít na stránku bez správných přístupových práv");
        getElement().getStyle().set("text-align","center");
        NotificationService.error("403 - Access Forbidden");
        return HttpServletResponse.SC_FORBIDDEN;
    }
}