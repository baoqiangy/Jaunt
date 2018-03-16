package csc445.missouriwestern.edu.jaunt.model;

import java.util.List;

/**
 * Created by byan on 3/15/2018.
 */

public class Order {
    private Customer customer;
    private Restaurant restaurant;
    private List<FoodItemOrder> foodItemOrders;

    public Order(Customer customer, Restaurant restaurant, List<FoodItemOrder> foodItemOrders) {
        this.customer = customer;
        this.restaurant = restaurant;
        this.foodItemOrders = foodItemOrders;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public Restaurant getRestaurant() {
        return restaurant;
    }

    public void setRestaurant(Restaurant restaurant) {
        this.restaurant = restaurant;
    }

    public List<FoodItemOrder> getFoodItemOrders() {
        return foodItemOrders;
    }

    public void setFoodItemOrders(List<FoodItemOrder> foodItemOrders) {
        this.foodItemOrders = foodItemOrders;
    }
}
