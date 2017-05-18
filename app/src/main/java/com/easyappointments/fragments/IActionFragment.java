package com.easyappointments.fragments;

import com.easyappointments.remote.ea.model.ws.BaseModel;

/**
 * Created by matte on 18/05/2017.
 */
public interface IActionFragment<T extends BaseModel>{
    void onClick(T item);
    boolean onLongClick(T item);
}
