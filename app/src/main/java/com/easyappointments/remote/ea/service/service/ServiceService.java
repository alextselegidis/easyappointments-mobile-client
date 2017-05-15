package com.easyappointments.remote.ea.service.service;

import com.easyappointments.remote.ea.model.ws.ServiceModel;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

/**
 * Created by matte on 15/05/2017.
 */

public interface ServiceService {
    @GET("services/{id}")
    Call<ServiceModel> get(@Path("id") int id);
}
