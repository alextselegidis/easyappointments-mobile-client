package com.easyappointments.remote.ea.service.service;

import com.easyappointments.remote.ea.model.ws.ServiceModel;

import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.QueryMap;

/**
 * Created by matte on 15/05/2017.
 */

public interface ServiceService {
    @GET("services")
    Call<List<ServiceModel>> get(@QueryMap Map<String, String> options);
    @GET("services/{id}")
    Call<ServiceModel> get(@Path("id") int id);
}
