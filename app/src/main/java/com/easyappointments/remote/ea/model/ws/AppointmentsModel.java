package com.easyappointments.remote.ea.model.ws;

import android.os.AsyncTask;
import android.util.Log;

import com.easyappointments.remote.ea.service.CustomerServiceFactory;
import com.easyappointments.remote.ea.service.ServiceServiceFactory;
import com.easyappointments.remote.ea.service.customer.CustomerService;
import com.easyappointments.remote.ea.service.service.ServiceService;

import java.io.IOException;
import java.util.Date;
import java.util.concurrent.ExecutionException;

/**
 * Created by matte on 15/05/2017.
 */

public class AppointmentsModel extends BaseModel {
    private volatile AsyncTask<Void, Void, Void> customerTask, serviceTask;

    public enum fields {id, start, end, notes, customerId, providerId, serviceId}

    public int id;
    public Date start;
    public Date end;
    public String notes;
    public int customerId;
    public int providerId;
    public int serviceId;

    public AppointmentsModel(){
        super();

        customerTask = new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... params) {
                CustomerService client = CustomerServiceFactory.getInstance();
                try {
                    CustomerModel c = client.get(customerId).execute().body();
                    customerModel = c;
                } catch (IOException e) {
                    Log.e("AppointmentsModel", "Error loading customer");
                }

                return (Void)null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                customerTask = null;
            }
        };

        serviceTask = new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... params) {
                ServiceService client = ServiceServiceFactory.getInstance();
                try {
                    ServiceModel s = client.get(serviceId).execute().body();
                    serviceModel = s;
                } catch (IOException e) {
                    Log.e("AppointmentsModel", "Error loading service");
                }

                return (Void)null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                serviceTask = null;
            }
        };
    }

    public CustomerModel getCustomerModel() {
        if(customerModel == null && customerTask != null){
            try {
                customerTask.execute().get();
            } catch (InterruptedException e) {
            } catch (ExecutionException e) {
            }
        }
        return customerModel;
    }

    public ServiceModel getServiceModel() {
        if(serviceModel == null && serviceTask != null){
            try {
                serviceTask.execute().get();
            } catch (InterruptedException e) {
            } catch (ExecutionException e) {
            }
        }
        return serviceModel;
    }

    private CustomerModel customerModel;
    private ServiceModel serviceModel;
}
