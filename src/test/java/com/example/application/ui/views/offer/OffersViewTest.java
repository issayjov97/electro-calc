package com.example.application.ui.views.offer;

import com.example.application.persistence.entity.OfferEntity;
import com.example.application.persistence.predicate.OfferSpecification;
import com.example.application.service.CustomerService;
import com.example.application.service.OfferService;
import com.example.application.service.PatternService;
import com.example.application.service.UserService;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.data.provider.Query;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.transaction.annotation.Transactional;

import java.util.stream.Collectors;

import static com.example.application.Values.TESTER;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@Transactional
class OffersViewTest {

    private final UI ui = new UI();
    @Autowired
    private OfferService offerService;

    @Autowired
    private CustomerService customerService;

    @Autowired
    private PatternService patternService;

    @Autowired
    private UserService userService;

    private OffersView offersView;

    @BeforeEach
    void init() {
        UI.setCurrent(ui);
        this.offersView = new OffersView(offerService, userService, null, null, customerService);
    }


    @Test
    @WithMockUser(username = TESTER)
    void formShownWhenAddOfferClicked() {
        OfferForm form = offersView.getOfferForm();

        assertFalse(form.getDialog().isOpened());
        offersView.getAddButton().click();

        assertTrue(form.getDialog().isOpened());
        assertNotNull(form.getEntity());
        assertEquals("", form.getName().getValue());
        assertEquals("", form.getDescription().getValue());
    }

    @Test
    @WithMockUser(username = TESTER)
    void filteredGridShownWhenFilterClicked() {
        Grid<OfferEntity> grid = offersView.getItems();
        grid.setItems(offerService.filter(new OfferSpecification(1L,"", null,"")));
        var offers = grid.getDataProvider().fetch(new Query<>()).collect(Collectors.toList());

        assertEquals(offers.size(), (int) grid.getDataProvider().fetch(new Query<>()).count());
        offersView.getNameFilter().setValue("test");
        offersView.getFilterButton().click();

        assertEquals(offers.stream().filter(offerEntity -> offerEntity.getName().contains("test")).count(), (int) grid.getDataProvider().fetch(new Query<>()).count());
    }

    private OfferEntity getFirstOrder(Grid<OfferEntity> grid) {
        return grid.getDataProvider().fetch(new Query<>()).findFirst().orElse(null);
    }
}