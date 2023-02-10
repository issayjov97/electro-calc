package com.example.application.ui.views;

import com.example.application.service.AuthService;
import com.example.application.service.ImportService;
import com.example.application.ui.components.NotificationService;
import com.example.application.ui.views.customer.CustomersView;
import com.example.application.ui.views.login.LoginView;
import com.example.application.ui.views.offer.OffersView;
import com.example.application.ui.views.patern.PatternsView;
import com.example.application.ui.views.settings.ProfileView;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.applayout.DrawerToggle;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.menubar.MenuBar;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.upload.Upload;
import com.vaadin.flow.component.upload.receivers.MemoryBuffer;
import com.vaadin.flow.router.RouterLink;
import com.vaadin.flow.server.VaadinServletRequest;
import com.vaadin.flow.server.VaadinServletResponse;
import com.vaadin.flow.server.VaadinSession;
import com.vaadin.flow.theme.Theme;
import com.vaadin.flow.theme.lumo.Lumo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

@Theme(themeFolder = "flowcrmtutorial", value = Lumo.class)
@CssImport(value = "./views/button.css", themeFor = "vaadin-button")
public class MainView extends AppLayout {
    private static Logger loger = LoggerFactory.getLogger(MainView.class);
    private final ImportService importService;
    private final MenuBar menuBar = new MenuBar();
    private Button logoutButton;
    private Button importButton;

    public MainView(ImportService importService) {
        this.importService = importService;
        createHeader();
        createDrawer();
    }

    private void createHeader() {
        H2 logo = new H2("Rozpočet elektro");
        logo.addClassNames("title");
        Icon vaadinIcon = new Icon(VaadinIcon.CALC);
        vaadinIcon.getStyle().set("margin-right", "10px");
        vaadinIcon.setColor("white");
        vaadinIcon.setSize("30px");
        HorizontalLayout header = new HorizontalLayout(new DrawerToggle(), vaadinIcon, logo);
        header.setDefaultVerticalComponentAlignment(FlexComponent.Alignment.CENTER);
        header.setWidth("100%");
        header.addClassNames("header");
        addToNavbar(header);
    }

    private void createDrawer() {
        List<RouterLink> links = new ArrayList<>();
        links.add(createMenuLink(PatternsView.class, "Položky", VaadinIcon.CALENDAR.create()));
        links.add(createMenuLink(CustomersView.class, "Zákazníci", VaadinIcon.USERS.create()));
        links.add(createMenuLink(OffersView.class, "Nabídky", VaadinIcon.EDIT.create()));
        links.add(createMenuLink(ProfileView.class, "Nastavení", VaadinIcon.COGS.create()));
        VerticalLayout drawer = new VerticalLayout(links.toArray(RouterLink[]::new));
        this.logoutButton = createMenuButton("Logout", VaadinIcon.SIGN_OUT.create());
        this.logoutButton.addClickListener(e -> logout());
        drawer.add(logoutButton);
        if (AuthService.isAdmin()) {
            this.importButton = createMenuButton("Import položek", VaadinIcon.UPLOAD_ALT.create());
            var uploadMenuBar = createUploadButton();
            drawer.add(uploadMenuBar);
        }
        addToDrawer(drawer);
    }

    private RouterLink createMenuLink(Class<? extends Component> viewClass, String caption, Icon icon) {
        final RouterLink routerLink = new RouterLink(null, viewClass);
        routerLink.setClassName("menu-link");
        routerLink.add(icon);
        routerLink.add(new Span(caption));
        icon.setSize("24px");
        return routerLink;
    }

    private Button createMenuButton(String caption, Icon icon) {
        final Button routerButton = new Button(caption);
        routerButton.setClassName("menu-button");
        routerButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY_INLINE);
        routerButton.setIcon(icon);
        icon.setSize("24px");
        return routerButton;
    }

    private void logout() {
        UI.getCurrent().navigate(LoginView.class);
        new SecurityContextLogoutHandler().logout(
                VaadinServletRequest.getCurrent().getHttpServletRequest(),
                VaadinServletResponse.getCurrent().getHttpServletResponse(),
                SecurityContextHolder.getContext().getAuthentication());
        VaadinSession.getCurrent().close();
    }

    private Upload createUploadButton() {
        var importButton = new Button("Import", VaadinIcon.UPLOAD.create());
        importButton.getElement().setAttribute("theme", "drawer-button");
        MemoryBuffer memoryBuffer = new MemoryBuffer();
        Upload upload = new Upload(memoryBuffer);
        upload.getElement().getStyle().set("margin-top", "5px");
        importButton.addClassName("drawer-button");
        upload.setDropAllowed(false);
        upload.setAcceptedFileTypes("text/csv", ".csv");
        upload.addFinishedListener(e -> {
            InputStream inputStream = memoryBuffer.getInputStream();
            try {
                importService.importPatterns(inputStream);
            } catch (RuntimeException exception) {
                loger.error("Import patterns",exception);
                NotificationService.error(exception.getMessage());
            }
        });
        upload.setUploadButton(importButton);
        return upload;
    }
}
