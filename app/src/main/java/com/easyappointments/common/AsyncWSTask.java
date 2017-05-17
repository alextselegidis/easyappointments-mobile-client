package com.easyappointments.common;

import android.content.Context;
import android.os.AsyncTask;
import android.support.design.widget.Snackbar;
import android.view.View;

import com.easyappointments.R;

/**
 * Created by matte on 17/05/2017.
 */

public abstract class AsyncWSTask extends AsyncTask<Void, Void, Boolean> {

    protected final View mView;
    protected final Context mContext;
    protected String errorMessage = null;

    protected AsyncWSTask(View v){
        mView = v;
        mContext = v.getContext();
    }

    @Override
    protected void onPostExecute(Boolean success) {
        if (!success) {
            Snackbar.make(mView, errorMessage, Snackbar.LENGTH_SHORT).show();
        }
    }
}
