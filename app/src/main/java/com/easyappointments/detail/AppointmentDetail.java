package com.easyappointments.detail;

import android.content.Context;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.easyappointments.R;
import com.easyappointments.common.AsyncWSTask;
import com.easyappointments.db.SettingsModel;
import com.easyappointments.fragments.adapter.AppointmentRecyclerViewAdapter;
import com.easyappointments.remote.ea.data.Options;
import com.easyappointments.remote.ea.model.ws.AppointmentsModel;
import com.easyappointments.remote.ea.model.ws.BaseModel;
import com.easyappointments.remote.ea.model.ws.CustomerModel;
import com.easyappointments.remote.ea.model.ws.ServiceModel;
import com.easyappointments.remote.ea.service.AppointmentServiceFactory;
import com.easyappointments.remote.ea.service.appointment.AppointmentService;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Response;

/**
 * Created by matte on 19/05/2017.
 */

public class AppointmentDetail extends TabbedActivity<AppointmentsModel> {
    private AppointmentTask task;
    private AppointmentsModel appointmentModel;
    private CustomerModel customerModel;
    private ServiceModel serviceModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        int appointmentId = getIntent().getExtras().getInt(BUNDLE_MODEL_ID);

        task = new AppointmentTask(mViewPager, appointmentId);
        task.execute();
    }

    @Override
    protected CustomFragmentPagerAdapter getFragmentPagerAdapter() {
        return new CustomFragmentPagerAdapter(getSupportFragmentManager()) {

            @Override
            protected String getTab0Title() {
                return getString(R.string.tabbed_title_appointment);
            }

            @Override
            protected String getTab1Title() {
                return getString(R.string.tabbed_title_customer);
            }

            @Override
            protected String getTab2Title() {
                return getString(R.string.tabbed_title_service);
            }

            @Override
            protected AppointmentsModel getAppointmentModel() {
                return appointmentModel;
            }

            @Override
            protected CustomerModel getCustomerModel() {
                return customerModel;
            }

            @Override
            protected ServiceModel getServiceModel() {
                return serviceModel;
            }
        };
    }

    class AppointmentTask extends AsyncWSTask {
        private int appId;

        protected AppointmentTask(View v, int appId) {
            super(v);
            this.appId = appId;
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            showProgress(true);

            try {
                AppointmentService appService = AppointmentServiceFactory.getInstance();

                Response<AppointmentsModel> appsResp = appService.get(appId).execute();
                appointmentModel = appsResp.body();

                if(appointmentModel == null)
                    errorMessage = getString(R.string.unknown_error);

                return appointmentModel != null;
            } catch (IOException e) {
                errorMessage = getString(R.string.error_network);
                return false;
            }
        }

        @Override
        protected void onPostExecute(Boolean success) {
            super.onPostExecute(success);

            if (success) {
                customerModel = appointmentModel.getCustomerModel();
                serviceModel = appointmentModel.getServiceModel();
            }

            task = null;
            showProgress(false);
        }

        @Override
        protected void onCancelled() {
            super.onCancelled();

            task = null;
            showProgress(false);
        }
    }
}

