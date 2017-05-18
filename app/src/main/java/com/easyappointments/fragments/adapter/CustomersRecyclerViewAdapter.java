package com.easyappointments.fragments.adapter;

import com.easyappointments.fragments.IActionFragment;
import com.easyappointments.remote.ea.model.ws.CustomerModel;

import java.util.List;

public class CustomersRecyclerViewAdapter extends BaseRecyclerViewAdapter<CustomerModel, IActionFragment<CustomerModel>>  {

    public CustomersRecyclerViewAdapter(List<CustomerModel> items, IActionFragment<CustomerModel> listener) {
        super(items, listener);
    }

    @Override
    public void onBindViewHolder(ViewHolder<CustomerModel> holder, int position) {
        super.onBindViewHolder(holder, position);

        holder.model = mItems.get(position);
        holder.mTitle.setText(holder.model.toString());
        holder.mSubRight.setText(holder.model.phone);
        holder.mSubtitle.setText(holder.model.notes);
    }
}
