package com.example.application.ui.views.customer;

import com.example.application.persistence.entity.CustomerEntity;
import com.example.application.ui.views.customer.events.DeleteEvent;
import com.example.application.ui.views.customer.events.SaveEvent;
import org.junit.jupiter.api.Test;

import java.util.concurrent.atomic.AtomicReference;

import static org.junit.jupiter.api.Assertions.assertEquals;

class CustomerFormTest {
    private final String name  = "test_customer123";
    private final String phone = "779654345";
    private final String email = "tester123@tester.cz";

    @Test
    void formFieldsPopulated() {
        CustomerForm customerForm = new CustomerForm();
        CustomerEntity customerEntity = getCustomer();

        customerForm.setEntity(customerEntity);

        assertEquals(name, customerForm.getNameField().getValue());
        assertEquals(email, customerForm.getEmailField().getValue());
        assertEquals(phone, customerForm.getPhoneField().getValue());
    }

    @Test
    void saveEventHasCorrectValues() {
        CustomerForm customerForm = new CustomerForm();
        CustomerEntity customerEntity = new CustomerEntity();
        customerForm.setEntity(customerEntity);
        customerForm.getNameField().setValue(name);
        customerForm.getEmailField().setValue(email);
        customerForm.getPhoneField().setValue(phone);

        AtomicReference<CustomerEntity> savedContactRef = new AtomicReference<>(null);
        customerForm.addListener(SaveEvent.class, e -> {
            savedContactRef.set(e.getItem());
        });
        customerForm.getSaveButton().click();

        CustomerEntity savedCustomerEntity = savedContactRef.get();

        assertEquals(name, savedCustomerEntity.getName());
        assertEquals(email, savedCustomerEntity.getEmail());
        assertEquals(phone, savedCustomerEntity.getPhone());
    }

    @Test
    void deleteEventHasCorrectValues() {
        CustomerForm customerForm = new CustomerForm();
        CustomerEntity customerEntity = getCustomer();
        customerForm.setEntity(customerEntity);

        AtomicReference<CustomerEntity> deleteUserRef = new AtomicReference<>(null);
        customerForm.addListener(DeleteEvent.class, e -> {
            deleteUserRef.set(e.getItem());
        });

        customerForm.getDeleteButton().click();
        CustomerEntity deletedCustomerEntity = deleteUserRef.get();

        assertEquals(name, deletedCustomerEntity.getName());
        assertEquals(email, deletedCustomerEntity.getEmail());
        assertEquals(phone, deletedCustomerEntity.getPhone());
    }

    private CustomerEntity getCustomer() {
        CustomerEntity customerEntity = new CustomerEntity();
        customerEntity.setName(name);
        customerEntity.setEmail(email);
        customerEntity.setPhone(phone);
        return customerEntity;
    }

}