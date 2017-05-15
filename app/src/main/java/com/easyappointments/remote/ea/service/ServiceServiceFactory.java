package com.easyappointments.remote.ea.service;

import com.easyappointments.remote.ea.service.service.ServiceService;

/**
 * Created by matte on 15/05/2017.
 */

public class ServiceServiceFactory extends BaseServiceFactory {
    public static ServiceService getInstance() {
        return getClient(ServiceService.class);
    }
}
