package com.patrickmumot.eatingdice.Elements;

import android.content.Context;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.widget.RelativeLayout;

public class SwipeRelativeLayout extends RelativeLayout {
    GestureDetector mGestureDetector;
    public SwipeRelativeLayout(Context context) {
        super(context);
    }

    public SwipeRelativeLayout(Context context, AttributeSet attrs) {
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
