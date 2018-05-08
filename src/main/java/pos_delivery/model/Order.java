package pos_delivery.model;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Order Entity Represents Details Of Each Order.
 * Order Consists Of Quantity and Menu.
 * Each Order Can Have Special Instruction Details.
 */
@Entity @Table(name = "ORDERS")
public class Order implements Serializable {

    /** Customer Assoicated With Order **/
    private Customer customer;

    /** Menu Associated With Order **/
    private Menu menu;

    /** Quantity Of Menu That Customer Ordering **/
    private int quantity;

    /** Any Special Instruction On The Order **/
    private List<String> details;

    /**
     * Constructor :
     * Order Requires Customer And Menu.
     * Each Order Initialized With Empty List Of Special Instruction And Zero Quantity.
     */
    public Order() {}
    public Order(Customer customer, Menu menu) {
        this.customer = customer;
        this.menu = menu;
        this.quantity = 0;
        this.details = new ArrayList<>();
    }

    @Id @ManyToOne(fetch = FetchType.LAZY) @JoinColumn(name = "CUSTOMER")
    public Customer getCustomer() { return customer; }
    public void setCustomer(Customer customer) { this.customer = customer; }

    @Id @ManyToOne(fetch = FetchType.LAZY)
    public Menu getMenu() { return menu; }
    public void setMenu(Menu menu) { this.menu = menu; }

    @Column(name = "QUANTITY")
    public int getQuantity() { return quantity; }
    public void setQuantity(int quantity) { this.quantity = quantity; }

    @ElementCollection(fetch = FetchType.EAGER) @Column(name = "DETAILS")
    public List<String> getDetails() { return details; }
    public void setDetails(List<String> details) { this.details = details; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Order order = (Order) o;

        return menu.equals(order.menu);
    }

    @Override
    public int hashCode() {
        return menu.hashCode();
    }

    @Override
    public String toString() {
        String str = getMenu().getName();
        for (String detail : details) str += "\n(" + detail + ")";

        return str;
    }
}