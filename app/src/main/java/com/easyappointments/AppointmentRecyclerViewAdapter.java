package com.easyappointments;

import com.easyappointments.common.Formatter;
import com.easyappointments.remote.ea.model.ws.AppointmentsModel;

import java.util.List;

public class AppointmentRecyclerViewAdapter extends BaseRecyclerViewAdapter<AppointmentsModel, IActionFragment<AppointmentsModel>>  {


    public AppointmentRecyclerViewAdapter(List<AppointmentsModel> items, IActionFragment<AppointmentsModel> listener) {
        super(items, listener);
    }

    @Override
    public void onBindViewHolder(ViewHolder<AppointmentsModel> holder, int position) {
        super.onBindViewHolder(holder, position);

        holder.model = mItems.get(position);

        if(holder.model.getCustomerModel() != null) {
            holder.mTitle.setText(holder.model.getCustomerModel().toString());
        }else{
            holder.mTitle.setText(R.string.not_available);
        }

        holder.mSubRight.setText(Formatter.formatDate(holder.model.start));

        if(holder.model.getServiceModel() != null) {
            holder.mSubtitle.setText(holder.model.getServiceModel().name);
        }
    }
}
