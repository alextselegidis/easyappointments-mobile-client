package com.easyappointments.detail;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.support.design.widget.TabLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.easyappointments.R;
import com.easyappointments.remote.ea.model.ws.AppointmentsModel;
import com.easyappointments.remote.ea.model.ws.BaseModel;
import com.easyappointments.remote.ea.model.ws.CustomerModel;
import com.easyappointments.remote.ea.model.ws.ServiceModel;

abstract class TabbedActivity<T extends BaseModel> extends AppCompatActivity {

    public static final String BUNDLE_MODEL_ID = "model_id";

    protected ViewPager mViewPager;
    private ProgressBar progressbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tabbed);

        progressbar = (ProgressBar) findViewById(R.id.tab_progress);

        Toolbar toolbar = (Toolbar) findViewById(R.id.tab_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mViewPager = (ViewPager) findViewById(R.id.tab_container);
        mViewPager.setAdapter(getFragmentPagerAdapter());

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs_layout);
        tabLayout.setupWithViewPager(mViewPager);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.tab_fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }

    protected abstract CustomFragmentPagerAdapter getFragmentPagerAdapter();

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_tabbed, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.tab_action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    protected void showProgress(final boolean show) {

        int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

        mViewPager.setVisibility(show ? View.GONE : View.VISIBLE);
        mViewPager.animate().setDuration(shortAnimTime).alpha(
                show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                mViewPager.setVisibility(show ? View.GONE : View.VISIBLE);
            }
        });

        progressbar.setVisibility(show ? View.VISIBLE : View.GONE);
        progressbar.animate().setDuration(shortAnimTime).alpha(
                show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                progressbar.setVisibility(show ? View.VISIBLE : View.GONE);
            }
        });
    }

    public static class AppointmentPlaceholderFragment extends Fragment {

        private AppointmentsModel model;

        public AppointmentPlaceholderFragment() {
        }

        public static AppointmentPlaceholderFragment newInstance(AppointmentsModel model){
            AppointmentPlaceholderFragment fragment = new AppointmentPlaceholderFragment();
            fragment.model = model;
            return fragment;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_tab_appointment, container, false);

            TextView txtapp = (TextView) rootView.findViewById(R.id.txt_app);
            txtapp.setText(Integer.toString(model.customerId));

            return rootView;
        }
    }

    public static class CustomerPlaceholderFragment extends Fragment {

        private CustomerModel model;

        public CustomerPlaceholderFragment() {
        }

        public static CustomerPlaceholderFragment newInstance(CustomerModel model){
            CustomerPlaceholderFragment fragment = new CustomerPlaceholderFragment();
            fragment.model = model;
            return fragment;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_tab_customer, container, false);


            return rootView;
        }
    }

    public static class ServicePlaceholderFragment extends Fragment {

        private ServiceModel model;

        public ServicePlaceholderFragment() {
        }

        public static ServicePlaceholderFragment newInstance(ServiceModel model){
            ServicePlaceholderFragment fragment = new ServicePlaceholderFragment();
            fragment.model = model;
            return fragment;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_tab_service, container, false);


            return rootView;
        }
    }



    abstract class CustomFragmentPagerAdapter extends FragmentPagerAdapter{

        protected abstract String getTab0Title();
        protected abstract String getTab1Title();
        protected abstract String getTab2Title();

        protected abstract AppointmentsModel getAppointmentModel();
        protected abstract CustomerModel getCustomerModel();
        protected abstract ServiceModel getServiceModel();

        public CustomFragmentPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            int section = position + 1;

            switch (position){
                case 0:
                    return AppointmentPlaceholderFragment.newInstance(getAppointmentModel());
                case 1:
                    return CustomerPlaceholderFragment.newInstance(getCustomerModel());
                case 2:
                    return ServicePlaceholderFragment.newInstance(getServiceModel());
            }

            return null;
        }

        @Override
        public int getCount() {
            return 3;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position){
                case 0:
                    return getTab0Title();
                case 1:
                    return getTab1Title();
                case 2:
                    return getTab2Title();
                default:
                    return null;
            }
        }
    }

}
