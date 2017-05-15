package com.easyappointments.remote.ea.service;

import com.easyappointments.remote.ea.service.appointment.AppointmentService;

/**
 * Created by matte on 15/05/2017.
 */

public class AppointmentServiceFactory extends BaseServiceFactory {
    public static AppointmentService getInstance() {
        return getClient(AppointmentService.class);
    }
}
