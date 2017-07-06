package com.example.liweiliu.personalcapitaldemo;

import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.util.Log;

import java.io.InputStream;

public class ImageDownloader extends AsyncTask<String, Void, Drawable> {
    CustomView mView;

    public ImageDownloader(CustomView imageView) {
        this.mView = imageView;
    }

    protected Drawable doInBackground(String... urls) {
        String urldisplay = urls[0];
        Drawable drawable = null;
        try {
            InputStream in = new java.net.URL(urldisplay).openStream();
            drawable = Drawable.createFromStream(in, "src");

        } catch (Exception e) {
            Log.e("Error", e.getMessage());
            e.printStackTrace();
        }
        return drawable;
    }

    protected void onPostExecute(Drawable result) {

        mView.setDrawable(result);
    }
}