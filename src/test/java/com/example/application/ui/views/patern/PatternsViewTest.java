    //package com.example.application.ui.views.patern;
    //
    //import com.example.application.persistence.entity.PatternEntity;
    //import com.example.application.service.PatternService;
    //import com.example.application.service.UserService;
    //import com.vaadin.flow.component.UI;
    //import com.vaadin.flow.component.grid.Grid;
    //import com.vaadin.flow.data.provider.ListDataProvider;
    //import com.vaadin.flow.data.provider.Query;
    //import org.junit.jupiter.api.BeforeEach;
    //import org.junit.jupiter.api.Test;
    //import org.springframework.beans.factory.annotation.Autowired;
    //import org.springframework.boot.test.context.SpringBootTest;
    //import org.springframework.security.test.context.support.WithMockUser;
    //
    //import java.math.BigDecimal;
    //
    //import static org.junit.jupiter.api.Assertions.assertEquals;
    //import static org.junit.jupiter.api.Assertions.assertFalse;
    //import static org.junit.jupiter.api.Assertions.assertNotNull;
    //import static org.junit.jupiter.api.Assertions.assertTrue;
    //
    //@SpringBootTest
    //class PatternsViewTest {
    //
    //    private final UI             ui = new UI();
    //    @Autowired
    //    private       PatternService patternService;
    //
    //    private PatternsView patternsView;
    //
    //    @Autowired
    //    private UserService userService;
    //
    //    @BeforeEach
    //    void init() {
    //        UI.setCurrent(ui);
    //        this.patternsView = new PatternsView(patternService, userService, null, null);
    //    }
    //
    //    @Test
    //    @WithMockUser(username = "tester123")
    //    public void formShownWhenPatternSelected() {
    //        Grid<PatternEntity> grid = patternsView.getItems();
    //        PatternEntity patternEntity = getFirstPattern(grid);
    //
    //        PatternForm form = patternsView.getPatternForm();
    //
    //        assertFalse(patternsView.getPatternForm().getDialog().isOpened());
    //        grid.asSingleSelect().setValue(patternEntity);
    //        assertTrue(patternsView.getPatternForm().getDialog().isOpened());
    //        assertEquals(patternEntity.getName(), form.getNameField().getValue());
    //    }
    //
    //    @Test
    //    @WithMockUser(username = "tester123")
    //    public void formShownWhenAddPatternClicked() {
    //        PatternForm form = patternsView.getPatternForm();
    //
    //        assertFalse(patternsView.getPatternForm().getDialog().isOpened());
    //        patternsView.getAddPatternButton().click();
    //
    //        assertTrue(patternsView.getPatternForm().getDialog().isOpened());
    //        assertNotNull(form.getEntity());
    //        assertEquals("", form.getNameField().getValue());
    //        assertEquals("", form.getDescriptionField().getValue());
    //        assertEquals(0, form.getDurationField().getValue());
    //        assertEquals(BigDecimal.ZERO, form.getPriceWithoutVatField().getValue());
    //
    //    }
    //
    //    @Test
    //    @WithMockUser(username = "tester123")
    //    public void filteredGridShownWhenFilterClicked() {
    //        Grid<PatternEntity> grid = patternsView.getItems();
    //
    //        patternsView.getNameFilter().setValue("testtest");
    //
    //        assertEquals(2, (int) grid.getDataProvider().fetch(new Query<>()).count());
    //        patternsView.getFilterButton().click();
    //
    //        assertEquals(1, (int) grid.getDataProvider().fetch(new Query<>()).count());
    //    }
    //
    //    private PatternEntity getFirstPattern(Grid<PatternEntity> grid) {
    //        return ((ListDataProvider<PatternEntity>) grid.getDataProvider()).getItems().iterator().next();
    //    }
    //}