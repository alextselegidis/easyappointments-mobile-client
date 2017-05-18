package com.easyappointments.fragments.adapter;

import com.easyappointments.common.Formatter;
import com.easyappointments.fragments.IActionFragment;
import com.easyappointments.remote.ea.model.ws.ServiceModel;

import java.util.List;

/**
 * Created by matte on 17/05/2017.
 */

public class ServicesRecyclerViewAdapter extends BaseRecyclerViewAdapter<ServiceModel, IActionFragment<ServiceModel>> {

    public ServicesRecyclerViewAdapter(List<ServiceModel> items, IActionFragment<ServiceModel> listener) {
        super(items, listener);
    }

    @Override
    public void onBindViewHolder(ViewHolder<ServiceModel> holder, int position) {
        super.onBindViewHolder(holder, position);

        holder.model = mItems.get(position);
        holder.mTitle.setText(holder.model.name);
        holder.mSubRight.setText(holder.model.duration + " - " + Formatter.formatPrice(holder.model.price, holder.model.currency));
        holder.mSubtitle.setText(holder.model.getCategory().name);
    }
}
