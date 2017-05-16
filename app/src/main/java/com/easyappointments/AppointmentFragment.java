package com.easyappointments;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.easyappointments.remote.ea.data.Options;
import com.easyappointments.remote.ea.model.ws.AppointmentsModel;
import com.easyappointments.remote.ea.model.ws.ProviderModel;
import com.easyappointments.remote.ea.service.AppointmentServiceFactory;
import com.easyappointments.remote.ea.service.appointment.AppointmentService;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Response;

public class AppointmentFragment extends BaseFragment<AppointmentsModel> implements IActionFragment<AppointmentsModel>{
    private NextAppointmentsTask nextAppsTask;

    public AppointmentFragment() {
        super(R.string.title_fragment_next_appointment,
                R.layout.fragment_list,
                R.id.next_apps_list,
                R.id.next_apps_progress);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = super.onCreateView(inflater, container, savedInstanceState);

        nextAppsTask = new NextAppointmentsTask();
        nextAppsTask.execute((Void) null);

        return v;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        nextAppsTask=null;
    }

    @Override
    public void onClick(AppointmentsModel item) {

    }

    @Override
    public boolean onLongClick(AppointmentsModel item) {
        return false;
    }

    public class NextAppointmentsTask extends AsyncTask<Void, Void, Boolean> {
        private String errorMessage = getString(R.string.unknown_error);
        List<AppointmentsModel> lastAppointments = null;

        private ProviderModel providerModel;

        @Override
        protected Boolean doInBackground(Void... params) {
            showProgress(true);

            Map<String, String> filters = new HashMap<>();
            filters.put(Options.sort, Options.sort_desc + AppointmentsModel.fields.start.toString());
            filters.put(Options.page, "1");
            filters.put(Options.length, "1");

            try {
                AppointmentService appService = AppointmentServiceFactory.getInstance();

                Response<List<AppointmentsModel>> appsResp = appService.get(filters).execute();
                lastAppointments = appsResp.body();

                return lastAppointments != null;
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
                recyclerView.setAdapter(new AppointmentRecyclerViewAdapter(lastAppointments, mListener));
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
