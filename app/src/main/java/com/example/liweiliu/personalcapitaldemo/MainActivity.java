package com.example.liweiliu.personalcapitaldemo;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        RelativeLayout relativeLayout = findViewById(R.id.relative_layout);

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

        mRecyclerView = (RecyclerView) findViewById(R.id.myList);

        GridLayoutManager glm = new GridLayoutManager(this, 2);
        glm.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                switch (mAdapter.getItemViewType(position)) {
                    case RecyclerAdapter.TYPE_FULL:
                        return 2;
                    case RecyclerAdapter.TYPE_HALF:
                        return 1;
                    default:
                        return -1;
                }
            }
        });
        mRecyclerView.setLayoutManager(glm);
        mAdapter = new RecyclerAdapter(this, this);
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
        menu.add(Menu.NONE, 0, Menu.NONE, "Refresh");
        return true;
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
