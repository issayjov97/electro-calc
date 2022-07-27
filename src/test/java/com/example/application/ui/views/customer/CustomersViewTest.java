package com.example.application.ui.views.customer;

import com.example.application.persistence.entity.CustomerEntity;
import com.example.application.service.CustomerService;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.data.provider.ListDataProvider;
import com.vaadin.flow.data.provider.Query;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
class CustomersViewTest {

    private final UI              ui = new UI();
    @Autowired
    private       CustomerService customerService;
    private       CustomersView   customersView;

    @BeforeEach
    void init() {
        UI.setCurrent(ui);
        this.customersView = new CustomersView(customerService);
    }

    @Test
    @WithMockUser(username = "tester123")
    public void formShownWhenUserSelected() {
        Grid<CustomerEntity> grid = customersView.getItems();
        CustomerEntity customerEntity = getFirstCustomer(grid);

        CustomerForm form = customersView.getCustomerForm();

        assertFalse(customersView.getCustomerForm().getDialog().isOpened());
        grid.asSingleSelect().setValue(customerEntity);
        assertTrue(customersView.getCustomerForm().getDialog().isOpened());
        assertEquals(customerEntity.getName(), form.getNameField().getValue());
    }

    @Test
    @WithMockUser(username = "tester123")
    public void formShownWhenAddUserClicked() {
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
    @WithMockUser(username = "tester123")
    public void filteredGridShownWhenFilterClicked() {
        Grid<CustomerEntity> grid = customersView.getItems();

        customersView.getNameFilter().setValue("tester");

        assertEquals(2, (int) grid.getDataProvider().fetch(new Query<>()).count());
        customersView.getFilterButton().click();

        assertEquals(1, (int) grid.getDataProvider().fetch(new Query<>()).count());
    }

    private CustomerEntity getFirstCustomer(Grid<CustomerEntity> grid) {
        return ((ListDataProvider<CustomerEntity>) grid.getDataProvider()).getItems().iterator().next();
    }

}