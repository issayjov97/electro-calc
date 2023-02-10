package com.example.application.ui.views.settings.admin.authority;

import com.example.application.persistence.entity.AuthorityEntity;
import com.example.application.service.AuthorityService;
import com.example.application.ui.components.NotificationService;
import com.example.application.ui.events.CloseEvent;
import com.example.application.ui.views.AbstractServicesView;
import com.example.application.ui.views.settings.SettingsView;
import com.example.application.ui.views.settings.admin.authority.events.SaveEvent;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.annotation.Secured;

@PageTitle("Role")
@Secured("ADMIN")
@Route(value = "authorities", layout = SettingsView.class)
public class AuthoritiesView extends AbstractServicesView<AuthorityEntity, AuthorityEntity> {

    private static Logger logger = LoggerFactory.getLogger(AuthoritiesView.class);
    private final AuthorityService authorityService;
    private final AuthorityForm authorityForm;
    private final Button addButton = new Button("Přidat roli");

    public AuthoritiesView(AuthorityService authorityService) {
        super(new Grid<>(), authorityService);
        this.authorityService = authorityService;
        this.authorityForm = new AuthorityForm();
        setSizeFull();
        configureEvents();
        setSizeFull();
        configureGrid();
        configureForm();
    }


    @Override
    protected void configureForm() {
        final VerticalLayout verticalLayout = new VerticalLayout();
        verticalLayout.setSizeFull();
        verticalLayout.setClassName("admin-config-view");
        verticalLayout.add(getToolBar(), getContent());
        add(verticalLayout, authorityForm);
    }

    @Override
    protected void configureGrid() {
        getItems().addClassName("items-grid");
        getItems().addColumn(AuthorityEntity::getName).setHeader("Název");
        getItems().getColumns().forEach(itemColumn -> itemColumn.setAutoWidth(true));
        getItems().setItems(authorityService.loadAll());
        getItems().addComponentColumn((role) -> {
            Button deleteRoleButton = new Button();
            deleteRoleButton.setIcon(VaadinIcon.TRASH.create());
            deleteRoleButton.addClickListener(e -> {
                try {
                    authorityService.deleteById(role.getId());
                    NotificationService.success();
                } catch (Exception ex) {
                    logger.error("Delete authority",ex);
                    NotificationService.error("Uživatelé mají přiřazenou tuto roli");
                }

                updateList();
            });
            return deleteRoleButton;
        }).setHeader("Akce");
        getItems().addThemeVariants(GridVariant.LUMO_NO_ROW_BORDERS, GridVariant.LUMO_NO_BORDER);
    }

    @Override
    protected HorizontalLayout getToolBar() {
        addButton.setIcon(VaadinIcon.PLUS.create());
        addButton.addClickListener(e -> {
            getItems().asSingleSelect().clear();
            authorityForm.setEntity(new AuthorityEntity());
            authorityForm.open("Nová role");
        });
        HorizontalLayout toolbar = new HorizontalLayout(addButton);
        toolbar.setJustifyContentMode(JustifyContentMode.START);
        return toolbar;
    }

    @Override
    protected void configureEvents() {
        authorityForm.addListener(SaveEvent.class, this::saveItem);
        authorityForm.addListener(CloseEvent.class, e -> closeEditor());
    }

    @Override
    protected void closeEditor() {
        authorityForm.setEntity(null);
        authorityForm.close();
    }

    private void saveItem(SaveEvent event) {
        authorityService.save(event.getItem());
        updateList();
        closeEditor();
        NotificationService.success();
    }

    @Override
    protected void updateList() {
        getItems().setItems(authorityService.loadAll());
    }

}