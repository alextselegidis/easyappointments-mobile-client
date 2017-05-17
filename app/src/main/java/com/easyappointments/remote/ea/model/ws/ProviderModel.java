package com.easyappointments.remote.ea.model.ws;

import java.util.List;

/**
 * Created by matteo on 24/04/17.
 */

public class ProviderModel extends BaseModel{
    public enum fields {id, email, firstName, lastName, settings, services};

    public int id;
    public String email;
    public String firstName;
    public String lastName;
    public int[] services;

    public ProviderSettingsModel settings;
}
