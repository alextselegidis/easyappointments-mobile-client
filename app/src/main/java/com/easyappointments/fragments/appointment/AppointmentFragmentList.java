package com.easyappointments.fragments.appointment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.easyappointments.R;
import com.easyappointments.common.AsyncWSTask;
import com.easyappointments.db.SettingsModel;
import com.easyappointments.fragments.BaseFragmentList;
import com.easyappointments.fragments.adapter.AppointmentRecyclerViewAdapter;
import com.easyappointments.remote.ea.data.Options;
import com.easyappointments.remote.ea.model.ws.AppointmentsModel;
import com.easyappointments.remote.ea.service.AppointmentServiceFactory;
import com.easyappointments.remote.ea.service.appointment.AppointmentService;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Response;

public class AppointmentFragmentList extends BaseFragmentList<AppointmentsModel> {
    private NextAppointmentsTask nextAppsTask;
    private List<AppointmentsModel> lastAppointments = null;

    public AppointmentFragmentList() {
        super(R.string.title_fragment_next_appointment);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = super.onCreateView(inflater, container, savedInstanceState);

        if(refresh){
            nextAppsTask = new NextAppointmentsTask(recyclerView);
            nextAppsTask.execute((Void) null);
        }else{
            recyclerView.setAdapter(new AppointmentRecyclerViewAdapter(lastAppointments, mListener));
        }

        return v;
    }

    @Override
    public void onClick(AppointmentsModel item) {

    }

    @Override
    public boolean onLongClick(AppointmentsModel item) {
        return false;
    }

    class NextAppointmentsTask extends AsyncWSTask {
        protected NextAppointmentsTask(View v) {
            super(v);
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            showProgress(true);

            SettingsModel settings = SettingsModel.loadSettings();

            Map<String, String> filters = new HashMap<>();
            filters.put(Options.sort, Options.sort_desc + AppointmentsModel.fields.start.toString());
            filters.put(Options.page, "1");
            filters.put(Options.length, Integer.toString(settings.countNextAppointments));

            try {
                AppointmentService appService = AppointmentServiceFactory.getInstance();

                Response<List<AppointmentsModel>> appsResp = appService.get(filters).execute();
                lastAppointments = appsResp.body();

                if(lastAppointments == null)
                    errorMessage = getString(R.string.unknown_error);

                return lastAppointments != null;
            } catch (IOException e) {
                lastAppointments = new ArrayList<>(0);
                errorMessage = getString(R.string.error_network);
                return false;
            }
        }

        @Override
        protected void onPostExecute(Boolean success) {
            super.onPostExecute(success);

            nextAppsTask = null;
            showProgress(false);

            if (success) {
                refresh = false;
                ((RecyclerView)mView).setAdapter(new AppointmentRecyclerViewAdapter(lastAppointments, mListener));
            }
        }

        @Override
        protected void onCancelled() {
            super.onCancelled();

            nextAppsTask = null;
            showProgress(false);
        }
    }
}
