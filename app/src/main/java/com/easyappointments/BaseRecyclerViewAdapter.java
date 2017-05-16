package com.easyappointments;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.easyappointments.remote.ea.model.ws.BaseModel;

import java.util.List;

/**
 * Created by matte on 16/05/2017.
 */

public abstract class BaseRecyclerViewAdapter<T extends BaseModel, I extends IActionFragment<T>>
        extends RecyclerView.Adapter<ViewHolder<T>> {
    private final int mLayout;
    protected List<T> mItems;
    protected I mListener;

    public BaseRecyclerViewAdapter(List<T> items, I listener){
        this(items, listener, R.layout.fragment_item);
    }

    public BaseRecyclerViewAdapter(List<T> items, I listener, int resLayout){
        mItems = items;
        mListener = listener;
        mLayout = resLayout;
    }

    @Override
    public int getItemCount() {
        return mItems.size();
    }

    @Override
    public ViewHolder<T> onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(mLayout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder<T> holder, int position) {
        final T model = mItems.get(position);

        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onClick(model);
            }
        });

        holder.mView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                return mListener.onLongClick(model);
            }
        });
    }
}

interface IActionFragment<T extends BaseModel>{
    void onClick(T item);
    boolean onLongClick(T item);
}
