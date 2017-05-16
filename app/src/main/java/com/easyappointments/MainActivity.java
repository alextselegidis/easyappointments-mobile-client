package com.easyappointments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import com.easyappointments.remote.ea.model.ws.AppointmentsModel;
import com.easyappointments.remote.ea.model.ws.CustomerModel;

public class MainActivity extends AppCompatActivity {
    private AppointmentFragment appsFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {

            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.navigation_home:
                        getSupportFragmentManager().beginTransaction().replace(R.id.main_content, appsFragment).commit();
                        return true;
                    case R.id.navigation_dashboard:
                        return true;
                    case R.id.navigation_notifications:
                        return true;
                }
                return false;
            }

        });

        appsFragment = new AppointmentFragment();
        getSupportFragmentManager().beginTransaction().replace(R.id.main_content, appsFragment).commit();
    }
}
