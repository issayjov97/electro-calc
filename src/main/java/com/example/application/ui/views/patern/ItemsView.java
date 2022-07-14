package com.example.application.ui.views.patern;

import com.example.application.dto.PatternDTO;
import com.example.application.service.PatternService;
import com.example.application.ui.views.MainView;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.NumberField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

@PageTitle("Items")
@CssImport(value = "./views/toolbar/text-field.css")
@Route(value = "items", layout = MainView.class)
public class ItemsView extends VerticalLayout {
    private final Grid<PatternDTO> itemsGrid        = new Grid(PatternDTO.class);
    private       TextField        nameFilter       = new TextField();
    private       NumberField      priceFilter      = new NumberField();
    private       NumberField      durationFilter   = new NumberField();
    private       Button           filterButton     = new Button("Filter");
    private       Button           addPatternButton = new Button("Add pattern");
    private final PatternService   patternService;
    private final PatternForm      patternForm      = new PatternForm();

    public ItemsView(PatternService patternService) {
        this.patternService = patternService;
        addClassName("Patterns-view");
        setSizeFull();
        configureGrid();
        add(getToolBar(), getContent(), patternForm);
        configureEvents();
    }

    private Component getContent() {
        HorizontalLayout content = new HorizontalLayout(itemsGrid);
        content.addClassNames("content");
        content.setSizeFull();
        return content;
    }

    private void configureGrid() {
        itemsGrid.addClassName("items-grid");
        itemsGrid.setSizeFull();
        itemsGrid.setColumns("name", "description", "duration", "priceWithoutVat");
        itemsGrid.getColumns().forEach(itemColumn -> itemColumn.setAutoWidth(true));
        itemsGrid.setItems(patternService.getAll());
        itemsGrid.asSingleSelect().addValueChangeListener(event -> {
            patternForm.setPatternDTO(event.getValue());
            patternForm.open("Edit pattern");
        });
    }

    private Component getToolBar() {
        nameFilter.getElement().setAttribute("theme", "test");
        durationFilter.getElement().setAttribute("theme", "test");
        priceFilter.getElement().setAttribute("theme", "test");

        nameFilter.setPlaceholder("Name");
        nameFilter.setClearButtonVisible(true);
        nameFilter.setValueChangeMode(ValueChangeMode.LAZY);

        durationFilter.setPlaceholder("Duration");
        durationFilter.setClearButtonVisible(true);
        durationFilter.setValueChangeMode(ValueChangeMode.LAZY);

        priceFilter.setPlaceholder("Price");
        priceFilter.setClearButtonVisible(true);
        priceFilter.setValueChangeMode(ValueChangeMode.LAZY);


        addPatternButton.addClickListener(e -> {
            itemsGrid.asSingleSelect().clear();
            patternForm.setPatternDTO(new PatternDTO());
            patternForm.open("Create pattern");
        });

        filterButton.addClickListener(e -> {
            itemsGrid.setItems(patternService.filter(nameFilter.getValue(), priceFilter.getValue(), durationFilter.getValue()));
        });
        HorizontalLayout filterPart = new HorizontalLayout(nameFilter, durationFilter, priceFilter, filterButton, addPatternButton);
        filterPart.addClassName("filterPart");
        HorizontalLayout toolbar = new HorizontalLayout(new Label("Patterns"), filterPart);
        toolbar.addClassName("toolbar");
        return toolbar;
    }

    private void configureEvents() {
        patternForm.addListener(PatternForm.SaveEvent.class, this::saveItem);
        patternForm.addListener(PatternForm.CloseEvent.class, e -> closeEditor());
        patternForm.addListener(PatternForm.DeleteEvent.class, this::deleteItem);
    }

    private void closeEditor() {
        patternForm.setPatternDTO(null);
        patternForm.close();
    }

    private void saveItem(PatternForm.SaveEvent event) {
        patternService.savePattern(event.getItem());
        updateList();
        closeEditor();
        var notification = Notification.show("Pattern was saved");
        notification.addThemeVariants(NotificationVariant.LUMO_SUCCESS);
        notification.setPosition(Notification.Position.TOP_CENTER);
    }

    private void updateList() {
        itemsGrid.setItems(patternService.filter(nameFilter.getValue(), priceFilter.getValue(), durationFilter.getValue()));
    }

    private void deleteItem(PatternForm.DeleteEvent event) {
        patternService.deletePattern(event.getItem());
        updateList();
        closeEditor();
        var notification = Notification.show("Pattern was deleted");
        notification.addThemeVariants(NotificationVariant.LUMO_SUCCESS);
        notification.setPosition(Notification.Position.TOP_CENTER);
    }
}
