package com.easyappointments.remote.ea.service.appointment;

import com.easyappointments.remote.ea.model.ws.AppointmentsModel;
import com.easyappointments.remote.ea.model.ws.CustomerModel;

import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.QueryMap;

/**
 * Created by matte on 15/05/2017.
 */

public interface AppointmentService {
    @GET("appointments/{id}")
    Call<AppointmentsModel> get(@Path("id") int id);
    @GET("appointments")
    Call<List<AppointmentsModel>> get(@QueryMap Map<String, String> options);
}
