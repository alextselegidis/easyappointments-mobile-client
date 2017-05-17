package com.easyappointments.remote.ea.service.provider;

import com.easyappointments.remote.ea.model.ws.ProviderModel;

import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.QueryMap;

/**
 * Created by matteo on 24/04/17.
 */

public interface ProviderService {
    @GET("providers/{id}")
    Call<ProviderModel> get(@Path("id") int id);
    @GET("providers/{id}")
    Call<ProviderModel> get(@Path("id") int id, @QueryMap Map<String, String> options);
    @GET("providers")
    Call<List<ProviderModel>> get(@QueryMap Map<String, String> options);
    @GET("providers")
    Call<List<ProviderModel>> get();
}
