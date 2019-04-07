package com.patrickmumot.eatingdice.Interface;

import com.patrickmumot.eatingdice.GooglePlaceModels.GooglePhoto;

/**
 * Created by User on 5/19/2018.
 */

public interface IPhotoInteraction {
    void PhotoTapped(GooglePhoto photo);
    void PhotoSwiped(boolean up);
}
