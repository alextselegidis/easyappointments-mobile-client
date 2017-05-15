package com.easyappointments;

import com.easyappointments.remote.ea.model.ws.AppointmentsModel;

/**
 * Created by matte on 10/05/2017.
 */

interface IAppointmentsInteractionFragment {
    void onSelect(AppointmentsModel app);
}
