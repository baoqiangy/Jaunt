package csc445.missouriwestern.edu.jaunt.model;

/**
 * Created by byan on 3/15/2018.
 */

public class FoodItem {
    private int cid;
    private int fid;
    private String name;
    private double price;

    public FoodItem(int cid, int fid, String name, double price) {
        this.cid = cid;
        this.fid = fid;
        this.name = name;
        this.price = price;
    }

    public int getCid() {
        return cid;
    }

    public void setCid(int cid) {
        this.cid = cid;
    }

    public int getFid() {
        return fid;
    }

    public void setFid(int fid) {
        this.fid = fid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }
}
