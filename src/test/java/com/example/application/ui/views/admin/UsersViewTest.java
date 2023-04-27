package com.example.application.ui.views.admin;

import com.example.application.persistence.entity.UserEntity;
import com.example.application.service.AuthorityService;
import com.example.application.service.FirmService;
import com.example.application.service.UserService;
import com.example.application.ui.views.settings.admin.user.UserForm;
import com.example.application.ui.views.settings.admin.user.UsersView;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.data.provider.Query;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.transaction.annotation.Transactional;

import java.util.stream.Collectors;

import static com.example.application.Values.TESTER;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@Transactional
class UsersViewTest {

    private final UI ui = new UI();
    @Autowired
    private UserService userService;
    @Autowired
    private AuthorityService authorityService;
    @Autowired
    private FirmService firmService;

    private UsersView usersView;

    @BeforeEach
    void init() {
        UI.setCurrent(ui);
        this.usersView = new UsersView(userService, authorityService, firmService);
    }

    @Test
    @WithMockUser(username = TESTER)
    void formShownWhenUserSelected() {
        Grid<UserEntity> grid = usersView.getItems();
        UserEntity userEntity = getFirstUser(grid);

        UserForm form = usersView.getUserForm();

        assertFalse(usersView.getUserForm().getDialog().isOpened());
        grid.asSingleSelect().setValue(userEntity);
        assertTrue(usersView.getUserForm().getDialog().isOpened());
        assertEquals(userEntity.getUsername(), form.getUsername().getValue());
    }

    @Test
    @WithMockUser(username = TESTER)
    void formShownWhenAddUserClicked() {
        UserForm form = usersView.getUserForm();

        assertFalse(usersView.getUserForm().getDialog().isOpened());
        usersView.getAddButton().click();

        assertTrue(usersView.getUserForm().getDialog().isOpened());
        assertNotNull(form.getEntity());
        assertEquals("", form.getUsername().getValue());
        assertEquals("", form.getPassword().getValue());
        assertEquals("", form.getEmail().getValue());
        assertEquals("", form.getFirstName().getValue());
        assertEquals("", form.getLastName().getValue());
        assertEquals(true, form.getEnabled().getValue());
        assertEquals(0, form.getAuthoritiesSelect().getSelectedItems().size());
    }

    private UserEntity getFirstUser(Grid<UserEntity> grid) {
        return grid.getDataProvider().fetch(new Query<>()).findFirst().orElse(null);
    }
}