package com.example.application.ui.views.offer;

import com.example.application.model.enums.OrderStatus;
import com.example.application.persistence.entity.CustomerEntity;
import com.example.application.persistence.entity.OfferEntity;
import com.example.application.service.CustomerService;
import com.example.application.ui.views.AbstractForm;
import com.example.application.ui.views.offer.events.SaveOfferDetailsEvent;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.NumberField;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.ValidationException;


@CssImport(value = "./views/menu-bar.css", themeFor = "vaadin-menu-bar")
public class OfferDetailsForm extends AbstractForm<OfferEntity> {
    private final TextField name = new TextField("Název");
    private final TextArea description = new TextArea("Popis");
    private final NumberField distance = new NumberField("Vzdalenost");
    private final TextArea note = new TextArea("Poznamka");
    private final ComboBox<OrderStatus> statuses = new ComboBox<>("Status nabídky", OrderStatus.values());
    private final ComboBox<CustomerEntity> customersSelect = new ComboBox<>("Zákazník");
    private final CustomerService customerService;

    public OfferDetailsForm(CustomerService customerService) {
        super(new BeanValidationBinder<>(OfferEntity.class));
        this.customerService = customerService;
        setBinder();
        configureSelect();
        VerticalLayout verticalLayout = new VerticalLayout(createDialogLayout(), createButtonsLayout());
        verticalLayout.setAlignItems(FlexComponent.Alignment.CENTER);
        add(verticalLayout);
    }

    @Override
    protected void setBinder() {
        binder.forField(name).withValidator(e -> e.length() > 2, "Název je povinný")
                .bind(OfferEntity::getName, OfferEntity::setName);
        binder.forField(description).bind(OfferEntity::getDescription, OfferEntity::setDescription);
        binder.forField(distance).bind(OfferEntity::getDistance, OfferEntity::setDistance);
        binder.forField(customersSelect)
                .bind(OfferEntity::getCustomerEntity, OfferEntity::setCustomerEntity);
        binder.forField(note)
                .bind(OfferEntity::getNote, OfferEntity::setNote);
        binder.forField(statuses)
                .bind(OfferEntity::getStatus, OfferEntity::setStatus);
    }

    @Override
    protected Component createDialogLayout() {
        FormLayout formLayout = new FormLayout();
        formLayout.setResponsiveSteps(
                new FormLayout.ResponsiveStep("0", 1),
                new FormLayout.ResponsiveStep("200px", 2)
        );
        formLayout.add(name, distance, customersSelect, statuses, description, note);
        return formLayout;
    }


    @Override
    protected HorizontalLayout createButtonsLayout() {
        saveButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        deleteButton.addThemeVariants(ButtonVariant.LUMO_ERROR);
        cancelButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
        saveButton.addClickListener(event -> validateAndSave());
        HorizontalLayout horizontalLayout = new HorizontalLayout(saveButton);
        horizontalLayout.setWidthFull();
        saveButton.setSizeFull();
        return horizontalLayout;
    }

    private void configureSelect() {
        statuses.setClearButtonVisible(true);
        customersSelect.addClassName("label");
        customersSelect.setClearButtonVisible(true);
        customersSelect.setItemLabelGenerator(CustomerEntity::getName);
        customersSelect.setItems(customerService.getFirmCustomers());
    }

    @Override
    protected void validateAndSave() {
        try {
            binder.writeBean(getEntity());
            fireEvent(new SaveOfferDetailsEvent(this, getEntity()));
        } catch (ValidationException e) {
            e.printStackTrace();
        }
    }
}
