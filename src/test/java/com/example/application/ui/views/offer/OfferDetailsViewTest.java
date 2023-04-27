package com.example.application.ui.views.offer;

import com.example.application.persistence.entity.OfferEntity;
import com.example.application.persistence.entity.PatternEntity;
import com.example.application.service.*;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.data.provider.ListDataProvider;
import com.vaadin.flow.data.provider.Query;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.convert.ConversionService;
import org.springframework.security.test.context.support.WithMockUser;

import static com.example.application.Values.TESTER;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class OfferDetailsViewTest {

    private final UI ui = new UI();
    @Autowired
    private OfferService offerService;
    @Autowired
    private PatternService patternService;
    @Autowired
    private UserService userService;
    @Autowired
    private CustomerService customerService;
    @Autowired
    private FinancialService financialService;
    private OfferDetailsView OfferDetailsView;
    @Autowired
    private ConversionService converter;

    @BeforeEach
    void init() {
        UI.setCurrent(ui);
        this.OfferDetailsView = new OfferDetailsView(patternService, userService, customerService, financialService, offerService, converter);
    }

    @Test
    @WithMockUser(username = TESTER)
    void formShownWhenOfferSelected() {
        OfferEntity OfferEntity = offerService.load(1L);

        assertFalse(OfferDetailsView.getOfferDetailsForm().getDialog().isOpened());
        assertNull(OfferDetailsView.getOfferDetailsForm().getEntity());

            UI.getCurrent().navigate(OfferDetailsView.class, 1L);

//        ServiceDetailsDialog orderDetailsDialog = OfferDetailsView.getOrderDetailsDialog();
//
//        assertFalse(orderDetailsDialog.getDialog().isOpened());
//
//        OfferDetailsView.getAddButton().click();
//
//        assertTrue(orderDetailsDialog.getDialog().isOpened());
//        assertEquals(2, orderDetailsDialog.getOrderPatternsGrid().getDataProvider().fetch(new Query<>()).count());
    }

//    @Test
//    @WithMockUser(username = "tester123")
//    public void filteredGridShownWhenFilterClicked() {
//        OfferEntity OfferEntity = offerService.load(1L);
//        OfferDetailsView.setOfferEntity(OfferEntity);
//        OfferDetailsView.updateList();
//        var countBefore = OfferDetailsView.getItems().getDataProvider().fetch(new Query<>()).count();
//
//        // TODO: Resolve delete button
////        OfferDetailsView.().click();
//
//        var countAfter = OfferDetailsView.getItems().getDataProvider().fetch(new Query<>()).count();
//
//        assertNotEquals(countBefore, countAfter);
//    }

//    @Test
//    @WithMockUser(username = "tester123")
//    public void formShownWhenAddUserClicked() {
//        UserForm form = usersView.getUserForm();
//
//        assertFalse(usersView.getUserForm().getDialog().isOpened());
//        usersView.getAddButton().click();
//
//        assertTrue(usersView.getUserForm().getDialog().isOpened());
//        assertNotNull(form.getUserEntity());
//        assertEquals("", form.getUsername().getValue());
//        assertEquals("", form.getPassword().getValue());
//        assertEquals("", form.getEmail().getValue());
//        assertEquals("", form.getFirstName().getValue());
//        assertEquals("", form.getLastName().getValue());
//        assertEquals(true, form.getEnabled().getValue());
//        assertEquals(0, form.getAuthorities().getSelectedItems().size());
//    }
//
//    @Test
//    @WithMockUser(username = "tester123")
//    public void filteredGridShownWhenFilterClicked() {
//        Grid<UserEntity> grid = usersView.getItemsGrid();
//
//        usersView.getUsernameFilter().setValue("tester123");
//
//        assertEquals(3, (int) grid.getDataProvider().fetch(new Query<>()).count());
//        usersView.getFilterButton().click();
//
//        assertEquals(1, (int) grid.getDataProvider().fetch(new Query<>()).count());
//    }

    private PatternEntity getFirstPattern(Grid<PatternEntity> grid) {
        return ((ListDataProvider<PatternEntity>) grid.getDataProvider()).getItems().iterator().next();
    }

}