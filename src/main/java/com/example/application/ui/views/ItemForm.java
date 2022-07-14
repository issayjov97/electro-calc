package com.example.application.ui.views;

import com.example.application.domain.Item;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.BigDecimalField;
import com.vaadin.flow.component.textfield.IntegerField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.binder.ValidationException;

import java.util.List;

public class ItemForm extends FormLayout {
    Binder<Item> binder = new BeanValidationBinder<>(Item.class);
    private Item item;

    TextField name = new TextField("Name");
    BigDecimalField price = new BigDecimalField("Price");

    IntegerField count = new IntegerField("Count");

    ComboBox<String> status = new ComboBox<>("Type");

    Button save = new Button("Save");
    Button delete = new Button("Delete");
    Button close = new Button("Cancel");

    public ItemForm(List<String> statuses) {
        addClassName("contact-form");
        binder.bindInstanceFields(this);
        status.setItems(statuses);
        status.setItemLabelGenerator(String::toString);

        add(name, price, count, status, createButtonsLayout());

    }

    private HorizontalLayout createButtonsLayout() {
        save.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        delete.addThemeVariants(ButtonVariant.LUMO_ERROR);
        close.addThemeVariants(ButtonVariant.LUMO_TERTIARY);

//        save.addClickShortcut(Key.ENTER);
        close.addClickShortcut(Key.ESCAPE);

        save.addClickListener(event -> {
            try {
                if (item == null)
                    item = new Item();
                binder.writeBean(item);
            } catch (ValidationException e) {
                e.printStackTrace();
            }
            System.out.println("Selected item:");
            System.out.println(item);
        });
        // binder.addStatusChangeListener(e -> save.setEnabled(binder.isValid()));

        return new HorizontalLayout(save, delete, close);
    }

    public void setItem(Item item) {
        this.item = item;
        binder.readBean(item);
    }

    private void validateAndSave() {
        try {
            binder.writeBean(item);
        } catch (ValidationException e) {
            e.printStackTrace();
        }
    }
}