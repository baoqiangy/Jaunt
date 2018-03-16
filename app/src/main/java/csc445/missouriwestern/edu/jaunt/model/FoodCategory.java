package csc445.missouriwestern.edu.jaunt.model;

import java.util.List;

/**
 * Created by byan on 3/15/2018.
 */

public class FoodCategory {
    private int rid;
    private int cid;
    private String name;
    private List<FoodItem> foodItems;

    public FoodCategory(int rid, int cid, String name) {
        this.rid = rid;
        this.cid = cid;
        this.name = name;
    }

    public int getRid() {
        return rid;
    }

    public void setRid(int rid) {
        this.rid = rid;
    }

    public int getCid() {
        return cid;
    }

    public void setCid(int cid) {
        this.cid = cid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<FoodItem> getFoodItems() {
        return foodItems;
    }

    public void setFoodItems(List<FoodItem> foodItems) {
        this.foodItems = foodItems;
    }
}
