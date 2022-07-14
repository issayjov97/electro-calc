package com.example.application.ui.views.order;

import com.example.application.dto.PatternDTO;
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


public class OrderDetailsDialog extends Div {
    private Button           saveButton   = new Button("Confirm");
    private Button           cancelButton      = new Button("Cancel");
    final   Grid<PatternDTO> orderPatternsGrid = new Grid(PatternDTO.class);
    private Dialog           dialog;
    private VerticalLayout   dialogLayout = new VerticalLayout();
    private Set<PatternDTO>  patterns     = new HashSet<>();

    public OrderDetailsDialog() {
        dialog = new Dialog();
        dialog.add(createDialogLayout());
        dialog.setSizeFull();
        dialog.setResizable(true);
        dialog.setDraggable(true);
        dialog.getElement().setAttribute("aria-label", "Order patterns");
    }

    private VerticalLayout createDialogLayout() {
        H2 headline = new H2("Patterns");
        headline.getStyle()
                .set("margin", "var(--lumo-space-m) 0 0 0")
                .set("font-size", "1.5em")
                .set("font-weight", "bold");
        HorizontalLayout header = new HorizontalLayout(headline);
        saveButton.addClickListener(event -> {
            fireEvent(new OrderDetailsDialog.SaveEvent(this, orderPatternsGrid.getSelectedItems()));
        });
        cancelButton.addClickListener(event -> {
            fireEvent(new OrderDetailsDialog.CloseEvent(this));
        });
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

    private HorizontalLayout createButtonsLayout() {
        return new HorizontalLayout(saveButton, cancelButton);
    }

    public void open() {
        dialog.open();
    }

    public void close() {
        dialog.close();
    }

    public void setPatterns(Set<PatternDTO> patterns) {
        this.patterns.addAll(patterns);
    }

    public void clearPatterns() {
        patterns.clear();
        orderPatternsGrid.asMultiSelect().clear();
    }


    public static abstract class OrderDetailsDialogEvent extends ComponentEvent<OrderDetailsDialog> {
        private Set<PatternDTO> patterns;

        protected OrderDetailsDialogEvent(OrderDetailsDialog source, Set<PatternDTO> patterns) {
            super(source, false);
            this.patterns = patterns;
        }

        public Set<PatternDTO> getPatterns() {
            return patterns;
        }
    }

    public static class SaveEvent extends OrderDetailsDialogEvent {
        SaveEvent(OrderDetailsDialog source, Set<PatternDTO> patterns) {
            super(source, patterns);
        }
    }

    public static class CloseEvent extends OrderDetailsDialogEvent {
        CloseEvent(OrderDetailsDialog source) {
            super(source, null);
        }
    }

    public <T extends ComponentEvent<?>> Registration addListener(Class<T> eventType,
                                                                  ComponentEventListener<T> listener) {
        return getEventBus().addListener(eventType, listener);
    }

}
