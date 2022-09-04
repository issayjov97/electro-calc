package com.example.application.ui.views.settings;

import com.example.application.service.AuthService;
import com.example.application.ui.views.MainView;
import com.example.application.ui.views.settings.admin.authority.AuthoritiesView;
import com.example.application.ui.views.settings.admin.firm.FirmsView;
import com.example.application.ui.views.settings.admin.user.UsersView;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.tabs.Tab;
import com.vaadin.flow.component.tabs.Tabs;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.ParentLayout;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouterLayout;

@PageTitle("Settings")
@ParentLayout(MainView.class)
@Route(value = "settings", layout = MainView.class)
public class SettingsView extends VerticalLayout implements RouterLayout {
    private final Tab profile;
    private final Tab firmSettings;
    private final Tab firmDetails;
    private final Tab roles;
    private final Tab users;
    private final Tab firms;

    public SettingsView() {
        profile = new Tab(
                VaadinIcon.CLIPBOARD_USER.create(),
                new Span("Osobní údaje")
        );

        firmSettings = new Tab(
                VaadinIcon.TOOLS.create(),
                new Span("Nastavení")
        );

        firmDetails = new Tab(
                VaadinIcon.BUILDING.create(),
                new Span("Údaje o firmě")
        );
        roles = new Tab(
                VaadinIcon.USER_CHECK.create(),
                new Span("Role")
        );
        users = new Tab(
                VaadinIcon.USER.create(),
                new Span("Uživatele")
        );

        firms = new Tab(
                VaadinIcon.HOME.create(),
                new Span("Firmy")
        );


        Tabs tabs = new Tabs(profile, firmSettings, firmDetails);

        if (AuthService.isAdmin())
            tabs.add(roles, users, firms);

        tabs.addSelectedChangeListener(e -> {
            setContent(tabs.getSelectedTab());
        });
        add(tabs);
        tabs.setSelectedTab(profile);
        setContent(tabs.getSelectedTab());
        setSizeFull();
        addClassName("edit-view");
    }

    private void setContent(Tab tab) {
        if (tab.equals(profile))
            UI.getCurrent().navigate(ProfileView.class);
        else if (tab.equals(roles))
            UI.getCurrent().navigate(AuthoritiesView.class);
        else if (tab.equals(users))
            UI.getCurrent().navigate(UsersView.class);
        else if (tab.equals(firms))
            UI.getCurrent().navigate(FirmsView.class);
        else if (tab.equals(firmSettings))
            UI.getCurrent().navigate(FirmSettingsView.class);
        else if (tab.equals(firmDetails))
            UI.getCurrent().navigate(FirmDetailsView.class);
    }
}
