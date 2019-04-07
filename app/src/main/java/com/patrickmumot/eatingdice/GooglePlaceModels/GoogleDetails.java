package com.patrickmumot.eatingdice.GooglePlaceModels;

import android.util.Log;

import com.patrickmumot.eatingdice.Models.AddressComponents;
import com.patrickmumot.eatingdice.Models.Location;
import com.patrickmumot.eatingdice.Models.OpenHours;
import com.patrickmumot.eatingdice.Models.Review;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by User on 3/25/2018.
 */

public class GoogleDetails {
    public String InternationalPhoneNumber;
    public String FormattedPhoneNumber;
    public List<AddressComponents> AddressComponents;
    public String FormattedAddress;
    public OpenHours OpenHours;
    public List<String> WeekdayText;
    public List<GooglePhoto> Photos;
    public double Rating;
    public List<Review> Reviews;
    public int UtcOffset;
    public String WebsiteUrl;

    public Location Geometry;
    public String IconUrl;
    public String Id;
    public String Vicinity;
    public int PriceLevel;
    public boolean OpenNow;
    public List<GooglePhoto> GooglePhotos;

    public String Reference;
    public List<String> Types;
    public String[] Weekdays = new String [] {"Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday", "Sunday"};

    public GoogleDetails(){
        AddressComponents = new ArrayList<>();
        WeekdayText = new ArrayList<>();
        Reviews = new ArrayList<>();
        OpenHours = new OpenHours();
        Photos = new ArrayList<>();
        GooglePhotos = new ArrayList<>();
        Types = new ArrayList<>();
        Geometry = new Location();
    }

    public String GetCloseHours(int weekday){
        Log.i("weekday", weekday+"");
        if(weekday == 0){
            weekday = 6;
        } else {
            weekday--;
        }

        String weekdayString = WeekdayText.get(weekday);
        return weekdayString.replace(Weekdays[weekday]+": ","");
    }

    public String GetDisplayAddress(){
        String address = null;
        if(AddressComponents.size() > 1){
            address = AddressComponents.get(0).ShortName + " " + AddressComponents.get(1).ShortName ;
        }

        return address;
    }
}
