package com.patrickmumot.eatingdice.Parser;

import android.util.Log;

import com.patrickmumot.eatingdice.Controller.DataController;
import com.patrickmumot.eatingdice.GooglePlaceModels.GoogleDetails;
import com.patrickmumot.eatingdice.GooglePlaceModels.GooglePhoto;
import com.patrickmumot.eatingdice.GooglePlaceModels.GooglePlace;
import com.patrickmumot.eatingdice.MainActivityRemake;
import com.patrickmumot.eatingdice.Models.AddressComponents;
import com.patrickmumot.eatingdice.Models.Period;
import com.patrickmumot.eatingdice.Models.Review;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.List;

public class ParseJson {

    public static GooglePlace JsonToClosestPlace(JSONObject jsonObject){
        try {
            JSONArray arr = jsonObject.getJSONArray("results");
            JSONObject closestLocation = new JSONObject();

            closestLocation = arr.getJSONObject(0);
            GooglePlace closestGooglePlace = createGooglePlace(closestLocation);
            return closestGooglePlace;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return new GooglePlace();
    }

    public static GoogleDetails JsonToGooglePlaceDetails(JSONObject jsonObject){
        GoogleDetails result = new GoogleDetails();
        try {
            jsonObject = jsonObject.getJSONObject("result");

            result.Geometry.Longitude = jsonObject.getJSONObject("geometry").getJSONObject("location").getDouble("lng");
            result.Geometry.Latitude = jsonObject.getJSONObject("geometry").getJSONObject("location").getDouble("lat");
            result.Id = jsonObject.getString("id");
            result.Reference = jsonObject.getString("reference");
            result.Vicinity = jsonObject.getString("vicinity");

            if(jsonObject.has("rating"))
                result.Rating = jsonObject.getDouble("rating");

            JSONArray types = jsonObject.getJSONArray("types");
            for(int i = 0; i < types.length(); i++) {
                result.Types.add(types.getString(i));
            }

            if(jsonObject.has("international_phone_number"))
                result.InternationalPhoneNumber = jsonObject.getString("international_phone_number");

            if(jsonObject.has("formatted_phone_number"))
                result.FormattedPhoneNumber = jsonObject.getString("formatted_phone_number");

            if(jsonObject.has("formatted_address"))
                result.FormattedAddress = jsonObject.getString("formatted_address");

            if(jsonObject.has("opening_hours")){

                JSONObject hoursObject = jsonObject.getJSONObject("opening_hours");
                result.OpenNow = hoursObject.getBoolean("open_now");
                JSONArray periodsArray = hoursObject.getJSONArray("periods");
                for(int o = 0; o < periodsArray.length(); o++){
                    Period period = new Period();
                    JSONObject periodJSON = periodsArray.getJSONObject(o);
                    JSONObject closeJSON;

                    if(periodJSON.has("close")){
                        closeJSON = periodJSON.getJSONObject("close");
                        period.PeriodClose.Day = closeJSON.getInt("day");
                        period.PeriodClose.Time = closeJSON.getString("time");
                    }

                    JSONObject openJSON;
                    if(periodJSON.has("open")) {
                        openJSON = periodJSON.getJSONObject("open");
                        period.PeriodOpen.Day = openJSON.getInt("day");
                        period.PeriodOpen.Time = openJSON.getString("time");
                    }

                    result.OpenHours.Periods.add(period);
                }
                JSONArray weekdayText;
                if(hoursObject.has("weekday_text")){
                    weekdayText = hoursObject.getJSONArray("weekday_text");
                    for(int q = 0; q < weekdayText.length(); q++){
                        result.WeekdayText.add(weekdayText.getString(q));
                    }
                }
            }



            JSONArray addressArray = jsonObject.getJSONArray("address_components");
            for(int i = 0; i < addressArray.length(); i++){
                JSONObject object = addressArray.getJSONObject(i);
                AddressComponents addressComponents = new AddressComponents();
                addressComponents.LongName = object.getString("long_name");
                addressComponents.ShortName = object.getString("short_name");
                JSONArray typesArray = object.getJSONArray("types");
                for(int j = 0; j < typesArray.length(); j++){
                    addressComponents.Types.add(typesArray.getString(j));
                }
                result.AddressComponents.add(addressComponents);
            }


            if(jsonObject.has("photos")){
                JSONArray photosArray = jsonObject.getJSONArray("photos");
                for(int p = 0; (p < photosArray.length() && p < 6); p++){
                    GooglePhoto photo = new GooglePhoto();
                    JSONObject photoJSON = photosArray.getJSONObject(p);
                    photo.Height = photoJSON.getInt("height");
                    photo.HtmlAttributions = photoJSON.getString("html_attributions");
                    photo.PhotoReference = photoJSON.getString("photo_reference");
                    photo.Width = photoJSON.getInt("width");
                    result.Photos.add(photo);
                }

            }

            if(jsonObject.has("reviews")){
                JSONArray reviewArray = jsonObject.getJSONArray("reviews");
                for(int r = 0; r < reviewArray.length(); r++){
                    Review review = new Review();
                    JSONObject reviewJSON = reviewArray.getJSONObject(r);
                    review.AuthorName = reviewJSON.getString("author_name");
                    if(reviewJSON.has("author_url"))
                        review.AuthorUrl = reviewJSON.getString("author_url");
                    //review.Language = reviewJSON.getString("language");
                    if(reviewJSON.has("profile_photo_url"))
                        review.ProfilePhotoUrl = reviewJSON.getString("profile_photo_url");
                    review.Rating = reviewJSON.getDouble("rating");
                    review.RelativeTimeDescription = reviewJSON.getString("relative_time_description");
                    review.Text = reviewJSON.getString("text");
                    review.Time = reviewJSON.getInt("time");
                    result.Reviews.add(review);
                }
            }

            if(jsonObject.has("website"))
                result.WebsiteUrl = jsonObject.getString("website");

            return result;
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return new GoogleDetails();
    }

    public static List<GooglePlace> JsonToSimpleGooglePlaces(JSONObject jsonObject){
        List<GooglePlace> result = new ArrayList<GooglePlace>();

        try {
            if(jsonObject.has("next_page_token")){
                MainActivityRemake.searchedCuisine.setNextPage(jsonObject.getString("next_page_token"));
            } else {
                MainActivityRemake.searchedCuisine.end = true;
            }

            JSONArray array = jsonObject.getJSONArray("results");
            for(int i = 0; i < array.length(); i++){
                JSONObject object = array.getJSONObject(i);
                GooglePlace place = createSimpleGooglePlace(object);
                if(place != null)
                    result.add(createSimpleGooglePlace(object));
                else
                    continue;
            }
            return result;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return new ArrayList<GooglePlace>();
    }


    static private GooglePlace createSimpleGooglePlace(JSONObject jsonObject){
        GooglePlace googlePlace = new GooglePlace();
        try {
            googlePlace.PlaceId = jsonObject.getString("place_id");
            googlePlace.Name = jsonObject.getString("name");
            if(jsonObject.has("price_level"))
                googlePlace.PriceLevel = jsonObject.getInt("price_level");

            double lat = jsonObject.getJSONObject("geometry").getJSONObject("location").getDouble("lat");
            double lng = jsonObject.getJSONObject("geometry").getJSONObject("location").getDouble("lng");
            googlePlace.Distance = DataController.distance(lat, MainActivityRemake.myLocation.getLatitude(),
                    lng, MainActivityRemake.myLocation.getLongitude(),0,0)/ 1000;
            return googlePlace;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    static private GooglePlace createGooglePlace(JSONObject jsonObject){
        GooglePlace googlePlace;
        googlePlace = new GooglePlace();
        try {

            googlePlace.Name = jsonObject.getString("name");
            googlePlace.PlaceId = jsonObject.getString("place_id");
            double lat = jsonObject.getJSONObject("geometry").getJSONObject("location").getDouble("lat");
            double lng = jsonObject.getJSONObject("geometry").getJSONObject("location").getDouble("lng");

            googlePlace.Distance = DataController.distance(lat, MainActivityRemake.myLocation.getLatitude(),
                    lng, MainActivityRemake.myLocation.getLongitude(),0,0)/ 1000;
            Log.i("distance", googlePlace.Distance + " - for " +googlePlace.Name );
        } catch (JSONException e1) {
            e1.printStackTrace();
        }

        return googlePlace;
    }



}
