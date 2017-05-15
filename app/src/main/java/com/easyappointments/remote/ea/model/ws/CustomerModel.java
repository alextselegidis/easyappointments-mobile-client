package com.easyappointments.remote.ea.model.ws;

/**
 * Created by matte on 15/05/2017.
 */

public class CustomerModel extends BaseModel {
    public enum fields {id, email, firstName, lastName, phone, address, city, zip, notes};

    public int id;
    public String email;
    public String firstName;
    public String lastName;
    public String phone;
    public String address;
    public String city;
    public String zip;
    public String notes;

    @Override
    public String toString() {
        return firstName + " " + lastName;
    }
}
