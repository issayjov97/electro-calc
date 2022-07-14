package com.example.application.ui.views.settings;

import com.example.application.persistence.entity.AuthorityEntity;
import com.example.application.service.UserService;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

@PageTitle("Authorities")
@Route(value = "authorities", layout = SettingsView.class)
public class RolesView extends Div {
    private final UserService           userService;
    private final AddAuthorityDialog    addAuthorityDialog;
    final         Grid<AuthorityEntity> itemsGrid = new Grid<>(AuthorityEntity.class);

    public RolesView(UserService userService) {
        this.userService = userService;
        this.addAuthorityDialog = new AddAuthorityDialog(userService);
        setSizeFull();
        configureEvents();
        add(getRolesContent());
    }

    private VerticalLayout getRolesContent() {
        itemsGrid.addClassName("items-grid");
        itemsGrid.setColumns("id", "name");
        itemsGrid.setItems(userService.getAuthorities());
        itemsGrid.asSingleSelect().addValueChangeListener(event -> {
        });
        final Button addRoleButton = new Button("Add role");

        addRoleButton.addClickListener(e -> {
            addAuthorityDialog.openDialog();
        });

        itemsGrid.addComponentColumn((role) -> {
            Button deleteRoleButton = new Button("Delete");
            deleteRoleButton.addClickListener(e -> {
                userService.deleteAuthority(role.getId());
                updateList();
            });
            return deleteRoleButton;
        }).setHeader("Actions");

        return new VerticalLayout(new HorizontalLayout(addRoleButton), itemsGrid);
    }

    private void configureEvents() {
        addAuthorityDialog.addListener(AddAuthorityDialog.CloseEvent.class, e -> {
            updateList();
        });
    }

    private void updateList() {
        itemsGrid.setItems(userService.getAuthorities());
    }

}