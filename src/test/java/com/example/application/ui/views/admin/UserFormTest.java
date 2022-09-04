package com.example.application.ui.views.admin;

import com.example.application.persistence.entity.AuthorityEntity;
import com.example.application.persistence.entity.UserEntity;
import com.example.application.ui.views.settings.admin.user.UserForm;
import com.example.application.ui.views.settings.admin.user.events.SaveEvent;
import com.example.application.ui.views.settings.admin.user.events.DeleteEvent;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

import static org.junit.jupiter.api.Assertions.assertEquals;

class UserFormTest {
    final String firstname = "tester123";
    final String lastname = "tester123";
    final String email = "tester123@tester.cz";
    final String password = "tester1997";
    final String username = "tester123";
    final LocalDateTime createdAt = LocalDateTime.now();

    @Test
    public void formFieldsPopulated() {
        UserForm userForm = new UserForm(null);
        UserEntity user = getUser();

        userForm.setEntity(user);

        assertEquals(firstname, userForm.getFirstName().getValue());
        assertEquals(lastname, userForm.getLastName().getValue());
        assertEquals(email, userForm.getEmail().getValue());
        assertEquals(username, userForm.getUsername().getValue());
        assertEquals(password, userForm.getPassword().getValue());
        assertEquals(true, userForm.getEnabled().getValue());
    }

    @Test
    public void saveEventHasCorrectValues() {
        UserForm userForm = new UserForm(null);
        UserEntity user = new UserEntity();
        List<AuthorityEntity> authorities = List.of(new AuthorityEntity());
        userForm.setEntity(user);
        userForm.getFirstName().setValue(firstname);
        userForm.getLastName().setValue(lastname);
        userForm.getEmail().setValue(email);
        userForm.getPassword().setValue(password);
        userForm.getUsername().setValue(username);
        userForm.getEnabled().setEnabled(true);
        userForm.getAllAuthorities().addAll(authorities);

        AtomicReference<UserEntity> savedContactRef = new AtomicReference<>(null);
        userForm.addListener(SaveEvent.class, e -> {
            savedContactRef.set(e.getItem());
        });
        userForm.getSaveButton().click();
        userForm.getAuthorities().select(authorities);

        UserEntity savedUserEntity = savedContactRef.get();

        assertEquals(firstname, savedUserEntity.getFirstName());
        assertEquals(lastname, savedUserEntity.getLastName());
        assertEquals(email, savedUserEntity.getEmail());
        assertEquals(password, savedUserEntity.getPassword());
        assertEquals(username, savedUserEntity.getUsername());
        assertEquals(true, savedUserEntity.getEnabled());
        assertEquals(authorities.size(), savedUserEntity.getAuthorityEntities().size());
    }

    @Test
    public void deleteEventHasCorrectValues() {
        UserForm userForm = new UserForm(null);
        UserEntity user = getUser();

        AtomicReference<UserEntity> deleteUserRef = new AtomicReference<>(null);
        userForm.addListener(DeleteEvent.class, e -> {
            deleteUserRef.set(e.getItem());
        });
        userForm.setEntity(user);
        userForm.getDeleteButton().click();

        UserEntity deletedUserEntity = deleteUserRef.get();

        assertEquals(firstname, deletedUserEntity.getFirstName());
        assertEquals(lastname, deletedUserEntity.getLastName());
        assertEquals(email, deletedUserEntity.getEmail());
        assertEquals(password, deletedUserEntity.getPassword());
        assertEquals(true, deletedUserEntity.getEnabled());
        assertEquals(username, deletedUserEntity.getUsername());
        assertEquals(createdAt, deletedUserEntity.getCreatedAt());
    }

    private UserEntity getUser(){
        UserEntity user = new UserEntity();
        user.setCreatedAt(createdAt);
        user.setUsername(username);
        user.setFirstName(firstname);
        user.setLastName(lastname);
        user.setPassword(password);
        user.setEmail(email);
        user.setEnabled(true);
        user.setAuthorityEntities(Collections.emptySet());
        return  user;
    }
}