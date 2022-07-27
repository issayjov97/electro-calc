package com.example.application.ui.views;

import com.example.application.persistence.entity.PatternEntity;
import com.vaadin.flow.component.ComponentEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.shared.Registration;

import java.util.HashSet;
import java.util.Set;


public class ServiceDetailsDialog extends Div {
    private final Button              saveButton        = new Button("Confirm");
    private final Button              cancelButton      = new Button("Cancel");
    private final Dialog              dialog;
    private final Set<PatternEntity>  patterns          = new HashSet<>();
    final         Grid<PatternEntity> orderPatternsGrid = new Grid<>(PatternEntity.class);

    public ServiceDetailsDialog() {
        dialog = new Dialog();
        dialog.add(createDialogLayout());
        dialog.setSizeFull();
        dialog.setResizable(true);
        dialog.setDraggable(true);
    }

    private VerticalLayout createDialogLayout() {
        H2 headline = new H2("Patterns");
        headline.setClassName("headline");
        HorizontalLayout header = new HorizontalLayout(headline);
        saveButton.addClickListener(event -> {
            fireEvent(new ServiceDetailsDialog.SaveEvent(this, orderPatternsGrid.getSelectedItems()));
            close();
        });
        cancelButton.addClickListener(event -> {
            fireEvent(new ServiceDetailsDialog.CloseEvent(this));
        });
        orderPatternsGrid.setColumns("name", "description", "duration");
        orderPatternsGrid.addColumn(PatternEntity::getPriceWithoutVAT).setHeader("Price without VAT");
        orderPatternsGrid.setSelectionMode(Grid.SelectionMode.MULTI);
        orderPatternsGrid.setItems(patterns);
        VerticalLayout dialogLayout = new VerticalLayout(header, orderPatternsGrid, new HorizontalLayout(saveButton, cancelButton));
        dialogLayout.setPadding(false);
        dialogLayout.setAlignItems(FlexComponent.Alignment.STRETCH);
        dialogLayout.getStyle()
                .set("min-width", "700px")
                .set("max-width", "100%")
                .set("height", "100%");
        orderPatternsGrid.addSelectionListener(selection -> {
            System.out.printf("Number of selected items: %s%n", selection.getAllSelectedItems().size());
        });
        return dialogLayout;
    }

    public void open() {
        dialog.open();
    }

    public void close() {
        dialog.close();
    }

    public void setPatterns(Set<PatternEntity> patterns) {
        this.patterns.addAll(patterns);
    }

    public void clearPatterns() {
        patterns.clear();
        orderPatternsGrid.asMultiSelect().clear();
    }


    public static abstract class OrderDetailsDialogEvent extends ComponentEvent<ServiceDetailsDialog> {
        private final Set<PatternEntity> patterns;

        protected OrderDetailsDialogEvent(ServiceDetailsDialog source, Set<PatternEntity> patterns) {
            super(source, false);
            this.patterns = patterns;
        }

        public Set<PatternEntity> getPatterns() {
            return patterns;
        }
    }

    public static class SaveEvent extends OrderDetailsDialogEvent {
        SaveEvent(ServiceDetailsDialog source, Set<PatternEntity> patterns) {
            super(source, patterns);
        }
    }

    public static class CloseEvent extends OrderDetailsDialogEvent {
        CloseEvent(ServiceDetailsDialog source) {
            super(source, null);
        }
    }

    public <T extends ComponentEvent<?>> Registration addListener(Class<T> eventType,
                                                                  ComponentEventListener<T> listener) {
        return getEventBus().addListener(eventType, listener);
    }

    public Button getSaveButton() {
        return saveButton;
    }

    public Grid<PatternEntity> getOrderPatternsGrid() {
        return orderPatternsGrid;
    }

    public Dialog getDialog() {
        return dialog;
    }

    public Set<PatternEntity> getPatterns() {
        return patterns;
    }


}
