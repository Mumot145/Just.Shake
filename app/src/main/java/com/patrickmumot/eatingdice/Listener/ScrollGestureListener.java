package com.patrickmumot.eatingdice.Listener;

import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;

import com.patrickmumot.eatingdice.Enum.detailType;
import com.patrickmumot.eatingdice.GooglePlaceModels.GooglePhoto;
import com.patrickmumot.eatingdice.Interface.IScrollListener;
import com.patrickmumot.eatingdice.MainActivityRemake;

/**
 * Created by CoXier on 17-2-21.
 */

public class ScrollGestureListener extends GestureDetector.SimpleOnGestureListener {
    private static final String TAG = "SimpleGestureListener";
    private IScrollListener mListener;
    private GooglePhoto photo;
    private detailType detail = null;
    private int index = 0;

    @Override
    public boolean onDown(MotionEvent e) {

        return true;
    }

    @Override
    public boolean onSingleTapConfirmed(MotionEvent e){
        if (mListener == null)
            return true;

        mListener.onTapped(detail, photo, index);
        return true;
    }

    public void tappedBg(){
        mListener.onTapped(detail, photo, index);
    }

    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
        if (mListener == null)
            return true;

        if (e1.getY() > e2.getY()) {
            mListener.onScrollVertical(1, detail);
        }

        if (e1.getY() < e2.getY()) {
            mListener.onScrollVertical(-1, detail);
        }
        return super.onFling(e1, e2, velocityX, velocityY);
    }

    @Override
    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
        if (mListener == null)
            return true;


        if (distanceX == 0 && Math.abs(distanceY) > 1){
            detailType type;

            if(MainActivityRemake.lastDetailType == null){
                type = detailType.Review;
            } else {
                type = MainActivityRemake.lastDetailType;
            }
            Log.i("lastdetails", type + "");
            mListener.onScrollVertical(distanceY, type);
        }

        if (distanceY == 0 && Math.abs(distanceX) > 1){
            mListener.onScrollHorizontal(distanceX);
        }
        return true;
    }


    public void setInfo(IScrollListener mListener, GooglePhoto photo, detailType type, int index) {
        this.mListener = mListener;
        this.detail = type;
        this.photo = photo;
        this.index = index;
    }
}
