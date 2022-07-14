package com.example.application.ui.views.settings;

import com.example.application.service.PatternService;
import com.example.application.service.UserService;
import com.example.application.ui.views.MainView;
import com.example.application.ui.views.admin.UsersView;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.tabs.Tab;
import com.vaadin.flow.component.tabs.TabVariant;
import com.vaadin.flow.component.tabs.Tabs;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.ParentLayout;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouterLayout;

@PageTitle("Settings")
@ParentLayout(MainView.class)
@Route(value = "settings", layout = MainView.class)
public class SettingsView extends VerticalLayout implements RouterLayout {
    private final PatternService patternService;
    private final UserService    userService;
    private       Tab            profile;
    private       Tab            accountSecurity;
    private       Tab            roles;
    private       Tab            users;

    public SettingsView(PatternService patternService, UserService userService) {
        this.patternService = patternService;
        this.userService = userService;
        profile = new Tab(
                VaadinIcon.USER.create(),
                new Span("Personal Info")
        );
        accountSecurity = new Tab(
                VaadinIcon.SAFE_LOCK.create(),
                new Span("Security")
        );
        roles = new Tab(
                VaadinIcon.USER_CHECK.create(),
                new Span("Roles")
        );

        users = new Tab(
                VaadinIcon.USER.create(),
                new Span("Users")
        );

        Tabs tabs = new Tabs(profile, accountSecurity, roles, users);

        for (Tab tab : new Tab[]{profile, accountSecurity, roles, users}) {
            tab.addThemeVariants(TabVariant.LUMO_ICON_ON_TOP);
        }

        tabs.addSelectedChangeListener(e -> {
            setContent(tabs.getSelectedTab());
        });
        setContent(tabs.getSelectedTab());
        add(tabs);
    }

    private void setContent(Tab tab) {
        if (tab.equals(profile))
            UI.getCurrent().navigate(ProfileView.class);
        else if (tab.equals(roles))
            UI.getCurrent().navigate(RolesView.class);
        else if (tab.equals(accountSecurity))
            UI.getCurrent().navigate(AccountSecurityView.class);
        else if (tab.equals(users))
            UI.getCurrent().navigate(UsersView.class);
        else
            UI.getCurrent().navigate(ProfileView.class);
    }
}
