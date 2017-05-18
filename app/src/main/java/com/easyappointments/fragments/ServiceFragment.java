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
import com.easyappointments.fragments.adapter.ServicesRecyclerViewAdapter;
import com.easyappointments.remote.ea.data.Options;
import com.easyappointments.remote.ea.model.ws.ProviderModel;
import com.easyappointments.remote.ea.model.ws.ServiceModel;
import com.easyappointments.remote.ea.service.ServiceServiceFactory;
import com.easyappointments.remote.ea.service.service.ServiceService;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Response;

/**
 * Created by matte on 17/05/2017.
 */

public class ServiceFragment extends BaseFragment<ServiceModel> {
    private int[] servicesProvider;
    private ServicesTask servicesTask;
    private List<ServiceModel> services;

    public ServiceFragment() {
        super(R.string.title_services);
    }

    @Override
    public void onClick(ServiceModel item) {

    }

    @Override
    public boolean onLongClick(ServiceModel item) {
        return false;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = super.onCreateView(inflater, container, savedInstanceState);

        Bundle b = getArguments();
        if(b!=null){
            this.servicesProvider = b.getIntArray(ProviderModel.fields.services.toString());
        }else{
            this.servicesProvider = null;
        }

        if (refresh) {
            servicesTask = new ServicesTask(recyclerView);
            servicesTask.execute((Void) null);
        }else{
            recyclerView.setAdapter(new ServicesRecyclerViewAdapter(services, mListener));
        }

        return v;
    }

    class ServicesTask extends AsyncWSTask {

        protected ServicesTask(View v) {
            super(v);
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            showProgress(true);

            SettingsModel settings = SettingsModel.loadSettings();

            Map<String, String> filters = new HashMap<>();
            filters.put(Options.sort, Options.sort_asc + ServiceModel.fields.name.toString());
            /*filters.put(Options.page, "1");
            filters.put(Options.length, "10");*/

            try {
                ServiceService service = ServiceServiceFactory.getInstance();

                Response<List<ServiceModel>> resp = service.get(filters).execute();

                services = resp.body();

                if (services == null)
                    errorMessage = getString(R.string.unknown_error);

                if(servicesProvider != null){
                    List<ServiceModel> servicesProviderModel = new ArrayList<>(servicesProvider.length);

                    for(int sid : servicesProvider){
                        for(ServiceModel s : services){
                            if(s.id == sid){
                                servicesProviderModel.add(s);
                                break;
                            }
                        }
                    }

                    services = servicesProviderModel;
                }

                return services != null;
            } catch (IOException e) {
                services = new ArrayList<>(0);
                errorMessage = getString(R.string.error_network);
                return false;
            }
        }

        @Override
        protected void onPostExecute(Boolean success) {
            super.onPostExecute(success);

            servicesTask = null;
            showProgress(false);

            if (success) {
                refresh = false;
                ((RecyclerView) mView).setAdapter(new ServicesRecyclerViewAdapter(services, mListener));
            }
        }

        @Override
        protected void onCancelled() {
            super.onCancelled();

            servicesTask = null;
            showProgress(false);
        }
    }
}
