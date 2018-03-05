package csc445.missouriwestern.edu.jaunt.model;

import android.location.Address;
import android.util.Log;

import org.json.JSONObject;

import java.util.Date;

import csc445.missouriwestern.edu.jaunt.utils.date.DateWrapper;
import csc445.missouriwestern.edu.jaunt.utils.places.AddressUtils;

/**
 * Created by byan on 2/20/2018.
 */

public class Driver {
    private int driverId;
    private String firstName;
    private String lastName;
    private String email;
    private String phone;
    private Date birthday;
    private String addressId;

    private String gms_id;
    private Address address;

    private boolean hasProfilePhoto;
    private boolean hasLicensePhoto;

    private String TAG = "TAG_Driver";

    public Driver() {
    }

    public Driver(int driverId, String firstName, String lastName, String email) {
        this.driverId = driverId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
    }

    public Driver(int driverId, String firstName, String lastName, String email, String phone, Date birthday) {

        this.driverId = driverId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.phone = phone;
        this.birthday = birthday;
    }

    public Driver(JSONObject jsonObject){
        try{
            this.driverId = Integer.parseInt(jsonObject.getString("id"));
            this.firstName = jsonObject.getString("firstname");
            this.lastName = jsonObject.getString("lastname");
            this.email = jsonObject.getString("email");
            String tmp = jsonObject.getString("phone");
            this.phone = (tmp == null || tmp.equalsIgnoreCase("null")) ? null : tmp;
            this.birthday = DateWrapper.sqlStringToDate(jsonObject.getString("birthday"));
            tmp = jsonObject.getString("address_id");
            this.addressId = (tmp == null || tmp.equalsIgnoreCase("null")) ? null : tmp;

            tmp = jsonObject.getString("profile_photo");
            tmp = (tmp == null || tmp.equalsIgnoreCase("null")) ? null : tmp;
            if(tmp != null){
                this.hasProfilePhoto = Boolean.parseBoolean(tmp);
            }
            tmp = jsonObject.getString("license_photo");
            tmp = (tmp == null || tmp.equalsIgnoreCase("null")) ? null : tmp;
            if(tmp != null){
                this.hasLicensePhoto = Boolean.parseBoolean(tmp);
            }

            JSONObject addressJson = jsonObject.getJSONObject("address");
            if(addressJson != null){
                gms_id = addressJson.getString("gms_id");
                address = AddressUtils.jsonToAddress(addressJson);
            }
        }catch (Exception e){
            Log.d(TAG, "Error unwrapping driver from JSONObject");
        }
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
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

    public Date getBirthday() {
        return birthday;
    }

    public void setBirthday(Date birthday) {
        this.birthday = birthday;
    }

    public String getAddressId() {
        return addressId;
    }

    public void setAddressId(String addressId) {
        this.addressId = addressId;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public boolean isHasProfilePhoto() {
        return hasProfilePhoto;
    }

    public void setHasProfilePhoto(boolean hasProfilePhoto) {
        this.hasProfilePhoto = hasProfilePhoto;
    }

    public boolean isHasLicensePhoto() {
        return hasLicensePhoto;
    }

    public void setHasLicensePhoto(boolean hasLicensePhoto) {
        this.hasLicensePhoto = hasLicensePhoto;
    }
    public String getGms_id() {
        return gms_id;
    }

    public void setGms_id(String gms_id) {
        this.gms_id = gms_id;
    }

}
