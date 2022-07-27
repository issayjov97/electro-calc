package com.example.application.ui.views;

import com.example.application.persistence.entity.AbstractEntity;
import com.example.application.persistence.entity.FileEntity;
import com.example.application.persistence.repository.FileRepository;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;


public abstract class DocumentsDialog<T extends AbstractEntity> extends Div {
    protected final Grid<FileEntity> filesGrid;
    protected final   Dialog           dialog;
    private         T                entity;
    protected final FileRepository   fileRepository;

    public DocumentsDialog(FileRepository fileRepository) {
        this.filesGrid = new Grid<>(FileEntity.class);
        this.fileRepository = fileRepository;
        dialog = new Dialog();
        dialog.setResizable(true);
        dialog.setDraggable(true);
        dialog.add(filesGrid);
    }

    protected VerticalLayout createDialogLayout() {
        final H2 headline = new H2("Images");
        headline.setClassName("headline");
        VerticalLayout dialogLayout = new VerticalLayout();
        HorizontalLayout header = new HorizontalLayout(headline);
        dialogLayout.add(header);
        dialogLayout.setPadding(false);
        dialogLayout.setAlignItems(FlexComponent.Alignment.STRETCH);
        return dialogLayout;
    }

    protected abstract void configureGrid();

    protected abstract Component uploadFile();

    public void open() {
        dialog.open();
    }

    protected abstract void updateList();

    public void close() {
        dialog.close();
    }

    public Dialog getDialog() {
        return dialog;
    }

    public T getEntity() {
        return entity;
    }

    public void setEntity(T entity) {
        this.entity = entity;
        updateList();
    }
}
