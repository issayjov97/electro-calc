package com.example.application.ui.views;

import com.example.application.service.CrudService;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.ComponentEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.shared.Registration;

public abstract class AbstractServicesView<E, S> extends VerticalLayout {
    private final Grid<E> itemsGrid;
    private final CrudService<S> crudService;
    private int lastPage;

    public AbstractServicesView(Grid<E> grid, CrudService<S> crudService) {
        this.itemsGrid = grid;
        this.crudService = crudService;
        itemsGrid.setPageSize(50);
        setSizeFull();
    }

    public Component getContent() {
        HorizontalLayout horizontalLayout = new HorizontalLayout(itemsGrid);
        horizontalLayout.setSizeFull();
        return horizontalLayout;
    }

    protected abstract void configureForm();

    protected abstract void configureGrid();

    protected HorizontalLayout getToolBar() {
        return null;
    }

    protected abstract void configureEvents();

    protected abstract void closeEditor();

    protected abstract void updateList();

    public Grid<E> getItems() {
        return itemsGrid;
    }

    public int getLastPage() {
        return lastPage;
    }

    public void setLastPage(int lastPage) {
        this.lastPage = lastPage;
    }

    public <T extends ComponentEvent<?>> Registration addListener(Class<T> eventType,
                                                                  ComponentEventListener<T> listener) {
        return getEventBus().addListener(eventType, listener);
    }

}
