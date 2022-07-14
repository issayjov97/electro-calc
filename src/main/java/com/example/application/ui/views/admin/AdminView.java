package com.example.application.ui.views.admin;

import com.example.application.ui.views.settings.SettingsView;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;

@Route(value = "admin", layout = SettingsView.class)
public class AdminView extends VerticalLayout {
    @Autowired
    public AdminView() {
        Label label = new Label("Looks like you are admin!");
        add(label);
    }

}