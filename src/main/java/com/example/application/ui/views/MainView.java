package com.example.application.ui.views;

import com.example.application.service.AuthService;
import com.example.application.service.UserService;
import com.example.application.ui.views.customer.CustomersView;
import com.example.application.ui.views.demand.DemandsView;
import com.example.application.ui.views.jobOrder.JobOrderView;
import com.example.application.ui.views.login.LoginView;
import com.example.application.ui.views.offer.OffersView;
import com.example.application.ui.views.order.OrdersView;
import com.example.application.ui.views.patern.PatternsView;
import com.example.application.ui.views.settings.SettingsView;
import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.applayout.DrawerToggle;
import com.vaadin.flow.component.avatar.Avatar;
import com.vaadin.flow.component.avatar.AvatarVariant;
import com.vaadin.flow.component.contextmenu.MenuItem;
import com.vaadin.flow.component.contextmenu.SubMenu;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.menubar.MenuBar;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.HighlightConditions;
import com.vaadin.flow.router.RouterLink;
import com.vaadin.flow.server.VaadinServletRequest;
import com.vaadin.flow.server.VaadinServletResponse;
import com.vaadin.flow.server.VaadinSession;
import com.vaadin.flow.theme.Theme;
import com.vaadin.flow.theme.lumo.Lumo;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;

import java.util.ArrayList;
import java.util.List;

@Theme(themeFolder = "flowcrmtutorial", value = Lumo.class)
public class MainView extends AppLayout {
    private final UserService userService;
    private final MenuBar     menuBar = new MenuBar();

    public MainView(UserService userService) {
        this.userService = userService;
        createHeader();
        createDrawer();
    }

    private void createHeader() {
        H2 logo = new H2("Vaadin CRM");
        logo.addClassNames("title");

        HorizontalLayout left = new HorizontalLayout(new DrawerToggle(), logo);
        HorizontalLayout right = new HorizontalLayout(buildMenuBar(), createUserAvatar());
        HorizontalLayout header = new HorizontalLayout(left, right);
        header.setDefaultVerticalComponentAlignment(FlexComponent.Alignment.CENTER);
        header.setWidth("100%");
        header.addClassNames("header");
        left.setWidth("100%");
        right.setWidth("100%");
        left.setJustifyContentMode(FlexComponent.JustifyContentMode.START);
        right.setJustifyContentMode(FlexComponent.JustifyContentMode.END);
        right.setAlignItems(FlexComponent.Alignment.CENTER);
        left.setAlignItems(FlexComponent.Alignment.CENTER);
        right.setMargin(true);
        addToNavbar(header);
    }

    private void createDrawer() {
        List<RouterLink> links = new ArrayList<>();
        RouterLink listLink = new RouterLink("Polozky(Patterns)", PatternsView.class);
        RouterLink customersLink = new RouterLink("Zakaznici(Customers)", CustomersView.class);
        RouterLink ordersLink = new RouterLink("Objednavky(Orders)", OrdersView.class);
        RouterLink offersLink = new RouterLink("Nabidky(Offers)", OffersView.class);
        RouterLink demandsLink = new RouterLink("Poptavky(Demands)", DemandsView.class);
        RouterLink jobOrdersLink = new RouterLink("Zakazky(Job order)", JobOrderView.class);

        listLink.setHighlightCondition(HighlightConditions.sameLocation());
        links.add(listLink);
        links.add(customersLink);
        links.add(ordersLink);
        links.add(offersLink);
        links.add(demandsLink);
        links.add(jobOrdersLink);
        VerticalLayout drawer = new VerticalLayout(links.toArray(RouterLink[]::new));
        drawer.setSizeUndefined();
        addToDrawer(drawer);
    }

    private Avatar createUserAvatar() {
        var user = userService.getByUsername(AuthService.getUsername());
        String name = user.getFirstName() + " " + user.getLastName();
        Avatar avatarName = new Avatar(name);
        avatarName.setColorIndex(0);
        avatarName.addThemeVariants(AvatarVariant.LUMO_XLARGE);
        return avatarName;
    }

    private MenuBar buildMenuBar() {
        Icon configsIcon = VaadinIcon.COGS.create();
        configsIcon.setSize("36px");
        MenuItem settingsItem = menuBar.addItem(configsIcon);
        SubMenu settingsSubMenu = settingsItem.getSubMenu();
        settingsSubMenu.addItem("Settings", (ComponentEventListener<ClickEvent<MenuItem>>) menuItemClickEvent -> {
            UI.getCurrent().navigate(SettingsView.class);
        });
        settingsSubMenu.addItem("Logout", (ComponentEventListener<ClickEvent<MenuItem>>) menuItemClickEvent -> {
            UI.getCurrent().navigate(LoginView.class);
            new SecurityContextLogoutHandler().logout(
                    VaadinServletRequest.getCurrent().getHttpServletRequest(),
                    VaadinServletResponse.getCurrent().getHttpServletResponse(),
                    SecurityContextHolder.getContext().getAuthentication());
            VaadinSession.getCurrent().close();
        });
        return menuBar;
    }
}
