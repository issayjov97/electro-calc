package com.example.application.ui.views.customer;

import com.example.application.persistence.entity.CustomerEntity;
import com.example.application.service.CustomerService;
import com.example.application.service.UserService;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.data.provider.ListDataProvider;
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
class CustomersViewTest {

    private final UI ui = new UI();
    @Autowired
    private CustomerService customerService;
    @Autowired
    private UserService userService;
    private CustomersView customersView;

    @BeforeEach
    void init() {
        UI.setCurrent(ui);
        this.customersView = new CustomersView(customerService, userService);
    }

    @Test
    @WithMockUser(username = TESTER)
    void formShownWhenCustomerSelected() {
        Grid<CustomerEntity> grid = customersView.getItems();
        CustomerEntity customerEntity = getFirstCustomer(grid);

        CustomerForm form = customersView.getCustomerForm();

        assertFalse(customersView.getCustomerForm().getDialog().isOpened());
        grid.asSingleSelect().setValue(customerEntity);

        assertTrue(customersView.getCustomerForm().getDialog().isOpened());
        assertEquals(customerEntity.getName(), form.getNameField().getValue());
    }

    @Test
    @WithMockUser(username = TESTER)
    void formShownWhenAddCustomerClicked() {
        CustomerForm form = customersView.getCustomerForm();

        assertFalse(customersView.getCustomerForm().getDialog().isOpened());
        customersView.getAddButton().click();

        assertTrue(customersView.getCustomerForm().getDialog().isOpened());
        assertNotNull(form.getEntity());
        assertEquals("", form.getNameField().getValue());
        assertEquals("", form.getEmailField().getValue());
        assertEquals("", form.getPhoneField().getValue());
    }

    @Test
    @WithMockUser(username = TESTER)
    void filteredGridShownWhenFilterClicked() {
        Grid<CustomerEntity> grid = customersView.getItems();
        var customers = grid.getDataProvider().fetch(new Query<>()).collect(Collectors.toList());

        assertEquals(customers.size(), (int) grid.getDataProvider().fetch(new Query<>()).count());
        customersView.getNameFilter().setValue("tester");
        customersView.getFilterButton().click();

        assertEquals(
                customers.stream().filter(customerEntity -> customerEntity.getName().contains("tester")).count(),
                (int) grid.getDataProvider().fetch(new Query<>()).count()
        );
    }

    private CustomerEntity getFirstCustomer(Grid<CustomerEntity> grid) {
        return grid.getDataProvider().fetch(new Query<>()).findFirst().orElse(null);
    }

}