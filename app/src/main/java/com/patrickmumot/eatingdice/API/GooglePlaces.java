package com.patrickmumot.eatingdice.API;

import android.content.Context;
import android.location.Location;
import android.net.Uri;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.patrickmumot.eatingdice.CategoriesActivity;
import com.patrickmumot.eatingdice.Enum.Direction;
import com.patrickmumot.eatingdice.GooglePlaceModels.GoogleDetails;
import com.patrickmumot.eatingdice.GooglePlaceModels.GooglePhoto;
import com.patrickmumot.eatingdice.GooglePlaceModels.GooglePlace;
import com.patrickmumot.eatingdice.Interface.IGooglePlacesResponse;
import com.patrickmumot.eatingdice.MainActivityRemake;
import com.patrickmumot.eatingdice.Parser.ParseJson;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by User on 3/22/2018.
 */

public class GooglePlaces {
    private String nearbySearch = "https://maps.googleapis.com/maps/api/place/nearbysearch/json?";
    static private String photoSearch =  "https://maps.googleapis.com/maps/api/place/photo?";
    private String detailSearch =  "https://maps.googleapis.com/maps/api/place/details/json?";
    private String distanceSearch = "https://maps.googleapis.com/maps/api/distancematrix/json?units=metric&";
    static private String apiKey = "AIzaSyAwfV2fa8O3-6HAmsvbcyfUZJjDEGosYgE";
    private Context thisContext;
    private RequestQueue queue;
    private IGooglePlacesResponse googlePlacesResponse;
    private Timer timer;
    private boolean allow = true;
    final Handler handler = new Handler();
    private String currentUrl = "";
    private Direction currentDirection = null;

    final Runnable runnable = new Runnable() {
        @Override
        public void run() {
            // Do something after 5s = 5000ms
            JSONGet(currentUrl, currentDirection);
        }
    };
    public GooglePlaces(Context context){
        thisContext = context;
        timer = new Timer();
        queue = Volley.newRequestQueue(thisContext);


        if (context instanceof IGooglePlacesResponse) {
            googlePlacesResponse = (IGooglePlacesResponse) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement IGooglePlaceResponse");
        }
    }

    public void SearchNearMe(Location myLocation, String type, String keyWord, String pageToken, Direction direction){
        String keyword = keyWord.replaceAll("'","")
                                .replaceAll("Restaurant","")
                                .replaceAll(" ","")
                                .replaceAll("Food","");
        if(keyword == "cafe"){
            keyword = "";
        }

        String url = nearbySearch + createUrlComponent("location",myLocation.getLatitude()+","+myLocation.getLongitude()) + "&" +
                "opennow&" +
                createUrlComponent("type", type) + "&" +
                createUrlComponent("rankby", "distance") + "&" +
                createUrlComponent("keyword", Uri.encode(keyword)) + "&" +
                createUrlComponent("pagetoken", pageToken) + "&" +
                createUrlComponent("key", apiKey);

        JSONGet(url, direction);
    }

    public void SearchDetails(String placeId){
        String url = detailSearch + createUrlComponent("place_id",placeId) + "&" +
                createUrlComponent("key", apiKey);
        JSONGet(url, Direction.DETAILS);
    }

    static public String CreatePhotoUrl(GooglePhoto photo){
        String url = photoSearch + createUrlComponent("maxwidth",String.valueOf(photo.Width)) + "&" +
                createUrlComponent("photoreference", photo.PhotoReference) + "&" +
                createUrlComponent("key", apiKey);
        return url;
    }

    static private String createUrlComponent(String urlElement, String addToUrl){
        return urlElement + "=" + addToUrl;
    }

    private void JSONGet(final String url, final Direction direction){
        currentDirection = direction;
        currentUrl = url;
        Log.i("url", currentUrl);
        if(allow == false){
            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    allow = true;
                    JSONGet(url,direction);
                }
            }, 2000);
        }

        if(allow == true){
            JsonObjectRequest postRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            try {
                                if(response.getString("status").equals("INVALID_REQUEST")){
                                    Toast.makeText(thisContext, "INVALID REQUEST", Toast.LENGTH_SHORT).show();
                                } else if(response.getString("status").equals("ZERO_RESULTS")) {
                                    if(direction == Direction.NEARBY){
                                        googlePlacesResponse.getGooglePlaces(new ArrayList<GooglePlace>());
                                    } else if(direction == Direction.NEARBYMAIN){
                                        //MainActivityRemake.startedSearch = true;
                                        googlePlacesResponse.getGooglePlacesMain(new ArrayList<GooglePlace>());
                                    }

                                } else {
                                    if(direction == Direction.NEARBY || direction == Direction.NEARBYMAIN){
                                        allow = false;
                                    }
                                    String nextPageToken = "";

                                    if(response.has("next_page_token")){
                                        nextPageToken = response.getString("next_page_token");
                                    }
                                    if(nextPageToken != ""){
                                        MainActivityRemake.searchedCuisine.setNextPage(nextPageToken);
                                        CategoriesActivity.tappedCuisine.setNextPage(nextPageToken);
                                    } else {
                                        MainActivityRemake.searchedCuisine.end = true;
                                        CategoriesActivity.tappedCuisine.end = true;
                                    }

                                    returnGooglePlaces(response, direction);
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            };

                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            // TODO Auto-generated method stub
                            handler.postDelayed(runnable, 2000);

                            Log.i("ERROR", "error => " + error.toString());
                        }
                    }
            ) {
                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    Map<String, String> params = new HashMap<String, String>();
                    params.put("Accept", "application/json");
                    return params;
                }
            };
            queue.add(postRequest);
        }
    }

    private void returnGooglePlaces(JSONObject jsonObject, Direction direction){
        if(direction == Direction.NEARBY){
            List<GooglePlace> places = ParseJson.JsonToSimpleGooglePlaces(jsonObject);
            googlePlacesResponse.getGooglePlaces(places);
        } else if(direction == Direction.DETAILS){
            GoogleDetails details = ParseJson.JsonToGooglePlaceDetails(jsonObject);
            googlePlacesResponse.getGoogleDetails(details);
        } else if(direction == Direction.NEARBYMAIN){
            List<GooglePlace> places = ParseJson.JsonToSimpleGooglePlaces(jsonObject);
            googlePlacesResponse.getGooglePlacesMain(places);
        }
    }
}
