package com.example.application.ui.views.order;

import com.example.application.dto.OrderDTO;
import com.vaadin.flow.component.ComponentEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.BigDecimalField;
import com.vaadin.flow.component.textfield.NumberField;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.binder.ValidationException;
import com.vaadin.flow.shared.Registration;


public class OrderForm extends Div {
    Binder<OrderDTO> binder = new BeanValidationBinder<>(OrderDTO.class);
    private final Dialog          dialog             = new Dialog();
    private H2 headline = new H2();
    private       BigDecimalField materialsCost      = new BigDecimalField("Materials cost");
    private final BigDecimalField transportationCost = new BigDecimalField("Transportation cost");
    private final NumberField     workHours          = new NumberField("Work hours");
    private       Button          saveButton         = new Button("Save");
    private       Button          cancelButton       = new Button("Cancel");
    private       Button          deleteButton       = new Button("Delete");
    private       OrderDTO        orderDTO;

    public OrderForm() {
        setBinder();
        dialog.getElement().setAttribute("aria-label", "Create new pattern");
        dialog.add(createDialogLayout());
        materialsCost.setWidthFull();
        transportationCost.setWidthFull();
        workHours.setWidthFull();
    }

    private void setBinder() {
        binder.forField(materialsCost).asRequired("Materials cost is required")
                .bind(OrderDTO::getMaterialsCost, OrderDTO::setMaterialsCost);
        binder.forField(transportationCost).asRequired("Transportation cost is required")
                .bind(OrderDTO::getTransportationCost, OrderDTO::setTransportationCost);
        binder.forField(workHours).asRequired("Work hours is reuired")
                .bind(OrderDTO::getWorkHours, OrderDTO::setWorkHours);
    }

    private VerticalLayout createDialogLayout() {
        VerticalLayout fieldLayout = new VerticalLayout(materialsCost, transportationCost, workHours);
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

    public void open(String title) {
        headline.setText(title);
        dialog.open();
    }

    public void close() {
        dialog.close();
    }

    public void setOrderDTO(OrderDTO orderDTO) {
        this.orderDTO = orderDTO;
        binder.readBean(orderDTO);
    }

    private HorizontalLayout createButtonsLayout() {
        saveButton.addClickListener(event -> validateAndSave());
        deleteButton.addClickListener(event -> fireEvent(new DeleteEvent(this, orderDTO)));
        cancelButton.addClickListener(event -> fireEvent(new CloseEvent(this)));
        return new HorizontalLayout(saveButton,deleteButton, cancelButton);
    }

    private void validateAndSave() {
        try {
            binder.writeBean(orderDTO);
            fireEvent(new SaveEvent(this, orderDTO));
        } catch (ValidationException e) {
            e.printStackTrace();
        }
    }


    public static abstract class OrderFormEvent extends ComponentEvent<OrderForm> {
        private OrderDTO item;

        protected OrderFormEvent(OrderForm source, OrderDTO item) {
            super(source, false);
            this.item = item;
        }

        public OrderDTO getItem() {
            return item;
        }
    }

    public static class SaveEvent extends OrderFormEvent {
        SaveEvent(OrderForm source, OrderDTO contact) {
            super(source, contact);
        }
    }

    public static class DeleteEvent extends OrderFormEvent {
        DeleteEvent(OrderForm source, OrderDTO contact) {
            super(source, contact);
        }
    }

    public static class CloseEvent extends OrderFormEvent {
        CloseEvent(OrderForm source) {
            super(source, null);
        }
    }

    public <T extends ComponentEvent<?>> Registration addListener(Class<T> eventType,
                                                                  ComponentEventListener<T> listener) {
        return getEventBus().addListener(eventType, listener);
    }
}
