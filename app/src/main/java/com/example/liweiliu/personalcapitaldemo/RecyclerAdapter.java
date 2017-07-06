package com.example.liweiliu.personalcapitaldemo;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.LinearLayoutCompat.LayoutParams;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.MyViewHolder> {
    public static final int TYPE_FULL = 0;
    public static final int TYPE_HALF = 1;
    private static final int VIEW_HEIGHT_PHONE = 400;
    private static final int VIEW_HEIGHT_TABLET = 600;
    private final OnItemClickListener mListener;
    private Context mContext;
    private List<ListItem> mData = new ArrayList<>();

    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    public RecyclerAdapter(Context context, OnItemClickListener listener) {
        mContext = context;
        mListener = listener;
    }

    public RecyclerAdapter(Context context, ArrayList<ListItem> data, OnItemClickListener listener) {
        mContext = context;
        mListener = listener;
        mData = data;
    }

    public ListItem getItemByPosition(int position) {
        return mData.get(position);
    }

    public void setItems(List<ListItem> items) {
        mData = items;
        notifyDataSetChanged();
    }

    @Override
    public int getItemViewType(int position) {
        return position == 0 ? TYPE_FULL : TYPE_HALF;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = new CustomView(parent.getContext());
        MyViewHolder viewHolder = new MyViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {

        ListItem item = mData.get(position);
        new ImageDownloader(holder.customView)
                .execute(item.getImage());
        holder.customView.setText(item.getTitle());
        holder.customView.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, isTablet() ? VIEW_HEIGHT_TABLET : VIEW_HEIGHT_PHONE));

        holder.customView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onItemClick(position);
            }

        });
    }

    @Override
    public int getItemCount() {
        Log.d("Adapter size", Integer.toString(mData.size()));
        return mData.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        public CustomView customView;

        public MyViewHolder(View itemView) {
            super(itemView);
            customView = (CustomView) itemView;
        }
    }

    private boolean isTablet() {
        DisplayMetrics metrics = new DisplayMetrics();
        ((Activity) mContext).getWindowManager().getDefaultDisplay().getMetrics(metrics);

        float yInches = metrics.heightPixels / metrics.ydpi;
        float xInches = metrics.widthPixels / metrics.xdpi;
        double diagonalInches = Math.sqrt(xInches * xInches + yInches * yInches);
        if (diagonalInches >= 6.5) {
            return true;
        } else {
            return false;
        }
    }
}