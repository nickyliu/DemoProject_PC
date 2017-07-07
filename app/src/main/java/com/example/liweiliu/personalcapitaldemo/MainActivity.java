package com.example.liweiliu.personalcapitaldemo;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.util.LruCache;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity implements RecyclerAdapter.OnItemClickListener {

    private RecyclerView mRecyclerView;
    private ProgressBar mProgressBar;
    private RecyclerAdapter mAdapter;
    private LruCache<String, Bitmap> mMemoryCache;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        final int maxMemory = (int) (Runtime.getRuntime().maxMemory() / 1024);
        final int cacheSize = maxMemory / 8;

        mMemoryCache = new LruCache<String, Bitmap>(cacheSize) {
            @Override
            protected int sizeOf(String key, Bitmap bitmap) {
                return bitmap.getByteCount() / 1024;
            }
        };

        RelativeLayout relativeLayout = new RelativeLayout(this);
        LayoutParams layoutParams = new LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.MATCH_PARENT);
        mRecyclerView = new RecyclerView(this);
        relativeLayout.setLayoutParams(layoutParams);
        mRecyclerView.setLayoutParams(layoutParams);
        relativeLayout.addView(mRecyclerView);

        setContentView(relativeLayout);

        // Customize action bar
        ActionBar actionBar = getSupportActionBar();
        TextView tv = new TextView(this);

        LayoutParams lp = new LayoutParams(
                LayoutParams.MATCH_PARENT,
                LayoutParams.WRAP_CONTENT);

        tv.setLayoutParams(lp);
        tv.setText("Research & Insight");
        tv.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20);
        tv.setGravity(Gravity.CENTER_VERTICAL | Gravity.CENTER_HORIZONTAL);
        tv.setTextColor(Color.BLACK);
        tv.setBackgroundColor(Color.WHITE);

        actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        ColorDrawable colorDrawable = new ColorDrawable(Color.parseColor("#FFFFFF"));
        actionBar.setBackgroundDrawable(colorDrawable);
        actionBar.setElevation(0);
        actionBar.setCustomView(tv);

        final int span_count = Util.isTablet(this)?3:2;
        GridLayoutManager glm = new GridLayoutManager(this, span_count);
        glm.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                switch (mAdapter.getItemViewType(position)) {
                    case RecyclerAdapter.TYPE_FULL:
                        return span_count;
                    case RecyclerAdapter.TYPE_HALF:
                        return 1;
                    default:
                        return -1;
                }
            }
        });
        mRecyclerView.setLayoutManager(glm);
        mAdapter = new RecyclerAdapter(this, this,mMemoryCache);
        mRecyclerView.addItemDecoration(new SpacesItemDecoration(60));
        mRecyclerView.setAdapter(mAdapter);

        // Loading spinner
        mProgressBar = new ProgressBar(this, null, android.R.attr.progressBarStyleLarge);
        mProgressBar.setIndeterminate(true);
        mProgressBar.setVisibility(View.VISIBLE);
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(100, 100);
        params.addRule(RelativeLayout.CENTER_IN_PARENT);
        mProgressBar.setLayoutParams(params);
        relativeLayout.addView(mProgressBar);

        getFeed();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuItem delete_item = menu.add(0, 0, 0, "Refresh");
        delete_item.setIcon(android.R.drawable.ic_menu_rotate);
        delete_item.setShowAsActionFlags(MenuItem.SHOW_AS_ACTION_IF_ROOM);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case 0:
                Log.d("Menu", "clicked refresh");
                getFeed();
                return true;

            default:
                return false;
        }
    }

    private void getFeed() {
        mMemoryCache.evictAll();
        new InfoDownloader(mProgressBar, mAdapter).execute();
    }

    @Override
    public void onItemClick(int position) {
        ListItem item = mAdapter.getItemByPosition(position);
        Intent intent = new Intent(getBaseContext(), WebViewActivity.class);
        intent.putExtra(WebViewActivity.INTENT_TITLE, item.getTitle());
        intent.putExtra(WebViewActivity.INTENT_URL, item.getLink());
        startActivity(intent);
    }

    private class SpacesItemDecoration extends RecyclerView.ItemDecoration {

        private int halfSpace;

        public SpacesItemDecoration(int space) {
            this.halfSpace = space / 2;
        }

        @Override
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
            outRect.top = 20;
            outRect.bottom = 20;
            outRect.left = halfSpace;
            outRect.right = halfSpace;
        }
    }

}
