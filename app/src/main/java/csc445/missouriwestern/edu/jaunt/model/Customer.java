package csc445.missouriwestern.edu.jaunt.model;

import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import csc445.missouriwestern.edu.jaunt.Jaunt;

/**
 * Created by byan on 3/15/2018.
 */

public class Customer {
    private int id;
    private String firstName;
    private String lastName;
    private String phone;
    private boolean isHost;

    public Customer(int id, String firstName, String lastName, String phone, boolean isHost) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.phone = phone;
        this.isHost = isHost;
    }

    public Customer(JSONObject jsonObject){
        try{
            this.id = jsonObject.getInt("ownerID");
            this.firstName = jsonObject.getString("ownerFirstName");
            this.lastName = jsonObject.getString("ownerLastName");
            this.phone = jsonObject.getString("ownerPhone");
            this.isHost = false;
        }catch (JSONException e){
            Toast.makeText(Jaunt.getAppContext(), "Constructing Customer - "+e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public boolean isHost() {
        return isHost;
    }

    public void setHost(boolean host) {
        isHost = host;
    }
}
