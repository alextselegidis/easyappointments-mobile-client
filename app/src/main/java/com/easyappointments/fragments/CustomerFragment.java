package com.easyappointments.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.easyappointments.R;
import com.easyappointments.common.AsyncWSTask;
import com.easyappointments.db.SettingsModel;
import com.easyappointments.fragments.adapter.CustomersRecyclerViewAdapter;
import com.easyappointments.remote.ea.data.Options;
import com.easyappointments.remote.ea.model.ws.CustomerModel;
import com.easyappointments.remote.ea.service.CustomerServiceFactory;
import com.easyappointments.remote.ea.service.customer.CustomerService;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Response;

/**
 * Created by matte on 17/05/2017.
 */

public class CustomerFragment extends BaseFragment<CustomerModel>{
    private List<CustomerModel> customers = null;
    private CustomersTask customersTask;

    public CustomerFragment() {
        super(R.string.title_customers);
    }

    @Override
    public void onClick(CustomerModel item) {

    }

    @Override
    public boolean onLongClick(CustomerModel item) {
        return false;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = super.onCreateView(inflater, container, savedInstanceState);

        if(refresh) {
            customersTask = new CustomersTask(recyclerView);
            customersTask.execute((Void) null);
        }else{
            recyclerView.setAdapter(new CustomersRecyclerViewAdapter(customers, mListener));
        }

        return v;
    }

    class CustomersTask extends AsyncWSTask {

        protected CustomersTask(View v) {
            super(v);
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            showProgress(true);

            SettingsModel settings = SettingsModel.loadSettings();

            Map<String, String> filters = new HashMap<>();
            filters.put(Options.sort, Options.sort_asc + CustomerModel.fields.firstName.toString()+","+Options.sort_asc+CustomerModel.fields.lastName.toString());
            /*filters.put(Options.page, "1");
            filters.put(Options.length, "10");*/

            try {
                CustomerService service = CustomerServiceFactory.getInstance();

                Response<List<CustomerModel>> resp = service.get(filters).execute();
                customers = resp.body();

                if(customers == null)
                    errorMessage = getString(R.string.unknown_error);

                return customers != null;
            } catch (IOException e) {
                customers = new ArrayList<>(0);
                errorMessage = getString(R.string.error_network);
                return false;
            }
        }

        @Override
        protected void onPostExecute(Boolean success) {
            super.onPostExecute(success);

            customersTask = null;
            showProgress(false);

            if (success) {
                refresh = false;
                ((RecyclerView)mView).setAdapter(new CustomersRecyclerViewAdapter(customers, mListener));
            }
        }

        @Override
        protected void onCancelled() {
            super.onCancelled();

            customersTask = null;
            showProgress(false);
        }
    }
}
