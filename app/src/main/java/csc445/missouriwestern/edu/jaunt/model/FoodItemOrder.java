package csc445.missouriwestern.edu.jaunt.model;

/**
 * Created by byan on 3/15/2018.
 */

public class FoodItemOrder {
    private FoodItem foodItem;
    private String options;
    private double paidPrice;
    private int qty;

    public FoodItemOrder(FoodItem foodItem, String options, double paidPrice, int qty) {
        this.foodItem = foodItem;
        this.options = options;
        this.paidPrice = paidPrice;
        this.qty = qty;
    }

    public FoodItem getFoodItem() {
        return foodItem;
    }

    public void setFoodItem(FoodItem foodItem) {
        this.foodItem = foodItem;
    }

    public String getOptions() {
        return options;
    }

    public void setOptions(String options) {
        this.options = options;
    }

    public double getPaidPrice() {
        return paidPrice;
    }

    public void setPaidPrice(double paidPrice) {
        this.paidPrice = paidPrice;
    }

    public int getQty() {
        return qty;
    }

    public void setQty(int qty) {
        this.qty = qty;
    }
}
