package com.easyappointments.remote.ea.model.ws;

import com.easyappointments.common.Formatter;

import java.util.Date;

/**
 * Created by matte on 15/05/2017.
 */

public class AppointmentsModel extends BaseModel {
    public enum fields {id, start, end, notes, customerId, providerId, serviceId}

    public int id;
    public Date start;
    public Date end;
    public String notes;
    public int customerId;
    public int providerId;
    public int serviceId;

    public CustomerModel customerModel;
    public ServiceModel serviceModel;
}
