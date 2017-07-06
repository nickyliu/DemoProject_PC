package com.example.liweiliu.personalcapitaldemo;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

public class CustomView extends View {
    private Drawable mImageDrawable;
    private String mText = "loading";

    public CustomView(Context context) {
        super(context);
    }

    public void setText(String text) {
        mText = text;
        invalidate();
    }

    public void setDrawable(Drawable drawable) {
        mImageDrawable = drawable;
        invalidate();
    }

    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (mImageDrawable == null) return;
        mImageDrawable.setBounds(0, 0, this.getWidth(), this.getHeight());
        mImageDrawable.draw(canvas);

        Paint txPaint = new Paint();
        Rect r = new Rect(0, this.getHeight() - this.getHeight() / 3, this.getWidth(), this.getHeight());
        txPaint.setStyle(Paint.Style.FILL);
        txPaint.setColor(Color.WHITE);
        canvas.drawRect(r, txPaint);

        Paint bgPaint = new Paint();
        Rect bg_r = new Rect(0, 0, this.getWidth(), this.getHeight());
        bgPaint.setStyle(Paint.Style.STROKE);
        bgPaint.setColor(Color.GRAY);
        bgPaint.setStrokeWidth(4);
        canvas.drawRect(bg_r, bgPaint);


        System.out.println("Drawing text");
        Paint paint = new Paint(Paint.LINEAR_TEXT_FLAG);
        paint.setColor(Color.BLACK);
        paint.setTextSize(32.0F);
        paint.setTextAlign(Paint.Align.CENTER);
        canvas.drawText(mText, 0, mText.length() > 35 ? 35 : mText.length(), this.getWidth() / 2, this.getHeight() - this.getHeight() / 6, paint);
    }

    public boolean onTouchEvent(MotionEvent event) {
        // TODO Auto-generated method stub
        Log.d("Hello Android", "Got a touch event: " + event.getAction());
        return super.onTouchEvent(event);
    }
}
