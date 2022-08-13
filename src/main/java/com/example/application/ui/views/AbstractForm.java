package com.example.application.ui.views;

import com.example.application.persistence.entity.AbstractEntity;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.ComponentEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.shared.Registration;


public abstract class AbstractForm<E extends AbstractEntity> extends Div {
    protected final Binder<E> binder;
    protected final Dialog    dialog;
    protected final H2        headline;
    protected final Button    saveButton;
    protected final Button    cancelButton;
    protected final Button    deleteButton;
    private         E         entity;

    public AbstractForm(Binder<E> validator) {
        this.headline = new H2();
        this.dialog = new Dialog();
        this.binder = validator;
        this.saveButton = new Button("Save");
        this.deleteButton = new Button("Delete");
        this.cancelButton = new Button("Cancel");
    }

    protected abstract void setBinder();

    protected abstract Component createDialogLayout();

    public void open(String title) {
        headline.setText(title);
        dialog.open();
    }

    public void close() {
        dialog.close();
    }

    public void setEntity(E entity) {
        this.entity = entity;
        binder.readBean(entity);
    }

    protected abstract HorizontalLayout createButtonsLayout();

    protected abstract void validateAndSave();


    public Dialog getDialog() {
        return dialog;
    }

    public E getEntity() {
        return entity;
    }

    public Button getSaveButton() {
        return saveButton;
    }

    public Button getCancelButton() {
        return cancelButton;
    }

    public Button getDeleteButton() {
        return deleteButton;
    }

    public <T extends ComponentEvent<?>> Registration addListener(Class<T> eventType,
                                                                  ComponentEventListener<T> listener) {
        return getEventBus().addListener(eventType, listener);
    }

}
