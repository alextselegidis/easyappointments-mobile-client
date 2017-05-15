package com.easyappointments;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.easyappointments.common.task.CustomerTask;
import com.easyappointments.common.task.ServiceTask;
import com.easyappointments.remote.ea.data.Options;
import com.easyappointments.remote.ea.model.ws.AppointmentsModel;
import com.easyappointments.remote.ea.model.ws.CustomerModel;
import com.easyappointments.remote.ea.model.ws.ProviderModel;
import com.easyappointments.remote.ea.model.ws.ServiceModel;
import com.easyappointments.remote.ea.service.AppointmentServiceFactory;
import com.easyappointments.remote.ea.service.ProviderServiceFactory;
import com.easyappointments.remote.ea.service.appointment.AppointmentService;
import com.easyappointments.remote.ea.service.provider.ProviderService;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import retrofit2.Response;

public class AppointmentFragment extends Fragment {

    private IAppointmentsInteractionFragment mListener;
    private AppointmentService appService;
    private NextAppointmentsTask nextAppsTask;
    private RecyclerView recyclerView;
    private ProgressBar progressBar;

    public AppointmentFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        appService = AppointmentServiceFactory.getInstance();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_appointment_list, container, false);

        Context context = view.getContext();

        recyclerView = (RecyclerView) view.findViewById(R.id.next_apps_list);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));

        progressBar = (ProgressBar) view.findViewById(R.id.next_apps_progress);

        nextAppsTask = new NextAppointmentsTask(context);
        nextAppsTask.execute((Void) null);

        return view;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mListener = (IAppointmentsInteractionFragment) context;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    private void showProgress(final boolean show) {

        int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

        recyclerView.setVisibility(show ? View.GONE : View.VISIBLE);
        recyclerView.animate().setDuration(shortAnimTime).alpha(
                show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                recyclerView.setVisibility(show ? View.GONE : View.VISIBLE);
            }
        });

        progressBar.setVisibility(show ? View.VISIBLE : View.GONE);
        progressBar.animate().setDuration(shortAnimTime).alpha(
                show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                progressBar.setVisibility(show ? View.VISIBLE : View.GONE);
            }
        });
    }

    public class NextAppointmentsTask extends AsyncTask<Void, Void, Boolean> {
        private String errorMessage = getString(R.string.unknown_error);
        private final Context ctx;
        List<AppointmentsModel> lastAppointments = null;

        private ProviderModel providerModel;

        NextAppointmentsTask(Context ctx) {
            this.ctx=ctx;
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            showProgress(true);

            Map<String, String> filters = new HashMap<>();
            filters.put(Options.sort, Options.sort_asc + AppointmentsModel.fields.start.toString());

            try {
                Response<List<AppointmentsModel>> appsResp = appService.get(filters).execute();
                lastAppointments = appsResp.body();

                return true;
            } catch (IOException e) {
                lastAppointments = new ArrayList<>(0);
                errorMessage = getString(R.string.error_network);
                return false;
            }
        }

        @Override
        protected void onPostExecute(final Boolean success) {
            nextAppsTask = null;
            showProgress(false);

            if (success) {
                int nAppointments = lastAppointments.size();

                Integer[] customersIds = new Integer[nAppointments];
                for(int i = 0; i < nAppointments; i++){
                    customersIds[i] = lastAppointments.get(i).customerId;
                }

                CustomerTask customerTask = new CustomerTask();
                customerTask.execute(customersIds);

                Integer[] servicesIds = new Integer[nAppointments];
                for(int i = 0; i < nAppointments; i++){
                    servicesIds[i] = lastAppointments.get(i).serviceId;
                }

                ServiceTask serviceTask = new ServiceTask();
                serviceTask.execute(servicesIds);

                try {
                    if(customerTask.get() && serviceTask.get()){
                        for(AppointmentsModel am : lastAppointments){
                            for(CustomerModel cf : customerTask.customers){
                                if(cf.id == am.customerId){
                                    am.customerModel = cf;
                                    break;
                                }
                            }

                            for(ServiceModel sf : serviceTask.services){
                                if(sf.id == am.serviceId){
                                    am.serviceModel = sf;
                                    break;
                                }
                            }
                        }

                        recyclerView.setAdapter(new AppointmentRecyclerViewAdapter(lastAppointments, mListener));
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                }
            } else {
                Snackbar.make(recyclerView, getString(R.string.error_load_appointments)+": "+errorMessage, Snackbar.LENGTH_SHORT).show();
            }
        }

        @Override
        protected void onCancelled() {
            nextAppsTask = null;
            showProgress(false);
        }
    }
}
