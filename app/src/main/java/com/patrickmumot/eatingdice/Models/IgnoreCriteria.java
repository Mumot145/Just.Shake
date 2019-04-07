package com.patrickmumot.eatingdice.Models;

import com.patrickmumot.eatingdice.GooglePlaceModels.GooglePlace;

import java.util.ArrayList;
import java.util.List;

public class IgnoreCriteria {
    private int Id;
    private String Name;
    private boolean Undo;
    private List<GooglePlace> placesIgnored = new ArrayList<>();

    public int GetId(){
        return Id;
    }

    public String GetName(){
        return Name;
    }

    public boolean GetUndo(){
        return Undo;
    }

    public List<GooglePlace> GetPlacesIgnored(){
        return placesIgnored;
    }

    public void AddPlaceIgnored(GooglePlace place){
        if(!placesIgnored.contains(place))
            placesIgnored.add(place);
    }

    public void EmptyPlaces(){
        placesIgnored.clear();
    }

    public void SetId(int id){
        Id = id;
    }

    public void SetName(String name){
        Name = name;
    }

    public void SetUndo(boolean undo){
        Undo = undo;
    }
}
