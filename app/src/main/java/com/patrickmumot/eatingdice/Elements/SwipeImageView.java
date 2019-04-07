package com.patrickmumot.eatingdice.Elements;

import android.content.Context;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;

public class SwipeImageView extends android.support.v7.widget.AppCompatImageView {
    GestureDetector mGestureDetector;
    public SwipeImageView(Context context) {
        super(context);
    }

    public SwipeImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return mGestureDetector.onTouchEvent(event);
    }

    public void setmGestureDetector(GestureDetector mGestureDetector) {
        this.mGestureDetector = mGestureDetector;
    }
}
