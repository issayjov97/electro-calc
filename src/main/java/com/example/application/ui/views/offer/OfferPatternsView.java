package com.example.application.ui.views.offer;

import com.example.application.persistence.entity.OfferPattern;
import com.example.application.persistence.entity.PatternEntity;
import com.example.application.ui.views.AbstractServicesView;
import com.example.application.ui.views.offer.events.DeleteOfferPatternEvent;
import com.example.application.ui.views.offer.events.UpdateOfferPatternEvent;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.data.provider.Query;
import org.springframework.core.convert.ConversionService;

@CssImport(value = "./views/toolbar/text-field.css", themeFor = "vaadin-text-field")
@CssImport(value = "./views/menu-bar.css", themeFor = "vaadin-menu-bar")
public class OfferPatternsView extends AbstractServicesView<OfferPattern, OfferPattern> {

    private OfferPatternForm offerPatternForm;
    private final ConversionService conversionService;

    public OfferPatternsView(OfferPatternForm form, ConversionService conversionService) {
        super(new Grid<>(), null);
        this.conversionService = conversionService;
        this.offerPatternForm = form;
        configureGrid();
        add(getContent());
    }

    @Override
    protected void configureForm() {
    }

    @Override
    protected void configureGrid() {
        getItems().setWidthFull();
        getItems().setMinHeight("300px");
        getItems().addColumn(OfferPattern::getCount).setHeader("Počet").setFlexGrow(0).setWidth("80px");
        getItems().addColumn(e -> e.getPatternEntity().getName()).setHeader("Název").setFlexGrow(0).setWidth("200px");
        getItems().addColumn(e -> conversionService.convert(e.getMaterialsCost(), String.class)).setHeader("Cena materiálů");
        getItems().addColumn(e -> conversionService.convert(e.getWorkCost(), String.class)).setHeader("Cena práce");
        getItems().getColumns().forEach(itemColumn -> itemColumn.setAutoWidth(true));
        var menu = getItems().addContextMenu();
        menu.addItem("Upravit", event -> {
            var item = event.getItem();
            if (item.isPresent()) {
                item.ifPresent(offerPattern -> fireEvent(new UpdateOfferPatternEvent(this, item.get())));
                offerPatternForm.getIsCabelCrossSection().setValue(item.get().withCabelCrossSection());
                offerPatternForm.getCount().setValue(item.get().getCount());
                offerPatternForm.open("");
            }
        });
        menu.addItem("Odstranit", event -> {
            var item = event.getItem();
            item.ifPresent(offerPattern -> fireEvent(new DeleteOfferPatternEvent(this, offerPattern)));
        });
    }

    @Override
    protected HorizontalLayout getToolBar() {
        return super.getToolBar();
    }

    @Override
    protected void configureEvents() {
    }

    @Override
    protected void updateList() {
    }

    @Override
    protected void closeEditor() {
    }

    public boolean containsItem(PatternEntity value) {
        return getItems().getDataProvider().fetch(new Query<>()).anyMatch(e -> e.getPatternEntity().equals(value));
    }
}
