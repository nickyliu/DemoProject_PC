package com.example.liweiliu.personalcapitaldemo;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.text.Html;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

public class CustomView extends View {
    private Bitmap mBitmap;
    private String mText;
    private String mDescription;

    public CustomView(Context context) {
        super(context);
    }

    public void setText(String text) {
        mText = text;
        invalidate();
    }

    public void setBitmap(Bitmap bitmap) {
        mBitmap = bitmap;
        invalidate();
    }

    public void setDescription (String description) {
        mDescription = description;
        invalidate();
    }

    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        if (mBitmap == null) return;
        Drawable drawable = new BitmapDrawable(this.getResources(), mBitmap);
        drawable.setBounds(0, 0, this.getWidth(), this.getHeight()-80);
        drawable.draw(canvas);

        int h_title = this.getHeight()/3;
        int p_title = this.getHeight()/3;
        int summary_line_count = 2;
        int text_size_ratio = Util.isTablet(getContext())?1:2;
        if (mDescription!=null & !mDescription.isEmpty()) {
            TextView textView = new TextView(getContext());
            textView.setTextSize(12.0f/text_size_ratio);
            textView.setTextColor(Color.BLACK);
            textView.layout(0, 0, this.getWidth(), this.getHeight()/4);
            textView.setMaxLines(Util.isTablet(getContext())?3:2);
            textView.setEllipsize(TextUtils.TruncateAt.END);
            textView.setPadding(30,20,30,30);
            textView.setBackgroundColor(Color.WHITE);
            textView.setGravity(Gravity.TOP );
            textView.setText(Html.fromHtml(mDescription));
            canvas.save();
            canvas.translate(0,this.getHeight()-this.getHeight()/4);
            textView.draw(canvas);
            canvas.restore();

            h_title = this.getHeight()/5;
            p_title = this.getHeight()/4+this.getHeight()/5;
            summary_line_count =1;
        }
        // Display Title in 2 lines
        TextView textView = new TextView(getContext());
        textView.setText(Html.fromHtml(mText));
        textView.setTextSize(16.0F/text_size_ratio);
        textView.setTextColor(Color.BLACK);
        textView.layout(0, 0, this.getWidth(), h_title);
        textView.setEllipsize(TextUtils.TruncateAt.END);
        textView.setMaxLines(summary_line_count);
        textView.setPadding(30,20,30,30);
        textView.setBackgroundColor(Color.WHITE);
        textView.setGravity(Gravity.CENTER_VERTICAL );
        canvas.save();
        canvas.translate(0,this.getHeight()-p_title);
        textView.draw(canvas);
        canvas.restore();

        Paint framePaint = new Paint();
        Rect frame_rec = new Rect(0, 0, this.getWidth(), this.getHeight());
        framePaint.setStyle(Paint.Style.STROKE);
        framePaint.setColor(Color.GRAY);
        framePaint.setStrokeWidth(4);
        canvas.drawRect(frame_rec, framePaint);
    }

    public boolean onTouchEvent(MotionEvent event) {
        // TODO Auto-generated method stub
        Log.d("Hello Android", "Got a touch event: " + event.getAction());
        return super.onTouchEvent(event);
    }
}
