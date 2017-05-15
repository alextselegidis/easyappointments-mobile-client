package com.easyappointments;

import android.os.AsyncTask;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.easyappointments.common.Formatter;
import com.easyappointments.common.task.CustomerTask;
import com.easyappointments.common.task.ServiceTask;
import com.easyappointments.remote.ea.data.Options;
import com.easyappointments.remote.ea.model.ws.AppointmentsModel;
import com.easyappointments.remote.ea.model.ws.CustomerModel;
import com.easyappointments.remote.ea.model.ws.ProviderModel;
import com.easyappointments.remote.ea.model.ws.ServiceModel;
import com.easyappointments.remote.ea.service.CustomerServiceFactory;
import com.easyappointments.remote.ea.service.ProviderServiceFactory;
import com.easyappointments.remote.ea.service.customer.CustomerService;
import com.easyappointments.remote.ea.service.provider.ProviderService;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import retrofit2.Response;

public class AppointmentRecyclerViewAdapter extends RecyclerView.Adapter<AppointmentRecyclerViewAdapter.ViewHolder> {

    private final List<AppointmentsModel> mApps;
    private final IAppointmentsInteractionFragment mListener;

    public AppointmentRecyclerViewAdapter(List<AppointmentsModel> items, IAppointmentsInteractionFragment listener) {
        mApps = items;
        mListener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_appointment, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mApp = mApps.get(position);

        if(holder.mApp.customerModel != null) {
            holder.mCustomer.setText(holder.mApp.customerModel.toString());
        }else{
            holder.mCustomer.setText(R.string.error_customer_not_found);
        }

        //date
        holder.mDate.setText(Formatter.formatDate(holder.mApp.start));

        if(holder.mApp.serviceModel != null) {
            holder.mService.setText(holder.mApp.serviceModel.name);
        }else{
            holder.mService.setText(R.string.error_service_not_found);
        }

        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO
            }
        });
    }

    @Override
    public int getItemCount() {
        return mApps.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final TextView mCustomer;
        public final TextView mService;
        public final TextView mDate;
        public AppointmentsModel mApp;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            mCustomer = (TextView) view.findViewById(R.id.app_customer);
            mService = (TextView) view.findViewById(R.id.app_service);
            mDate = (TextView) view.findViewById(R.id.app_date);
        }
    }
}
