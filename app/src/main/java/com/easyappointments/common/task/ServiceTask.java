package com.easyappointments.common.task;

import android.os.AsyncTask;

import com.easyappointments.remote.ea.model.ws.CustomerModel;
import com.easyappointments.remote.ea.model.ws.ServiceModel;
import com.easyappointments.remote.ea.service.CustomerServiceFactory;
import com.easyappointments.remote.ea.service.ServiceServiceFactory;
import com.easyappointments.remote.ea.service.customer.CustomerService;
import com.easyappointments.remote.ea.service.service.ServiceService;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

/**
 * Created by matte on 15/05/2017.
 */

public class ServiceTask extends AsyncTask<Integer, Void, Boolean> {
    public List<ServiceModel> services;

    @Override
    protected Boolean doInBackground(Integer... servicesIds) {
        HashSet<Integer> idDistinct = new HashSet<Integer>(Arrays.asList(servicesIds));
       ServiceService client = ServiceServiceFactory.getInstance();
        services = new ArrayList<>(idDistinct.size());

        try {
            for (Integer sid : idDistinct){
                ServiceModel s = client.get(sid).execute().body();
                if(s != null)
                    services.add(s);
            }

            return true;
        } catch (IOException e) {
            services = null;
            return false;
        }
    }
}
