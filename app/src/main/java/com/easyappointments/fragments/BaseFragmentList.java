package com.easyappointments.fragments;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.easyappointments.R;
import com.easyappointments.remote.ea.model.ws.BaseModel;

/**
 * Created by matte on 16/05/2017.
 */

public abstract class BaseFragmentList<T extends BaseModel> extends Fragment implements IActionFragment<T> {
    private final int resourceRecyclerView;
    private final int resourceProgressBar;
    private final int resourceLayout;

    protected IActionFragment<T> mListener;
    protected Context context;
    protected boolean refresh = true;

    private int titleRes;
    protected RecyclerView recyclerView;
    private ProgressBar progressBar;

    protected BaseFragmentList(int resourceTitle, int resourceLayout, int resourceRecyclerView, int resourceProgressBar){
        this.titleRes = resourceTitle;
        this.resourceRecyclerView=resourceRecyclerView;
        this.resourceProgressBar = resourceProgressBar;
        this.resourceLayout=resourceLayout;
    }

    protected BaseFragmentList(int resourceTitle){
        this(resourceTitle,
                R.layout.fragment_list,
                R.id.recyclerView_list,
                R.id.fragment_progressbar);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mListener = this;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(resourceLayout, container, false);

        this.context = view.getContext();

        this.recyclerView = (RecyclerView) view.findViewById(resourceRecyclerView);
        this.recyclerView.setLayoutManager(new LinearLayoutManager(context));

        this.progressBar = (ProgressBar) view.findViewById(resourceProgressBar);

        return view;
    }

    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
        setFragmentTitle();
    }

    public void setFragmentTitle(){
        getActivity().setTitle(titleRes);
    }

    protected void showProgress(final boolean show) {

        int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

        this.recyclerView.setVisibility(show ? View.GONE : View.VISIBLE);
        this.recyclerView.animate().setDuration(shortAnimTime).alpha(
                show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                recyclerView.setVisibility(show ? View.GONE : View.VISIBLE);
            }
        });

        this.progressBar.setVisibility(show ? View.VISIBLE : View.GONE);
        this.progressBar.animate().setDuration(shortAnimTime).alpha(
                show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                progressBar.setVisibility(show ? View.VISIBLE : View.GONE);
            }
        });
    }
}
