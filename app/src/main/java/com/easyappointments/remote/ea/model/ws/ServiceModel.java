package com.easyappointments.remote.ea.model.ws;

/**
 * Created by matte on 15/05/2017.
 */

public class ServiceModel extends BaseModel {
    public enum fields {id, name, duration, price,currency}

    public int id;
    public String name;
    public int duration;
    public double price;
    public String currency;
}
