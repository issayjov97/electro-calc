package com.example.application.ui.views.order;

import com.example.application.persistence.entity.OrderEntity;
import com.example.application.persistence.entity.PatternEntity;
import com.example.application.service.OrderService;
import com.example.application.service.PatternService;
import com.example.application.service.UserService;
import com.example.application.ui.views.ServiceDetailsDialog;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.data.provider.ListDataProvider;
import com.vaadin.flow.data.provider.Query;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
class OrdersPatternsViewTest {

    private final UI           ui = new UI();
    @Autowired
    private       OrderService orderService;

    @Autowired
    private PatternService patternService;

    @Autowired
    private UserService userService;

    private OrdersPatternsView ordersPatternsView;

    @BeforeEach
    void init() {
        UI.setCurrent(ui);
        this.ordersPatternsView = new OrdersPatternsView(orderService, patternService, userService);
    }

    @Test
    @WithMockUser(username = "tester123")
    public void formShownWhenUserSelected() {
        OrderEntity orderEntity = orderService.load(1L);
        ordersPatternsView.setOrderEntity(orderEntity);
        ordersPatternsView.updateList();

        ServiceDetailsDialog orderDetailsDialog = ordersPatternsView.getOrderDetailsDialog();

        assertFalse(orderDetailsDialog.getDialog().isOpened());

        ordersPatternsView.getAddButton().click();

        assertTrue(orderDetailsDialog.getDialog().isOpened());
        assertEquals(2, orderDetailsDialog.getOrderPatternsGrid().getDataProvider().fetch(new Query<>()).count());
    }

    @Test
    @WithMockUser(username = "tester123")
    public void filteredGridShownWhenFilterClicked() {
        OrderEntity orderEntity = orderService.load(1L);
        ordersPatternsView.setOrderEntity(orderEntity);
        ordersPatternsView.updateList();
        var countBefore = ordersPatternsView.getItems().getDataProvider().fetch(new Query<>()).count();

        // TODO: Resolve delete button
//        ordersPatternsView.().click();

        var countAfter = ordersPatternsView.getItems().getDataProvider().fetch(new Query<>()).count();

        assertNotEquals(countBefore, countAfter);
    }

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