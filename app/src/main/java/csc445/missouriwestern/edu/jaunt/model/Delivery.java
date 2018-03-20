package csc445.missouriwestern.edu.jaunt.model;

import android.location.Address;
import android.widget.Toast;

import org.joda.time.DateTime;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import csc445.missouriwestern.edu.jaunt.Globals;
import csc445.missouriwestern.edu.jaunt.Jaunt;
import csc445.missouriwestern.edu.jaunt.utils.date.TimeWrapper;
import csc445.missouriwestern.edu.jaunt.utils.places.AddressUtils;
import io.paperdb.Paper;

/**
 * Created by byan on 3/15/2018.
 */

public class Delivery {
    private int driverId;
    private int goid;
    private int hostuid;
    private Restaurant restaurant;
    //including totalAmount, deliveryFee and tips
    private Address deliveryaddress;
    private double totalAmount;
    private double deliveryFee;
    private double tips;
    private List<Order> orders; //maybe group order
    private DateTime scheduledPickupTime;
    private DateTime scheduledDeliverTime;
    private DateTime actualPickupTime;
    private DateTime actualDeliverTime;

    public Delivery(int driverId, int hostuid, Restaurant restaurant, Address deliveryaddress, List<Order> orders, DateTime scheduledPickupTime,
                    DateTime scheduledDeliverTime, double totalAmount,
                    double deliveryFee, double tips){
        this.driverId = driverId;
        this.hostuid = hostuid;
        this.restaurant = restaurant;
        this.deliveryaddress = deliveryaddress;
        this.orders = orders;
        this.scheduledPickupTime = scheduledPickupTime;
        this.scheduledDeliverTime = scheduledDeliverTime;
        this.totalAmount = totalAmount;
        this.deliveryFee = deliveryFee;
        this.tips = tips;
    }

    public Delivery(JSONObject jsonObject){
        try{
            this.goid = jsonObject.getInt("goid");
            this.hostuid = jsonObject.getInt("hostuid");
            this.restaurant = loadRestaurant(jsonObject.getInt("rid"));
            if(this.restaurant == null) return;
            this.deliveryaddress = AddressUtils.jsonToAddress(jsonObject.getJSONObject("deliveryaddress"));
            this.orders = populateOrders(jsonObject.getJSONArray("carts"));
            this.scheduledPickupTime = TimeWrapper.sqlTimeToJodaTime(jsonObject.getString("potime"));
            this.scheduledDeliverTime = TimeWrapper.sqlTimeToJodaTime(jsonObject.getString("potime"));
            JSONObject summary = jsonObject.getJSONObject("summary");
            this.totalAmount = summary.getDouble("total");
            this.deliveryFee = summary.getDouble("deliveryfees");
            if(jsonObject.has("tips")){
                this.tips = jsonObject.getDouble("tips");
            }
        }catch (JSONException e){
            Toast.makeText(Jaunt.getAppContext(), "Constructing Delivery - " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    private List<Order> populateOrders(JSONArray delivery_orders) {
        List<Order> orders = new ArrayList<>();
        if(delivery_orders == null) return null;
        try{
            for (int i=0; i<delivery_orders.length(); i++) {
                JSONObject jsonObject = delivery_orders.getJSONObject(i);
                Order order = new Order(jsonObject);
                if(jsonObject.getInt("ownerID") == hostuid){
                    order.getCustomer().setHost(true);
                }
                orders.add(order);
            }
        }
        catch (JSONException e){
            Toast.makeText(Jaunt.getAppContext(), "Constructing Delivery - "+e.getMessage(), Toast.LENGTH_SHORT).show();
        }
        return orders;
    }

    private Restaurant loadRestaurant(int rid) {
        List<Restaurant> restaurants = Paper.book(Globals.GUEST_BOOK).read("restaurants", null);
        for (Restaurant r:
             restaurants) {
            if(r.getRid() == rid){
                return r;
            }
        }
        return null;
    }

    public Restaurant getRestaurant() {
        return restaurant;
    }

    public void setRestaurant(Restaurant restaurant) {
        this.restaurant = restaurant;
    }

    public Address getDeliveryaddress() {
        return deliveryaddress;
    }

    public void setDeliveryaddress(Address deliveryaddress) {
        this.deliveryaddress = deliveryaddress;
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

    public DateTime getScheduledPickupTime() {
        return scheduledPickupTime;
    }

    public void setScheduledPickupTime(DateTime scheduledPickupTime) {
        this.scheduledPickupTime = scheduledPickupTime;
    }

    public DateTime getScheduledDeliverTime() {
        return scheduledDeliverTime;
    }

    public void setScheduledDeliverTime(DateTime scheduledDeliverTime) {
        this.scheduledDeliverTime = scheduledDeliverTime;
    }

    public DateTime getActualPickupTime() {
        return actualPickupTime;
    }

    public void setActualPickupTime(DateTime actualPickupTime) {
        this.actualPickupTime = actualPickupTime;
    }

    public DateTime getActualDeliverTime() {
        return actualDeliverTime;
    }

    public void setActualDeliverTime(DateTime actualDeliverTime) {
        this.actualDeliverTime = actualDeliverTime;
    }

    public double getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(double totalAmount) {
        this.totalAmount = totalAmount;
    }

    public double getDeliveryFee() {
        return deliveryFee;
    }

    public void setDeliveryFee(double deliveryFee) {
        this.deliveryFee = deliveryFee;
    }

    public double getTips() {
        return tips;
    }

    public void setTips(double tips) {
        this.tips = tips;
    }
}
