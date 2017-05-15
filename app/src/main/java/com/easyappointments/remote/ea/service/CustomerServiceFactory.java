package com.easyappointments.remote.ea.service;

import com.easyappointments.remote.ea.service.customer.CustomerService;

/**
 * Created by matte on 15/05/2017.
 */

public class CustomerServiceFactory extends BaseServiceFactory {
    public static CustomerService getInstance() {
        return getClient(CustomerService.class);
    }
}
