package com.patrickmumot.eatingdice.Models;
/**
 * Created by User on 2/6/2018.
 */

public class Cuisine {
    private int Id;
    private String Name;
    private boolean LookFor = false;
    private int LookedCount;
    private String CuisineImage;
    private int CuisineResId;
    private double Distance;
    private String NextPageString = "";
    private String Type;
    private String KeyWord;
    public boolean end = false;
    public boolean Delivery;

    public Cuisine(){

    }

    public void setId(int id){
        Id = id;
    }
    public void setName(String name){
        Name = name;
    }
    public void setLookFor(boolean lookFor){
        LookFor = lookFor;
    }
    public void setLookedCount(int lookedCount){
        LookedCount = lookedCount;
    }
    public void setCuisineImage(String cuisineImage) { CuisineImage = cuisineImage; }
    public void setCuisineResId(int cuisineResId) { CuisineResId = cuisineResId; }
    public void setDistance(double distance) { Distance = distance; }
    public void setNextPage(String nextPage) { NextPageString = nextPage; }
    public void setType(String type) { Type = type; }
    public void setKeyWord(String keyWord) { KeyWord = keyWord; }
    public void setDelivery(boolean delivery) { Delivery = delivery; }

    public int getId(){
        return Id;
    }
    public String getName(){
        return Name;
    }
    public boolean getLookFor(){
        return LookFor;
    }
    public int getLookedCount() { return LookedCount; }
    public int getCuisineResId() { return CuisineResId; }
    public String getNextPage() { return NextPageString; }
    public String getType() { return Type; }
    public String getKeyWord() { return KeyWord; }
    public boolean getDelivery() { return Delivery; }

    public String getCuisineImage() {
        return CuisineImage+"_fixed";
    }
    public String getCuisineRegImage() {
        return CuisineImage+"_reg";
    }
}
