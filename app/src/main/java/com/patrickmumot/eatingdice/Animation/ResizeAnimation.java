package com.patrickmumot.eatingdice.Animation;

import android.support.constraint.ConstraintLayout;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.Transformation;

/**
 * an animation for resizing the view.
 */
public class ResizeAnimation extends Animation {

    public static void resize(final View v, final int initHeight, final int initWidth, final int endHeight, final int endWidth) {
        //v.measure(ConstraintLayout.LayoutParams.MATCH_PARENT, ConstraintLayout.LayoutParams.WRAP_CONTENT);
        //final int targetHeight = height;
        //Log.i("targetHeight", targetHeight+"");
        // Older versions of android (pre API 21) cancel animations for views with a height of 0.
        //v.getLayoutParams().height = 1;
        //v.setVisibility(View.VISIBLE);
        Animation a = new Animation()
        {
            @Override
            protected void applyTransformation(float interpolatedTime, Transformation t) {
                float height =
                        (endHeight - initHeight) * interpolatedTime + initHeight;
                float width = (endWidth - initWidth) * interpolatedTime + initWidth;
                ViewGroup.LayoutParams p = v.getLayoutParams();
                p.height = (int) height;
                p.width = (int) width;
                v.requestLayout();
            }

            @Override
            public boolean willChangeBounds() {
                return true;
            }
        };
        Log.i("speed", (int)(endHeight / v.getContext().getResources().getDisplayMetrics().density * 2) + "");
        // 1dp/ms
        a.setDuration(400);
        v.startAnimation(a);
    }

    public static void collapse(final View v, int height) {
        final int initialHeight = v.getMeasuredHeight();
        Log.i("height", initialHeight+"");
        Animation a = new Animation()
        {
            @Override
            protected void applyTransformation(float interpolatedTime, Transformation t) {
                Log.i("interpolatedTime", interpolatedTime+"");
                if(interpolatedTime == 1){
                    //v.setVisibility(View.GONE);
                }else{
                    v.getLayoutParams().height = initialHeight - (int)(initialHeight * interpolatedTime);
                    v.requestLayout();
                }
            }

            @Override
            public boolean willChangeBounds() {
                return true;
            }
        };

        // 1dp/ms
        a.setDuration((int)(initialHeight / v.getContext().getResources().getDisplayMetrics().density));
        v.startAnimation(a);
    }
}