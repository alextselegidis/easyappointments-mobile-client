package com.easyappointments.common.task;

import android.content.Context;
import android.os.AsyncTask;
import android.support.design.widget.Snackbar;

import com.easyappointments.AppointmentRecyclerViewAdapter;
import com.easyappointments.R;
import com.easyappointments.remote.ea.data.Options;
import com.easyappointments.remote.ea.model.ws.AppointmentsModel;
import com.easyappointments.remote.ea.model.ws.CustomerModel;
import com.easyappointments.remote.ea.model.ws.ProviderModel;
import com.easyappointments.remote.ea.service.CustomerServiceFactory;
import com.easyappointments.remote.ea.service.customer.CustomerService;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import retrofit2.Response;

/**
 * Created by matte on 15/05/2017.
 */

public class CustomerTask extends AsyncTask<Integer, Void, Boolean> {
    public List<CustomerModel> customers;

    @Override
    protected Boolean doInBackground(Integer... customersIds) {
        HashSet<Integer> idDistinct = new HashSet<Integer>(Arrays.asList(customersIds));
        CustomerService client = CustomerServiceFactory.getInstance();
        customers = new ArrayList<>(idDistinct.size());

        try {
            for (Integer cid : idDistinct){
                CustomerModel c = client.get(cid).execute().body();
                if(c != null)
                    customers.add(c);
            }

            return true;
        } catch (IOException e) {
            customers = null;
            return false;
        }
    }
}
