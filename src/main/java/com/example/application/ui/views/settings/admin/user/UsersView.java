package com.example.application.ui.views.settings.admin.user;

import com.example.application.persistence.entity.AuthorityEntity;
import com.example.application.persistence.entity.UserEntity;
import com.example.application.service.AuthorityService;
import com.example.application.service.FirmService;
import com.example.application.service.UserService;
import com.example.application.ui.components.NotificationService;
import com.example.application.ui.events.CloseEvent;
import com.example.application.ui.views.AbstractServicesView;
import com.example.application.ui.views.settings.SettingsView;
import com.example.application.ui.views.settings.admin.user.events.DeleteEvent;
import com.example.application.ui.views.settings.admin.user.events.SaveEvent;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import org.springframework.security.access.annotation.Secured;

import java.util.stream.Collectors;

@PageTitle("Users")
@Secured("ADMIN")
@Route(value = "users", layout = SettingsView.class)
public class UsersView extends AbstractServicesView<UserEntity, UserEntity> {
    private final TextField   usernameFilter = new TextField();
    private final UserForm    userForm;
    private final Button      addButton      = new Button("Přidat uživatele");
    private final UserService userService;

    public UsersView(UserService userService, AuthorityService authorityService, FirmService firmService) {
        super(new  Grid<>(), userService);
        this.userService = userService;
        this.userForm = new UserForm(firmService);
        userForm.setAllAuthorities(authorityService.loadAll());
        setSizeFull();
        configureGrid();
        configureEvents();
        add(getToolBar(), getContent(), userForm);
    }

    @Override
    protected void configureForm() {

    }

    @Override
    protected void configureGrid() {
        getItems().addClassName("items-grid");
        getItems().addColumn(UserEntity::getUsername).setHeader("Uživatelské jméno");
        getItems().addColumn(UserEntity::getFirstName).setHeader("Jméno");
        getItems().addColumn(UserEntity::getLastName).setHeader("Přijmení");
        getItems().addColumn(UserEntity::getEmail).setHeader("Email");
        getItems().addColumn(UserEntity::getEnabled).setHeader("Povoleno");
        getItems().addColumn(UserEntity::getCreatedAt).setHeader("Vytvořen");
        getItems().getColumns().forEach(itemColumn -> itemColumn.setAutoWidth(true));
        getItems().setItems(userService.loadAll());
        getItems().asSingleSelect().addValueChangeListener(event -> {
            if (event.getValue() != null) {
                userForm.setEntity(event.getValue());
                userForm.open("Editace uživatele");
            }
        });
        getItems().addColumn(e -> e.getAuthorityEntities().stream().map(AuthorityEntity::getName).collect(Collectors.joining(","))).setHeader("Role");
    }

    @Override
    protected HorizontalLayout getToolBar() {
        addButton.setIcon(VaadinIcon.PLUS.create());

        addButton.addClickListener(e -> {
            getItems().asSingleSelect().clear();
            userForm.setEntity(new UserEntity());
            userForm.open("Přidat uživatele");
        });
        HorizontalLayout toolbar = new HorizontalLayout(addButton);
        toolbar.addClassName("filterPart");
        return toolbar;
    }

    @Override
    protected void configureEvents() {
        userForm.addListener(SaveEvent.class, this::saveItem);
        userForm.addListener(CloseEvent.class, e -> closeEditor());
        userForm.addListener(DeleteEvent.class, this::deleteItem);
    }

    @Override
    protected void closeEditor() {
        userForm.setEntity(null);
        userForm.close();
    }

    private void saveItem(SaveEvent event) {
        userService.save(event.getItem());
        updateList();
        closeEditor();
        NotificationService.success();
    }

    @Override
    protected void updateList() {
        getItems().setItems(userService.loadAll());
    }

    private void deleteItem(DeleteEvent event) {
        userService.delete(event.getItem());
        updateList();
        closeEditor();
        NotificationService.success();
    }

    public TextField getUsernameFilter() {
        return usernameFilter;
    }


    public UserForm getUserForm() {
        return userForm;
    }

    public Button getAddButton() {
        return addButton;
    }
}
