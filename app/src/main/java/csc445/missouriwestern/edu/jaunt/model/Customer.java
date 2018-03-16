package csc445.missouriwestern.edu.jaunt.model;

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
