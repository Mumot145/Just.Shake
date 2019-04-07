package com.patrickmumot.eatingdice.Elements;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.util.Log;

import com.patrickmumot.eatingdice.R;
/**
 * An extension of the seek bar which adds the progress text to the left of the seek bar thumb. (if progress bar 
 * is determinate else a constant value of 0 is shown so use this view only when using determinate progress bar).
 */
public class MySeekBar extends android.support.v7.widget.AppCompatSeekBar {
    private Paint seekBarHintPaint;
    private Paint backgroundPaint;
    private int hintTextColor;
    private float hintTextSize;

    public MySeekBar(Context context) {
        super(context);
        init();
    }

    public MySeekBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray a = context.getTheme().obtainStyledAttributes(
                attrs,
                R.styleable.MySeekBar,
                0, 0);

        try {
            hintTextColor = a.getColor(R.styleable.MySeekBar_hint_text_color, 0);
            hintTextSize = a.getDimension(R.styleable.MySeekBar_hint_text_size, 0);
        } finally {
            a.recycle();
        }

        init();
    }

    public MySeekBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @TargetApi(21)
    public MySeekBar(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr);

        TypedArray a = context.getTheme().obtainStyledAttributes(
                attrs,
                R.styleable.MySeekBar,
                0, 0);

        try {
            hintTextColor = a.getColor(R.styleable.MySeekBar_hint_text_color, 0);
            hintTextSize = a.getDimension(R.styleable.MySeekBar_hint_text_size, 0);
        } finally {
            a.recycle();
        }
        init();
    }

    private void init() {


        backgroundPaint = new Paint();
        backgroundPaint.setColor(Color.BLACK);
      //  backgroundPaint.setStrokeWidth(50);
        //backgroundPaint.set

        seekBarHintPaint = new TextPaint();
        seekBarHintPaint.setColor(hintTextColor);
        seekBarHintPaint.setTextAlign(Paint.Align.CENTER);
        seekBarHintPaint.setTextSize(hintTextSize);

    }

    @Override
    protected synchronized void onDraw(Canvas canvas) {
        super.onDraw(canvas);


        Log.i("progress", this.getProgress()+"");
        Log.i("progress / max", (double) this.getProgress() / this.getMax()+"");
        Log.i("width", this.getWidth()+"");
        double locationOfText = (double) this.getProgress() / this.getMax();
        if(locationOfText < 0.02){
            locationOfText = 0.03;
        } else if(locationOfText > 0.98){
            locationOfText = 0.98;
        }
        int thumb_x = (int) (locationOfText * (double) (this.getWidth() * 0.93) + (2/locationOfText));
        Log.i("result", thumb_x+"");
        int middle = getHeight() - 3;


        Paint paint = new Paint();
        Paint.FontMetrics fm = new Paint.FontMetrics();
        paint.setColor(Color.WHITE);
        paint.getFontMetrics(fm);
        int margin = 16;
        float textLength = paint.measureText(String.valueOf(getProgress()));
        float kmTextLength =  paint.measureText("km");
        Log.i("text leng", textLength+"");
        canvas.drawRect(thumb_x - textLength - kmTextLength +  - margin, 20,
                thumb_x + textLength + kmTextLength + margin, 50
                        + margin, paint);

        canvas.drawText(String.valueOf(getProgress()) + "km", thumb_x, 50, seekBarHintPaint);

    }
}