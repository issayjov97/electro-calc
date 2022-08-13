package com.example.application.ui.views.settings;

import com.example.application.persistence.entity.AuthorityEntity;
import com.example.application.service.AuthorityService;
import com.example.application.ui.events.CloseEvent;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import org.springframework.security.access.annotation.Secured;

@PageTitle("Authorities")
@Secured("ADMIN")
@Route(value = "authorities", layout = SettingsView.class)
public class RolesView extends Div {
    private final AuthorityService      authorityService;
    private final AddAuthorityDialog    addAuthorityDialog;
    final         Grid<AuthorityEntity> itemsGrid = new Grid<>(AuthorityEntity.class);

    public RolesView(AuthorityService authorityService) {
        this.authorityService = authorityService;
        this.addAuthorityDialog = new AddAuthorityDialog(authorityService);
        setSizeFull();
        configureEvents();
        add(getRolesContent());
    }

    private VerticalLayout getRolesContent() {
        itemsGrid.addClassName("items-grid");
        itemsGrid.setColumns("id", "name");
        itemsGrid.setItems(authorityService.loadAll());
        itemsGrid.asSingleSelect().addValueChangeListener(event -> {
        });
        final Button addRoleButton = new Button("Add role");

        addRoleButton.addClickListener(e -> {
            addAuthorityDialog.openDialog();
        });

        itemsGrid.addComponentColumn((role) -> {
            Button deleteRoleButton = new Button("Delete");
            deleteRoleButton.addClickListener(e -> {
                authorityService.deleteById(role.getId());
                updateList();
            });
            return deleteRoleButton;
        }).setHeader("Actions");

        return new VerticalLayout(new HorizontalLayout(addRoleButton), itemsGrid);
    }

    private void configureEvents() {
        addAuthorityDialog.addListener(CloseEvent.class, e -> {
            updateList();
        });
    }

    private void updateList() {
        itemsGrid.setItems(authorityService.loadAll());
    }
}