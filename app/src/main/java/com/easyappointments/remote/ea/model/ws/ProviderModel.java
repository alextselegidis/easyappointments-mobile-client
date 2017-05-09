package com.easyappointments.remote.ea.model.ws;

/**
 * Created by matteo on 24/04/17.
 */

public class ProviderModel extends BaseModel{
    public enum fields {id, email, firstName, lastName};

    public int id;
    public String email;
    public String firstName;
    public String lastName;
}
