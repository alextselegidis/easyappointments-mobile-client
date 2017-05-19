package com.easyappointments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import com.easyappointments.fragments.AppointmentFragmentList;
import com.easyappointments.fragments.CustomerFragmentList;
import com.easyappointments.fragments.ServiceFragmentList;

public class MainActivity extends AppCompatActivity {
    public AppointmentFragmentList getAppsFragment() {
        if(appsFragment == null)
            appsFragment = new AppointmentFragmentList();
        return appsFragment;
    }

    public CustomerFragmentList getCustomersFragment() {
        if(customersFragment == null)
            customersFragment = new CustomerFragmentList();
        return customersFragment;
    }

    public ServiceFragmentList getServicesFragment() {
        if(servicesFragment == null)
            servicesFragment = new ServiceFragmentList();
        return servicesFragment;
    }

    private AppointmentFragmentList appsFragment;
    private CustomerFragmentList customersFragment;
    private ServiceFragmentList servicesFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getSupportActionBar().setDisplayHomeAsUpEnabled(false);

        final Bundle b = getIntent().getExtras();

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {

            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                Fragment frag = null;

                switch (item.getItemId()) {
                    case R.id.navigation_next_appointments:
                        frag = getAppsFragment();
                        break;
                    case R.id.navigation_customers:
                        frag = getCustomersFragment();
                        break;
                    case R.id.navigation_services:
                        frag = getServicesFragment();
                        frag.setArguments(b);
                        break;
                    default:
                        return false;
                }

                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.main_content, frag)
                        .commit();

                return true;
            }

        });

        appsFragment = new AppointmentFragmentList();
        getSupportFragmentManager().beginTransaction().replace(R.id.main_content, getAppsFragment()).commit();
    }
}
