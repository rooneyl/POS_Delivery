package pos_delivery.module;

import pos_delivery.model.Customer;
import pos_delivery.model.Menu;
import pos_delivery.model.Order;
import pos_delivery.model.Source;

import java.util.ArrayList;
import java.util.List;

public class OrderPlacer {

    private Customer customer;
    private List<List<Order>> orderList;

    public OrderPlacer(String name, Source source) {
        customer = new Customer(name, source);
        orderList = new ArrayList<>();
        orderList.add(new ArrayList<>());
    }

    public List<List<Order>> getOrderList() {
        return orderList;
    }

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

    public void addDetail(final Order order, final String detail, int no) {
        orderList.get(no).stream()
                .filter(o -> o.equals(order))
                .findFirst()
                .ifPresent(o -> o.getDetails().add(detail));
    }

    public void removeOrder(final Order order, int no) {
        orderList.get(no).remove(order);
    }

    public void addSeparator() {
        orderList.add(new ArrayList<>());
    }

    public void placeOrder() {
        /* Execute Printer Job */
        // TODO

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

    public Customer getCustomer() {
        return customer;
    }
}