package com.easyappointments.remote.ea.model.ws;

import android.os.AsyncTask;
import android.util.Log;

import com.easyappointments.remote.ea.service.CategoryServiceFactory;
import com.easyappointments.remote.ea.service.CustomerServiceFactory;
import com.easyappointments.remote.ea.service.category.CategoryService;
import com.easyappointments.remote.ea.service.customer.CustomerService;

import java.io.IOException;
import java.util.concurrent.ExecutionException;

/**
 * Created by matte on 15/05/2017.
 */

public class ServiceModel extends BaseModel {
    private volatile AsyncTask<Void, Void, Void> categoryTask;

    public enum fields {id, name, duration, price,currency}

    public int id;
    public String name;
    public int duration;
    public double price;
    public String currency;
    public int categoryId;

    public ServiceModel(){
        super();

        categoryTask = new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... params) {
                CategoryService client = CategoryServiceFactory.getInstance();
                try {
                    CategoryModel c = client.get(categoryId).execute().body();
                    categoryModel = c;
                } catch (IOException e) {
                    Log.e("AppointmentsModel", "Error loading category");
                }

                return (Void)null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                categoryTask = null;
            }
        };
    }

    private CategoryModel categoryModel;
    public CategoryModel getCategory(){
        if(categoryModel == null && categoryTask != null){
            try {
                categoryTask.execute().get();
            } catch (InterruptedException e) {
            } catch (ExecutionException e) {
            }
        }
        return categoryModel;
    }
}
