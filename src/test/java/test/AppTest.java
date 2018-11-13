package test;

import org.junit.Before;
import org.junit.Test;
import org.sjlee.pos.delivery.model.*;
import org.sjlee.pos.delivery.module.Configurator;
import org.sjlee.pos.delivery.module.DataBaseController;
import org.sjlee.pos.delivery.module.MenuController;
import org.sjlee.pos.delivery.module.OrderPlacer;

import java.sql.Date;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class AppTest {
    private MenuController menuController;
    private DataBaseController dataBaseController;

    private Date currDate;
    private Random rand;

    @Before
    public void beforeClass() {
        try {
            DataBaseController.getInstance().startDB();
            Configurator.load();
            menuController = MenuController.getInstance();
            dataBaseController = DataBaseController.getInstance();

            currDate = Date.valueOf(LocalDate.now());
            rand = new Random();
        } catch (Exception e) {
            System.out.println("start-up fail");
        }

    }

    @Test
    public void menuParserTest() {
        assertEquals(29, menuController.getMenus(Category.APPETIZER).size());
        assertEquals(6, menuController.getMenus(Category.ENTREE).size());
        assertEquals(13, menuController.getMenus(Category.COMBO).size());
        assertEquals(26, menuController.getMenus(Category.NIGIRI).size());
        assertEquals(7, menuController.getMenus(Category.OSHI).size());
        assertEquals(41, menuController.getMenus(Category.ROLL).size());
        assertEquals(15, menuController.getMenus(Category.SASHIMI).size());
        assertEquals(28, menuController.getMenus(Category.UDON).size());
        assertEquals(6, menuController.getMenus(Category.TEMPURA).size());
        assertEquals(16, menuController.getMenus(Category.SPECIAL).size());
    }

    @Test
    public void orderPlacerTest_Empty() {
        OrderPlacer orderPlacer = new OrderPlacer("Empty",Source.FOODORA);
        orderPlacer.placeOrder();
        assertTrue(dataBaseController.retrieveOrder(currDate,currDate).isEmpty());
    }

    @Test
    public void orderPlacerTest_NonEmpty() {
        OrderPlacer orderPlacer_empty = new OrderPlacer("SingleOrder",Source.FOODORA);

        // no order added should not saved into db
        orderPlacer_empty.placeOrder();
        assertTrue(dataBaseController.retrieveOrder(currDate,currDate).isEmpty());

        // singleOrder
        orderPlacer_empty.addOrder(menuController.getMenus(Category.APPETIZER).get(0),0);
        orderPlacer_empty.placeOrder();

        Customer singleOrder = orderPlacer_empty.getCustomer();
        Customer singleOrder_db = dataBaseController.retrieveOrder(currDate,currDate).get(0);
        List<Order> singleOrder_db_detail = dataBaseController.retrieveOrderDetail(singleOrder_db);

        assertEquals(1, dataBaseController.retrieveOrder(currDate, currDate).size());
        assertTrue(orderChecker(singleOrder.getOrderList(),singleOrder_db_detail));

        // multipleOrder
        List<Menu> randomMenus = Arrays.stream(Category.values())
                .map(category -> menuController.getMenus(category))
                .flatMap(List::stream)
                .filter(menu -> rand.nextBoolean())
                .filter(menu->rand.nextBoolean())
                .collect(Collectors.toList());

        OrderPlacer orderPlacer_multi = new OrderPlacer("Multi",Source.FOODORA);
        randomMenus.forEach(menu -> orderPlacer_multi.addOrder(menu,0));
        orderPlacer_multi.placeOrder();

        Customer multiOrder = orderPlacer_empty.getCustomer();
        Customer multieOrder_db = dataBaseController.retrieveOrder(currDate,currDate).get(0);
        List<Order> multiOrder_db_detail = dataBaseController.retrieveOrderDetail(singleOrder_db);

        assertEquals(2, dataBaseController.retrieveOrder(currDate, currDate).size());
        assertTrue(orderChecker(multiOrder.getOrderList(),multiOrder_db_detail));
    }

    private boolean orderChecker(List<Order> orderList_A, List<Order> orderList_B) {
        if(orderList_A.size() != orderList_B.size()) return false;
        for (Order order : orderList_A) {
            if (!orderList_B.contains(order)) return false;
        }

        return true;
    }
}
