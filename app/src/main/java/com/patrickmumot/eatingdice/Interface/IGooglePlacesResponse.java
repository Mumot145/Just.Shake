package com.patrickmumot.eatingdice.Interface;

import com.patrickmumot.eatingdice.GooglePlaceModels.GoogleDetails;
import com.patrickmumot.eatingdice.GooglePlaceModels.GooglePlace;

import java.util.List;

/**
 * Created by User on 3/22/2018.
 */

public interface IGooglePlacesResponse {
    void getGooglePlaces(List<GooglePlace> googlePlaces);
    void getGoogleDetails(GoogleDetails googleDetails);
    void getGooglePlacesMain(List<GooglePlace> googlePlaces);
}
