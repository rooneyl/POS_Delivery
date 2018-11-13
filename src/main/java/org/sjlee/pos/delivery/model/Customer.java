package org.sjlee.pos.delivery.model;

import javax.persistence.*;
import java.sql.Date;
import java.sql.Time;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Customer Entity Represent Each Customer Placing An Order.
 * Customer Has List Of Orders Which Represent Details Of Orders.
 */
@Entity @Table(name = "CUSTOMERS")
public class Customer {

    /** Order Number Is Auto-Generated Unique Number Assigned To Each Customer **/
    private int orderNumber;

    /** Name Of Customer **/
    private String name;

    /** Date Of Customer Placed Order **/
    private Date date;

    /** Time Of Customer Placed Order **/
    private Time time;

    /** Orders That The Customer Placed **/
    private List<Order> orderList;

    /** Delivery Company That The Customer Used **/
    private Source source;

    /**
     * Constructor :
     * Customer Is Initialized With Name And Source Of Delivery Company.
     * At The Time of Creation, Date And Time Is Clocked Using System.Time.
     * For Consistency, First Letter Of Each Word In The Name Is Capitalized.
     */
    public Customer() {}
    public Customer(String name, Source source) {
        long currTime = System.currentTimeMillis();
        String capitalizedName = Stream.of(name.toLowerCase().trim().split("\\s"))
                .filter(word -> word.length() > 0)
                .map(word -> word.substring(0, 1).toUpperCase() + word.substring(1))
                .collect(Collectors.joining(" "));

        this.name = capitalizedName;
        this.source = source;
        this.date = new Date(currTime);
        this.time = new Time(currTime);
    }

    @Id @GeneratedValue @Column(name = "UID")
    public int getOrderNumber() { return orderNumber; }
    public void setOrderNumber(int orderNumber) { this.orderNumber = orderNumber; }

    @Column(name = "NAME")
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    @Column(name = "DATE")
    public Date getDate() { return date; }
    public void setDate(Date date) { this.date = date; }

    @Column(name = "TIME")
    public Time getTime() { return time; }
    public void setTime(Time time) { this.time = time; }

    @OneToMany(mappedBy = "customer", cascade = CascadeType.ALL, orphanRemoval = true)
    public List<Order> getOrderList() { return orderList; }
    public void setOrderList(List<Order> orderList) { this.orderList = orderList; }

    @Enumerated(EnumType.STRING) @Column(name = "SOURCE")
    public Source getSource() { return source; }
    public void setSource(Source source) { this.source = source; }
}