package com.example.application.ui.views.jobOrder;

import com.example.application.persistence.entity.JobOrderEntity;
import com.example.application.ui.events.CloseEvent;
import com.example.application.ui.views.AbstractForm;
import com.example.application.ui.views.jobOrder.events.DeleteEvent;
import com.example.application.ui.views.jobOrder.events.SaveEvent;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.ValidationException;


public class JobOrderForm extends AbstractForm<JobOrderEntity> {
    private final TextField  name       = new TextField("Name");
    private final DatePicker demandDate = new DatePicker("Datum of demand");
    private final DatePicker offerDate  = new DatePicker("Date of offer");
    private final DatePicker orderDate  = new DatePicker("Date of order");


    public JobOrderForm() {
        super(new BeanValidationBinder<>(JobOrderEntity.class));
        setBinder();
        dialog.add(createDialogLayout());
    }

    @Override
    protected void setBinder() {
        binder.forField(name).asRequired("Name cost is required")
                .withValidator(name -> name.length() > 4, "Name must contain at least 6 characters ")
                .bind(JobOrderEntity::getJobName, JobOrderEntity::setJobName);
        binder.bind(demandDate, "demandDate");
        binder.bind(offerDate, "offerDate");
        binder.bind(orderDate, "orderDate");
    }

    @Override
    protected VerticalLayout createDialogLayout() {
        VerticalLayout fieldLayout = new VerticalLayout(name, demandDate, offerDate, orderDate);
        fieldLayout.setSpacing(false);
        fieldLayout.setPadding(false);
        fieldLayout.setAlignItems(FlexComponent.Alignment.STRETCH);
        HorizontalLayout buttonLayout = createButtonsLayout();
        buttonLayout.setJustifyContentMode(FlexComponent.JustifyContentMode.END);
        VerticalLayout dialogLayout = new VerticalLayout(headline, fieldLayout, buttonLayout);
        dialogLayout.setPadding(false);
        dialogLayout.setAlignItems(FlexComponent.Alignment.STRETCH);
        dialogLayout.getStyle().set("width", "300px").set("max-width", "100%");
        return dialogLayout;
    }

    @Override
    protected HorizontalLayout createButtonsLayout() {
        saveButton.addClickListener(event -> validateAndSave());
        deleteButton.addClickListener(event -> fireEvent(new DeleteEvent(this, getEntity())));
        cancelButton.addClickListener(event -> fireEvent(new CloseEvent(this, false)));
        return new HorizontalLayout(saveButton, deleteButton, cancelButton);
    }

    @Override
    protected void validateAndSave() {
        try {
            binder.writeBean(getEntity());
            fireEvent(new SaveEvent(this, getEntity()));
        } catch (ValidationException e) {
            e.printStackTrace();
        }
    }
}
