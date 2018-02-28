package csc445.missouriwestern.edu.jaunt.model;

/**
 * Created by byan on 2/20/2018.
 */

public class Driver {
    private int driverId;
    private String firstName;
    private String lastName;
    private String email;
    private Registration registration;

    public Driver() {
    }

    public Driver(int driverId, String firstName, String lastName, String email) {
        this.driverId = driverId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
    }

    public Driver(int driverId, String firstName, String lastName, String email, Registration registration) {
        this.driverId = driverId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.registration = registration;
    }

    public int getDriverId() {
        return driverId;
    }

    public void setDriverId(int driverId) {
        this.driverId = driverId;
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Registration getRegistration() {
        return registration;
    }

    public void setRegistration(Registration registration) {
        this.registration = registration;
    }

    private static Driver driver = null;
    public static Driver getInstance() {
        if(driver == null) {
            driver  = new Driver();
        }
        return driver;
    }
}
