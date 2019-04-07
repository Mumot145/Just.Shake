package com.patrickmumot.eatingdice.Controller;

import android.util.Log;

import com.patrickmumot.eatingdice.DataSet.CuisineData;
import com.patrickmumot.eatingdice.DataSet.IgnoreCriteriaData;
import com.patrickmumot.eatingdice.GooglePlaceModels.GooglePlace;
import com.patrickmumot.eatingdice.Models.Cuisine;
import com.patrickmumot.eatingdice.Models.IgnoreCriteria;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by User on 2/16/2018.
 */

public class DataController {
    static int startIndex = 0;
    static int endIndex = 4;
    static List<Integer> usedNumbers = new ArrayList<>();
    static Random r = new Random();

    static public List<Cuisine> dataToCuisine(List<CuisineData> cuisineData){
        List<Cuisine> result = new ArrayList<>();
        for (CuisineData cData:cuisineData) {
            Cuisine cuisine = new Cuisine();
            cuisine.setName(cData.Name);
            cuisine.setId(cData.Id);
            cuisine.setLookFor(cData.LookFor);
            cuisine.setLookedCount(cData.LookForCount);
            cuisine.setCuisineImage(cData.CuisineImage);
            cuisine.setType(cData.CuisineType);
            cuisine.setKeyWord(cData.CuisineKeyword);
            cuisine.setDelivery(cData.Delivery);
            result.add(cuisine);
        }

        return result;
    }

    static public List<IgnoreCriteria> dataToIgnoreCriteria(List<IgnoreCriteriaData> ignoreCriteriaData){
        List<IgnoreCriteria> result = new ArrayList<>();
        for (IgnoreCriteriaData cData:ignoreCriteriaData) {
            IgnoreCriteria ignoreCriteria = new IgnoreCriteria();
            ignoreCriteria.SetId(cData.Id);
            ignoreCriteria.SetName(cData.Name);
            ignoreCriteria.SetUndo(cData.Undo);
            result.add(ignoreCriteria);
        }

        return result;
    }

    public static double round (double value, int precision) {
        int scale = (int) Math.pow(10, precision);
        return (double) Math.round(value * scale) / scale;
    }

    public static double distance(double lat1, double lat2, double lon1,
                                  double lon2, double el1, double el2) {

        final int R = 6371; // Radius of the earth

        double latDistance = Math.toRadians(lat2 - lat1);
        double lonDistance = Math.toRadians(lon2 - lon1);
        double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2)
                + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2))
                * Math.sin(lonDistance / 2) * Math.sin(lonDistance / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        double distance = R * c * 1000; // convert to meters

        double height = el1 - el2;

        distance = Math.pow(distance, 2) + Math.pow(height, 2);

        return Math.sqrt(distance);
    }

    public static GooglePlace GetPlaceClosest(List<GooglePlace> places, double iteration){
        if(iteration == 0){
            startIndex = 0;
            endIndex = 5;
        }
        double whole = iteration / 5;
        if(whole % 1 == 0 && iteration > 0){
            startIndex = startIndex + 5;
            endIndex = endIndex + 5;
        }

        if(endIndex > places.size()){
            endIndex = places.size();
        }
        Log.i("indexes", "whole = " + whole + "result - "+ (whole % 1 == 0) + " iteration =>" + iteration);
        Integer Result = null;
        while(Result == null){
            Result = r.nextInt(endIndex - startIndex) + startIndex;
            Log.i("Result", Result+ "");
            if(usedNumbers.contains(Result)){
                Result = null;
            } else {
                usedNumbers.add(Result);
            }
        }

        return places.get(Result);
    }

    public static GooglePlace GetPlaceRandom(List<GooglePlace> places){
        Integer Result = null;
        while(Result == null){
            Result = r.nextInt(places.size());
           // Log.i("Result", Result+ "");
            if(usedNumbers.contains(Result)){
                Result = null;
            } else {
                usedNumbers.add(Result);
            }
        }

        return places.get(Result);
    }

    public static void EmptyUsedNumbers(){
        usedNumbers.clear();
    }

}
