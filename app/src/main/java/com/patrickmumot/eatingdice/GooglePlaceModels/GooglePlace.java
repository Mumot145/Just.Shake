package com.patrickmumot.eatingdice.GooglePlaceModels;

import com.patrickmumot.eatingdice.Models.Location;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by User on 3/22/2018.
 */

public class GooglePlace {
    public boolean Saved = false;
    public int Id;
    public String Name;
    public GoogleDetails Details;
    public String PlaceId;
    public double Distance;
    public int PriceLevel = -1;
    public boolean complete = false;
    public long IgnoreId;

    public GooglePlace(){
        Details = new GoogleDetails();
    }
}
