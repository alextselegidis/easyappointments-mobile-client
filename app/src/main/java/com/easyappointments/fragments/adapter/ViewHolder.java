package com.easyappointments.fragments.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.activeandroid.Model;
import com.easyappointments.R;
import com.easyappointments.remote.ea.model.ws.BaseModel;
import com.easyappointments.remote.ea.model.ws.CustomerModel;

/**
 * Created by matte on 16/05/2017.
 */

public class ViewHolder<T extends BaseModel> extends RecyclerView.ViewHolder {
    public final View mView;
    public final TextView mTitle;
    public final TextView mSubtitle;
    public final TextView mSubRight;
    public T model;

    public ViewHolder(View view) {
        super(view);
        mView = view;
        mTitle = (TextView) view.findViewById(R.id.app_customer);
        mSubtitle = (TextView) view.findViewById(R.id.app_service);
        mSubRight = (TextView) view.findViewById(R.id.app_date);
    }
}