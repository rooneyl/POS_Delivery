package org.sjlee.pos.delivery.module;

import org.sjlee.pos.delivery.model.Customer;
import org.sjlee.pos.delivery.model.Menu;
import org.sjlee.pos.delivery.model.Order;
import org.sjlee.pos.delivery.model.Source;

import java.util.ArrayList;
import java.util.List;

/**
 * OrderPlacer Organizes Orders Taken From Customer.
 * Order Can Be Placed As Multiple List Of OrderList For
 * Separate Bagging.
 */
public class OrderPlacer {

    /**
     * Customer Information
     **/
    private Customer customer;

    /**
     * OrderList Can Have Multiple Section For Separate Bagging
     **/
    private List<List<Order>> orderList;

    /**
     * Constructor :
     * OrderPlacer Requires Name Of Customer And Source of Order.
     * OrderPlace Initialized With One Section For OrderList.
     */
    public OrderPlacer(String name, Source source) {
        customer = new Customer(name, source);
        orderList = new ArrayList<>();
        orderList.add(new ArrayList<>());
    }

    /**
     * Return Order List
     */
    public List<List<Order>> getOrderList() {
        return orderList;
    }

    /**
     * Add Item Into Order List.
     * If Item Is Already Added Before, It Increase Quantity Orderd.
     * Otherwise, It Create New Order Associated With Menu.
     *
     * @param menu Item That Need To Be In The List
     * @param no   Section of Order
     */
    public void addOrder(final Menu menu, int no) {
        Order order = orderList.get(no).stream()
                .filter(o -> o.getMenu().equals(menu))
                .findFirst()
                .orElse(null);

        if (order == null) {
            order = new Order(customer, menu);
            orderList.get(no).add(order);
        }

        order.setQuantity(order.getQuantity() + 1);
    }

    /**
     * Special Cooking Instruction Can Be Added.
     *
     * @param order  Order That Needs Special Instruction
     * @param detail Special Instruction
     * @param no     Section of Order
     */
    public void addDetail(final Order order, final String detail, int no) {
        orderList.get(no).stream()
                .filter(o -> o.equals(order))
                .findFirst()
                .ifPresent(o -> o.getDetails().add(detail));
    }

    /**
     * Remove Order From OrderList
     *
     * @param order Order That Needs To Be Removed
     * @param no    Section of Order
     */
    public void removeOrder(final Order order, int no) {
        orderList.get(no).remove(order);
    }

    /**
     * Increase Section In OrderList
     */
    public void addSeparator() {
        orderList.add(new ArrayList<>());
    }

    /**
     * Place Order.
     * Sends Tasks To Corresponding Cooking Section.
     * Store OrderInformation In DB.
     * Do Nothing If There Is No Order.
     */
    public void placeOrder() {
        int totalOrderNum = orderList.stream().mapToInt(List::size).sum();
        if (totalOrderNum == 0) return;

        /* Execute Printer Job */
//        PrinterService.print(customer, orderList);

        /* Aggregate List of OrderList into Single List */
        List<Order> netOrder = orderList.get(0);
        if (orderList.size() > 1) {
            for (int i = 1; i < orderList.size(); i++) {
                for (Order order : orderList.get(i))
                    if (netOrder.contains(order)) {
                        Order o = netOrder.get(netOrder.indexOf(order));
                        o.setQuantity(o.getQuantity() + order.getQuantity());
                    } else {
                        netOrder.add(order);
                    }
            }
        }
        customer.setOrderList(netOrder);

        /* Save Customer into DataBase */
        DataBaseController.getInstance().saveOrder(customer);
    }

    /**
     * Get Customer Information
     */
    public Customer getCustomer() {
        return customer;
    }
}