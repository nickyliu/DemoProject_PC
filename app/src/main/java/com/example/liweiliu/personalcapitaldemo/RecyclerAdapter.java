package com.example.liweiliu.personalcapitaldemo;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.v4.util.LruCache;
import android.support.v7.widget.LinearLayoutCompat.LayoutParams;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import java.io.InputStream;
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
    private LruCache<String, Bitmap> mMemoryCache;

    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    public RecyclerAdapter(Context context, OnItemClickListener listener,LruCache<String, Bitmap> memoryCache) {
        mContext = context;
        mListener = listener;
        mMemoryCache = memoryCache;
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

        // load image
        String imageUrl = item.getImage();
        final Bitmap bitmap = getBitmapFromMemCache(imageUrl);
        if (bitmap != null) {
            holder.customView.setBitmap(bitmap);
        } else {
            new ImageDownloader(holder.customView).execute(imageUrl);
        }
        if (position ==0) {
            // set summary
            holder.customView.setDescription(item.getDescription());
        } else {
            holder.customView.setDescription("");
        }
        holder.customView.setText(item.getTitle());
        holder.customView.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, Util.isTablet(mContext) ? VIEW_HEIGHT_TABLET : VIEW_HEIGHT_PHONE));

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

    public void addBitmapToMemoryCache(String key, Bitmap bitmap) {
        if (getBitmapFromMemCache(key) == null) {
            mMemoryCache.put(key, bitmap);
        }
    }

    public Bitmap getBitmapFromMemCache(String key) {
        return mMemoryCache.get(key);
    }

    private class ImageDownloader extends AsyncTask<String, Void, Bitmap> {
        CustomView mView;

        public ImageDownloader(CustomView imageView) {
            this.mView = imageView;
        }

        protected Bitmap doInBackground(String... urls) {
            String urldisplay = urls[0];
            Bitmap bitmap = null;
            try {
                InputStream in = new java.net.URL(urldisplay).openStream();
                bitmap = BitmapFactory.decodeStream(in);
                addBitmapToMemoryCache(urldisplay, bitmap);

            } catch (Exception e) {
                Log.e("Error", e.getMessage());
                e.printStackTrace();
            }
            return bitmap;
        }

        protected void onPostExecute(Bitmap result) {

            mView.setBitmap(result);
        }
    }
}