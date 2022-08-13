package com.example.application.ui.views;

import com.example.application.persistence.entity.AbstractEntity;
import com.example.application.service.CrudService;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;

public abstract class AbstractServicesView<E extends AbstractEntity, S extends AbstractEntity> extends VerticalLayout {
    private final Grid<E>        itemsGrid;
    private final CrudService<S> crudService;

    public AbstractServicesView(Grid<E> grid, CrudService<S> crudService) {
        this.itemsGrid = grid;
        this.crudService = crudService;
        itemsGrid.setPageSize(15);
        setSizeFull();
    }

    public Component getContent() {
        VerticalLayout verticalLayout = new VerticalLayout(itemsGrid);
        verticalLayout.setSizeFull();
        return verticalLayout;
    }

    protected abstract void configureForm();

    protected abstract void configureGrid();

    protected abstract HorizontalLayout getToolBar();

    protected abstract void configureEvents();

    protected abstract void closeEditor();

    protected abstract void updateList();

    public Grid<E> getItems() {
        return itemsGrid;
    }
}
