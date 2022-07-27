package com.example.application.ui.views.admin;

import com.example.application.persistence.entity.AuthorityEntity;
import com.example.application.persistence.entity.UserEntity;
import com.example.application.service.AuthorityService;
import com.example.application.service.FirmService;
import com.example.application.service.UserService;
import com.example.application.ui.views.AbstractServicesView;
import com.example.application.ui.views.settings.SettingsView;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import org.springframework.security.access.annotation.Secured;

import java.util.stream.Collectors;

@PageTitle("Users")
@Secured("ADMIN")
@Route(value = "users", layout = SettingsView.class)
public class UsersView extends AbstractServicesView<UserEntity, UserEntity> {
    private final TextField   usernameFilter = new TextField();
    private final TextField   emailFilter    = new TextField();
    private final UserForm    userForm;
    private final Button      addButton      = new Button("Add user");
    private final Button      filterButton   = new Button("Filter");
    private final UserService userService;

    public UsersView(UserService userService, AuthorityService authorityService, FirmService firmService) {
        super(new Grid<>(UserEntity.class), userService);
        this.userService = userService;
        this.userForm = new UserForm(firmService);
        userForm.setAllAuthorities(authorityService.loadAll());
        addClassName("Users-view");
        setSizeFull();
        configureGrid();
        add(getToolBar(), getContent(), userForm);
        configureEvents();
    }

    @Override
    protected void configureForm() {

    }

    @Override
    protected void configureGrid() {
        getItems().addClassName("Users-grid");
        getItems().setColumns("username", "firstName", "lastName", "email", "enabled", "createdAt");
        getItems().getColumns().forEach(itemColumn -> itemColumn.setAutoWidth(true));
        getItems().setItems(userService.loadAll());
        getItems().asSingleSelect().addValueChangeListener(event -> {
            if (event.getValue() != null) {
                userForm.setEntity(event.getValue());
                userForm.open("Edit user");
            }
        });
        getItems().addColumn(e -> e.getAuthorityEntities().stream().map(AuthorityEntity::getName).collect(Collectors.joining(","))).setHeader("Authorities");
        getItems().addComponentColumn((user) -> {
            Button deleteButton = new Button("Delete");
            deleteButton.addClickListener(e -> {
                userService.delete(user);
                updateList();
                var notification = Notification.show("User was deleted");
                notification.addThemeVariants(NotificationVariant.LUMO_SUCCESS);
                notification.setPosition(Notification.Position.TOP_CENTER);
            });
            return deleteButton;
        }).setHeader("Actions");
    }

    @Override
    protected HorizontalLayout getToolBar() {
        usernameFilter.getElement().setAttribute("theme", "test");
        emailFilter.getElement().setAttribute("theme", "test");

        usernameFilter.setPlaceholder("username");
        usernameFilter.setClearButtonVisible(true);
        usernameFilter.setValueChangeMode(ValueChangeMode.LAZY);

        emailFilter.setPlaceholder("email");
        emailFilter.setClearButtonVisible(true);
        emailFilter.setValueChangeMode(ValueChangeMode.LAZY);

        filterButton.addClickListener(e -> {
            getItems().setItems(userService.filter(usernameFilter.getValue(), emailFilter.getValue()));
        });

        addButton.addClickListener(e -> {
            getItems().asSingleSelect().clear();
            userForm.setEntity(new UserEntity());
            userForm.open("Add user");
        });
        HorizontalLayout toolbar = new HorizontalLayout(usernameFilter, emailFilter, filterButton, addButton);
        toolbar.addClassName("toolbar");
        return toolbar;
    }

    @Override
    protected void configureEvents() {
        userForm.addListener(UserForm.SaveEvent.class, this::saveItem);
        userForm.addListener(UserForm.CloseEvent.class, e -> closeEditor());
        userForm.addListener(UserForm.DeleteEvent.class, this::deleteItem);
    }

    @Override
    protected void closeEditor() {
        userForm.setEntity(null);
        userForm.close();
    }

    private void saveItem(UserForm.SaveEvent event) {
        userService.save(event.getItem());
        updateList();
        closeEditor();
        var notification = Notification.show("User was saved");
        notification.addThemeVariants(NotificationVariant.LUMO_SUCCESS);
        notification.setPosition(Notification.Position.TOP_CENTER);
    }

    @Override
    protected void updateList() {
        getItems().setItems(userService.loadAll());
    }

    private void deleteItem(UserForm.DeleteEvent event) {
        userService.delete(event.getItem());
        updateList();
        closeEditor();
        var notification = Notification.show("User was deleted");
        notification.addThemeVariants(NotificationVariant.LUMO_SUCCESS);
        notification.setPosition(Notification.Position.TOP_CENTER);
    }

    public TextField getUsernameFilter() {
        return usernameFilter;
    }

    public TextField getEmailFilter() {
        return emailFilter;
    }

    public UserForm getUserForm() {
        return userForm;
    }

    public Button getAddButton() {
        return addButton;
    }

    public Button getFilterButton() {
        return filterButton;
    }
}
