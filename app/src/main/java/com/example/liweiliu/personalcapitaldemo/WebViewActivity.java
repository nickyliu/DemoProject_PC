package com.example.liweiliu.personalcapitaldemo;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.Gravity;
import android.webkit.WebView;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.*;
import android.widget.TextView;

public class WebViewActivity extends AppCompatActivity {
    private WebView mWebView;
    public static final String INTENT_TITLE = "title";
    public static final String INTENT_URL = "url";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        String url = intent.getStringExtra(INTENT_URL);
        String title = intent.getStringExtra(INTENT_TITLE);

        RelativeLayout relativeLayout = new RelativeLayout(this);
        LayoutParams layoutParams = new LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.MATCH_PARENT);
        relativeLayout.setLayoutParams(layoutParams);

        mWebView = new WebView(this);
        mWebView.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT,
                LayoutParams.MATCH_PARENT));

        relativeLayout.addView(mWebView);

        setContentView(relativeLayout);

        // Custom Action Bar
        ActionBar actionBar = getSupportActionBar();
        TextView tv = new TextView(this);
        LayoutParams lp = new LayoutParams(
                LayoutParams.MATCH_PARENT,
                LayoutParams.WRAP_CONTENT);

        tv.setLayoutParams(lp);
        tv.setText(title);
        tv.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20);
        tv.setGravity(Gravity.CENTER_VERTICAL | Gravity.CENTER_HORIZONTAL);

        tv.setTextColor(Color.DKGRAY);
        tv.setBackgroundColor(Color.WHITE);

        actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);

        ColorDrawable colorDrawable = new ColorDrawable(Color.parseColor("#FFFFFF"));
        actionBar.setBackgroundDrawable(colorDrawable);
        actionBar.setElevation(0);
        actionBar.setCustomView(tv);

        mWebView.loadUrl(url);
    }
}