package com.easyappointments.remote.ea.service.category;

import com.easyappointments.remote.ea.model.ws.CategoryModel;
import com.easyappointments.remote.ea.model.ws.ServiceModel;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

/**
 * Created by matte on 17/05/2017.
 */

public interface CategoryService {
    @GET("categories/{id}")
    Call<CategoryModel> get(@Path("id") int id);
}
