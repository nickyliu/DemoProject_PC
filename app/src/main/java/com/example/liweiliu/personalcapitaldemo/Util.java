package com.example.liweiliu.personalcapitaldemo;

import android.app.Activity;
import android.content.Context;
import android.util.DisplayMetrics;

public class Util {

    static boolean isTablet(Context context) {
        DisplayMetrics metrics = new DisplayMetrics();
        ((Activity) context).getWindowManager().getDefaultDisplay().getMetrics(metrics);

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
