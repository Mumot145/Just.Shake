package com.patrickmumot.eatingdice.Interface;

import com.patrickmumot.eatingdice.Enum.detailType;
import com.patrickmumot.eatingdice.GooglePlaceModels.GooglePhoto;

public interface IScrollListener{
    /**
     * left scroll dx >0
     * right scroll dx <0
     * @param dx
     */
    void onScrollHorizontal(float dx);

    /**
     * upward scroll dy > 0
     * downward scroll dy < 0
     * @param dy
     */
    void onScrollVertical(float dy, detailType detailType);
    void onTapped(detailType detailType, GooglePhoto photo, int index);
}