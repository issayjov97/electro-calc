package com.example.application.ui.views.admin;

import com.example.application.dto.UserDTO;
import com.example.application.service.UserService;
import com.example.application.ui.views.settings.SettingsView;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.NumberField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import org.springframework.security.access.annotation.Secured;

@PageTitle("Users")
@Secured("ADMIN")
@Route(value = "users", layout = SettingsView.class)
public class UsersView extends VerticalLayout {
    private final Grid<UserDTO> itemsGrid   = new Grid(UserDTO.class);
    private final TextField     nameFilter  = new TextField();
    private final NumberField   emailFilter = new NumberField();
    private final UserService   userService;
    private final UserForm      userForm    = new UserForm();

    public UsersView(UserService userService) {
        this.userService = userService;
        userForm.setAllAuthorities(userService.getMappedAuthorities());
        addClassName("Patterns-view");
        setSizeFull();
        configureGrid();
        add(getToolBar(), getContent(), userForm);
        configureEvents();
    }

    private Component getContent() {
        HorizontalLayout content = new HorizontalLayout(itemsGrid);
        content.addClassNames("content");
        content.setSizeFull();
        return content;
    }

    private void configureGrid() {
        itemsGrid.addClassName("items-grid");
        itemsGrid.setColumns("username", "firstName", "lastName", "email", "enabled", "createdAt","authorities");
        itemsGrid.getColumns().forEach(itemColumn -> itemColumn.setAutoWidth(true));
        itemsGrid.setItems(userService.getAll());
        itemsGrid.asSingleSelect().addValueChangeListener(event -> {
            if (event.getValue() != null) {
                userForm.setUserDTO(event.getValue());
                userForm.open("Edit user");
            }
        });
        itemsGrid.addComponentColumn((user) -> {
            Button deleteButton = new Button("Delete");
            deleteButton.addClickListener(e -> {
                userService.deleteUser(user.getId());
                updateList();
                var notification = Notification.show("User was deleted");
                notification.addThemeVariants(NotificationVariant.LUMO_SUCCESS);
                notification.setPosition(Notification.Position.TOP_CENTER);
            });
            return deleteButton;
        }).setHeader("Actions");
    }

    private HorizontalLayout getToolBar() {
        nameFilter.getElement().setAttribute("theme", "test");
        emailFilter.getElement().setAttribute("theme", "test");

        nameFilter.setPlaceholder("username");
        nameFilter.setClearButtonVisible(true);
        nameFilter.setValueChangeMode(ValueChangeMode.LAZY);

        emailFilter.setPlaceholder("email");
        emailFilter.setClearButtonVisible(true);
        emailFilter.setValueChangeMode(ValueChangeMode.LAZY);

        Button filterButton = new Button("Filter");

        filterButton.addClickListener(e -> {
            //itemsGrid.setItems(userService.getAll().filter(nameFilter.getValue(), priceFilter.getValue(), durationFilter.getValue()));
        });

        Button addPattern = new Button("Add user");

        addPattern.addClickListener(e -> {
            itemsGrid.asSingleSelect().clear();
            userForm.setUserDTO(new UserDTO());
            userForm.open("Add user");
        });
        HorizontalLayout toolbar = new HorizontalLayout(nameFilter, emailFilter, filterButton, addPattern);
        toolbar.addClassName("toolbar");
        return toolbar;
    }

    private void configureEvents() {
        userForm.addListener(UserForm.SaveEvent.class, this::saveItem);
        userForm.addListener(UserForm.CloseEvent.class, e -> closeEditor());
        userForm.addListener(UserForm.DeleteEvent.class, this::deleteItem);
    }

    private void closeEditor() {
        userForm.setUserDTO(null);
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

    private void updateList() {
        itemsGrid.setItems(userService.getAll());
    }

    private void deleteItem(UserForm.DeleteEvent event) {
        userService.deletePattern(event.getItem());
        updateList();
        closeEditor();
        var notification = Notification.show("User was deleted");
        notification.addThemeVariants(NotificationVariant.LUMO_SUCCESS);
        notification.setPosition(Notification.Position.TOP_CENTER);
    }
}
