package csc445.missouriwestern.edu.jaunt.model;

import org.joda.time.LocalTime;

import java.util.List;

/**
 * Created by byan on 3/15/2018.
 */

public class Delivery {
    private int driverId;
    //including totalAmount, deliveryFee and tips
    private List<Order> orders; //maybe group order
    private LocalTime scheduledPickupTime;
    private LocalTime actualPickupTime;
    private LocalTime actualDeliverTime;

    public Delivery(List<Order> orders, LocalTime scheduledPickupTime) {
        this.orders = orders;
        this.scheduledPickupTime = scheduledPickupTime;
    }

    public int getDriverId() {
        return driverId;
    }

    public void setDriverId(int driverId) {
        this.driverId = driverId;
    }

    public List<Order> getOrders() {
        return orders;
    }

    public void setOrders(List<Order> orders) {
        this.orders = orders;
    }

    public LocalTime getScheduledPickupTime() {
        return scheduledPickupTime;
    }

    public void setScheduledPickupTime(LocalTime scheduledPickupTime) {
        this.scheduledPickupTime = scheduledPickupTime;
    }

    public LocalTime getActualPickupTime() {
        return actualPickupTime;
    }

    public void setActualPickupTime(LocalTime actualPickupTime) {
        this.actualPickupTime = actualPickupTime;
    }

    public LocalTime getActualDeliverTime() {
        return actualDeliverTime;
    }

    public void setActualDeliverTime(LocalTime actualDeliverTime) {
        this.actualDeliverTime = actualDeliverTime;
    }


}
