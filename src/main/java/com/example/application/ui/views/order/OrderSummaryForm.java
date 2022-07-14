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


public class OrderSummaryForm extends Div {
    private final Dialog          dialog             = new Dialog();
    private       BigDecimalField materialsCost      = new BigDecimalField("Materials cost");
    private final BigDecimalField priceWithoutVat    = new BigDecimalField("Price without VAT");
    private final BigDecimalField totalPrice         = new BigDecimalField("Total price");
    private final BigDecimalField transportationCost = new BigDecimalField("Transportation cost");
    private final NumberField     workHours          = new NumberField("Work hours");
    private       Button          saveButton         = new Button("Save");
    private       Button          cancelButton       = new Button("Cancel");
    private       Button          deleteButton       = new Button("Delete");
    private       OrderDTO        orderDTO;

    public OrderSummaryForm() {
        dialog.getElement().setAttribute("aria-label", "Create new pattern");
        dialog.add(createDialogLayout());
    }

    private VerticalLayout createDialogLayout() {
        H2 headline = new H2("New order");
        VerticalLayout fieldLayout = new VerticalLayout(materialsCost, priceWithoutVat, totalPrice, transportationCost, workHours);
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

    public void open() {
        dialog.open();
    }

    public void close() {
        dialog.close();
    }

    public void setOrderDTO(OrderDTO orderDTO) {
        this.orderDTO = orderDTO;
    }

    private HorizontalLayout createButtonsLayout() {
        saveButton.addClickListener(event -> validateAndSave());
        deleteButton.addClickListener(event -> fireEvent(new DeleteEvent(this, orderDTO)));
        cancelButton.addClickListener(event -> fireEvent(new CloseEvent(this)));
        return new HorizontalLayout(saveButton,deleteButton, cancelButton);
    }

    private void validateAndSave() {
    }


    public static abstract class OrderFormEvent extends ComponentEvent<OrderSummaryForm> {
        private OrderDTO item;

        protected OrderFormEvent(OrderSummaryForm source, OrderDTO item) {
            super(source, false);
            this.item = item;
        }

        public OrderDTO getItem() {
            return item;
        }
    }

    public static class SaveEvent extends OrderFormEvent {
        SaveEvent(OrderSummaryForm source, OrderDTO contact) {
            super(source, contact);
        }
    }

    public static class DeleteEvent extends OrderFormEvent {
        DeleteEvent(OrderSummaryForm source, OrderDTO contact) {
            super(source, contact);
        }
    }

    public static class CloseEvent extends OrderFormEvent {
        CloseEvent(OrderSummaryForm source) {
            super(source, null);
        }
    }

    public <T extends ComponentEvent<?>> Registration addListener(Class<T> eventType,
                                                                  ComponentEventListener<T> listener) {
        return getEventBus().addListener(eventType, listener);
    }
}
