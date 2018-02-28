package csc445.missouriwestern.edu.jaunt.model;

import android.location.Address;

import java.util.Date;

/**
 * Created by byan on 2/20/2018.
 */

public class Registration {
    private Address address;
    private Date birthday;
    private String license;

    public Registration(Address address, Date birthday, String license) {
        this.address = address;
        this.birthday = birthday;
        this.license = license;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public Date getBirthday() {
        return birthday;
    }

    public void setBirthday(Date birthday) {
        this.birthday = birthday;
    }

    public String getLicense() {
        return license;
    }

    public void setLicense(String license) {
        this.license = license;
    }
}
