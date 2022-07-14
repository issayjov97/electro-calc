//package com.example.application.ui.views.patern;
//
//import com.example.application.dto.PatternDTO;
//import com.example.application.service.PatternService;
//import com.vaadin.flow.component.button.Button;
//import com.vaadin.flow.component.html.Div;
//import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
//import com.vaadin.flow.component.textfield.NumberField;
//import com.vaadin.flow.component.textfield.TextField;
//import com.vaadin.flow.data.value.ValueChangeMode;
//
//import java.util.Set;
//
//public class ItemsToolbar extends HorizontalLayout {
//
//
//    public ItemsToolbar(PatternService patternService) {
//        this.patternService = patternService;
//        this.addClassName("toolbar");
//        this.nameFilter = new TextField();
//        this.priceFilter = new NumberField();
//        this.durationFilter = new NumberField();
//        durationFilter.getElement().setAttribute("theme","test");
//        durationFilter.getElement().setAttribute("theme","test");
//        Button filterButton = new Button("Filter");
//
//        nameFilter.setPlaceholder("Name");
//        nameFilter.setClearButtonVisible(true);
//        nameFilter.setValueChangeMode(ValueChangeMode.LAZY);
//
//        durationFilter.setPlaceholder("Duration");
//        durationFilter.setClearButtonVisible(true);
//        durationFilter.setClassName("toolbar");
//        durationFilter.setValueChangeMode(ValueChangeMode.LAZY);
//
//        priceFilter.setPlaceholder("Price");
//        priceFilter.setClearButtonVisible(true);
//        priceFilter.setValueChangeMode(ValueChangeMode.LAZY);
//
//        add(nameFilter, priceFilter, durationFilter, filterButton);
//    }
//
//    public Set<PatternDTO> filter() {
//        return patternService.filter(nameFilter.getValue(), priceFilter.getValue(), durationFilter.getValue());
//    }
//
//    public void addNewPattern() {
//    }
//
//}
