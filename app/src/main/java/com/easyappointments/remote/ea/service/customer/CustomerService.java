package com.easyappointments.remote.ea.service.customer;

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

public interface CustomerService {
    @GET("customers")
    Call<List<CustomerModel>> get(@QueryMap Map<String, String> options);
    @GET("customers/{id}")
    Call<CustomerModel> get(@Path("id") int id);
    @GET("customers/{id}")
    Call<CustomerModel> get(@Path("id") int id, @QueryMap Map<String, String> options);
}
