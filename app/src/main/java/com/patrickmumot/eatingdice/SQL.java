package com.patrickmumot.eatingdice;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.patrickmumot.eatingdice.Controller.DataController;
import com.patrickmumot.eatingdice.DataSet.CuisineData;
import com.patrickmumot.eatingdice.DataSet.IgnoreCriteriaData;
import com.patrickmumot.eatingdice.GooglePlaceModels.GooglePlace;
import com.patrickmumot.eatingdice.Models.Cuisine;
import com.patrickmumot.eatingdice.Models.IgnoreCriteria;
import com.patrickmumot.eatingdice.Table.CuisineTable;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by User on 2/5/2018.
 */

public class SQL extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "EatingDice2.db";

    public static final String CUISINE_TABLE_NAME = "cuisine_table";
    public static final String PLACE_HISTORY_TABLE_NAME = "place_history_table";
    public static final String PLACE_IGNORE_TABLE_NAME = "place_ignore_table";

    public static final String CUISINE_COL_2 = "Name";
    public static final String CUISINE_COL_3 = "LookFor";
    public static final String CUISINE_COL_4 = "LookedForCount";
    public static final String CUISINE_COL_5 = "CuisineImage";
    public static final String CUISINE_COL_6 = "CuisineType";
    public static final String CUISINE_COL_7 = "CuisineKeyword";
    public static final String CUISINE_COL_8 = "Delivery";

    public static final String PLACE_HISTORY_COL_2 = "PlaceId";
    public static final String PLACE_HISTORY_COL_3 = "LocationName";
    public static final String PLACE_HISTORY_COL_4 = "ShakesBeforeSelected";
    public static final String PLACE_HISTORY_COL_5 = "Delivery";
    public static final String PLACE_HISTORY_COL_6 = "Complete";

    public static final String PLACE_IGNORE_COL_2 = "PlaceName";
    public static final String PLACE_IGNORE_COL_4 = "Undo";

    SQLiteDatabase db;

    public SQL(Context context) {
        super(context, DATABASE_NAME, null, 1);
        db = this.getWritableDatabase();
    }

    public void initiatePlaceSelectedHistoryTable(){
        db = this.getWritableDatabase();
        db.execSQL("create table IF NOT EXISTS "+ PLACE_HISTORY_TABLE_NAME + "  (Id INTEGER PRIMARY KEY AUTOINCREMENT, PlaceId TEXT, LocationName TEXT, ShakesBeforeSelected INTEGER, Delivery BOOLEAN, Complete BOOLEAN)");
    }


    public void initiatePlaceIgnoreTable(){
        db = this.getWritableDatabase();
        db.execSQL("create table IF NOT EXISTS "+ PLACE_IGNORE_TABLE_NAME + " (Id INTEGER PRIMARY KEY AUTOINCREMENT, PlaceName TEXT, Undo BOOLEAN)");
    }


    public void initiateCuisineTable(){
        db = this.getWritableDatabase();
        db.execSQL("create table IF NOT EXISTS "+ CUISINE_TABLE_NAME + " (Id INTEGER PRIMARY KEY AUTOINCREMENT, Name TEXT, LookFor BOOLEAN, LookedForCount INTEGER, CuisineImage TEXT, CuisineType TEXT, CuisineKeyword TEXT, Delivery BOOLEAN)");
        //db.execSQL("DELETE FROM "+CUISINE_TABLE_NAME);
    }
    //****************************************************INITIATE ^^^
    public void populateInitialCuisines(){
        String[] cuisines = CuisineTable.cuisines;
        String[] cuisineImages = CuisineTable.cuisineImages;
        String[] cuisineTypes = CuisineTable.foodType;
        String[] cuisineKeywords= CuisineTable.foodKeyWord;
        boolean[] cuisineDelivery = CuisineTable.foodDelivery;
        int i = 0;
        for (String cuisine : cuisines ) {
            ContentValues contentValues = new ContentValues();
            contentValues.put(CUISINE_COL_2, cuisine);
            contentValues.put(CUISINE_COL_3, false);
            contentValues.put(CUISINE_COL_4, 0);
            contentValues.put(CUISINE_COL_5, cuisineImages[i]);
            contentValues.put(CUISINE_COL_6,cuisineTypes[i]);
            contentValues.put(CUISINE_COL_7, cuisineKeywords[i]);
            contentValues.put(CUISINE_COL_8, cuisineDelivery[i]);
            long insertId = db.insert(CUISINE_TABLE_NAME, null, contentValues);

            if(insertId == -1){
                Log.i("Main Cuisine", "Failed - " + cuisine);
            } else{
                Log.i("Main Cuisine", "SUCCESS - " + cuisine + " - insertId - "+insertId +" ----"+ cuisineImages[i]);

            }
            i++;
        }
    }

    public long savePlace(GooglePlace place, int shakeCount, boolean delivery){
        db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(PLACE_HISTORY_COL_2, place.PlaceId);
        contentValues.put(PLACE_HISTORY_COL_3, place.Name);
        contentValues.put(PLACE_HISTORY_COL_4, shakeCount);
        contentValues.put(PLACE_HISTORY_COL_5, delivery);
        contentValues.put(PLACE_HISTORY_COL_6, false);
        return db.insert(PLACE_HISTORY_TABLE_NAME, null, contentValues);
    }

    public long addIgnore(GooglePlace place){
        db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(PLACE_IGNORE_COL_2, place.Name);
        contentValues.put(PLACE_IGNORE_COL_4, false);

        return db.insert(PLACE_IGNORE_TABLE_NAME, null, contentValues);
    }

    public void removeIgnore(long id){

        String query = "DELETE FROM "+PLACE_IGNORE_TABLE_NAME +" WHERE Id = " + id;
        db.execSQL(query);
    }

    public void completePlace(GooglePlace place){
        db = this.getWritableDatabase();
        String query = "UPDATE "+PLACE_HISTORY_TABLE_NAME+ " SET " + PLACE_HISTORY_COL_6 + " = 1 WHERE Id = " + place.Id;
        db = getWritableDatabase();
        db.execSQL(query);
        Log.i("completed journey", "for = "+ place.Id +" - " +place.Name);
    }

    public void editCuisineLookFor(int id, boolean lookFor){
        int toggle = 0;
        if(lookFor){
            toggle = 1;
        }

        String query = "UPDATE "+CUISINE_TABLE_NAME+ " SET " + CUISINE_COL_3 + " = " + toggle + " WHERE Id = " + id;
        db = getWritableDatabase();
        Log.i("query", query + " - "+lookFor);
        db.execSQL(query);
    }

    public void editCuisineLookForCount(int id, int lookForCount){
        String query = "UPDATE "+CUISINE_TABLE_NAME+ " SET " + CUISINE_COL_4 + " = " + lookForCount + " WHERE Id = " + id;
        db = getWritableDatabase();
        db.execSQL(query);
    }

    public void updateCuisines(int id, int lookForCount){

        String query = "UPDATE "+CUISINE_TABLE_NAME+ " SET " + CUISINE_COL_4 + " = " + lookForCount + " WHERE Id = " + id;
        db = getWritableDatabase();
        Log.i("query", query + " - "+lookForCount);
        db.execSQL(query);
    }

    public List<Cuisine> getAllCuisines(){
        String query = "SELECT  * FROM " + CUISINE_TABLE_NAME;
        db = getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        return DataController.dataToCuisine(getCuisine(cursor));
    }

    public List<IgnoreCriteria> getAllIgnores(){
        String query = "SELECT  * FROM " + PLACE_IGNORE_TABLE_NAME;
        db = getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        return DataController.dataToIgnoreCriteria(getIgnoreCriteria(cursor));
    }

    private List<CuisineData> getCuisine(Cursor cursor){
        List<CuisineData> cuisines = new ArrayList<>();
        if (cursor.moveToFirst()) {
            do {
                cuisines.add(createCuisine(cursor));
            } while (cursor.moveToNext());
        }
        return cuisines;
    }

    private List<IgnoreCriteriaData> getIgnoreCriteria(Cursor cursor){
        List<IgnoreCriteriaData> ignoreCriteriaData = new ArrayList<>();
        if (cursor.moveToFirst()) {
            do {
                ignoreCriteriaData.add(createIgnoreCriteria(cursor));
            } while (cursor.moveToNext());
        }
        return ignoreCriteriaData;
    }


    private CuisineData createCuisine(Cursor cursor){
        int id = Integer.parseInt(cursor.getString(0));
        String name = cursor.getString(1);
        boolean lookFor = (cursor.getInt(2)>0);
        int lookForCount = (cursor.getInt(3));
        String cuisineImage = cursor.getString(4);
        String cuisineType = cursor.getString(5);
        String cuisineKeyword = cursor.getString(6);
        boolean cuisineDelivery = (cursor.getInt(7)>0);
        CuisineData cuisineData = new CuisineData();
        cuisineData.Id = id;
        cuisineData.Name = name;
        cuisineData.LookFor = lookFor;
        cuisineData.LookForCount = lookForCount;
        cuisineData.CuisineImage = cuisineImage;
        cuisineData.CuisineType = cuisineType;
        cuisineData.CuisineKeyword = cuisineKeyword;
        cuisineData.Delivery = cuisineDelivery;
        return cuisineData;
    }

    private IgnoreCriteriaData createIgnoreCriteria(Cursor cursor){
        int id = Integer.parseInt(cursor.getString(0));
        String name = cursor.getString(1);
        boolean undo = (cursor.getInt(2)>0);

        IgnoreCriteriaData ignoreCriteriaData = new IgnoreCriteriaData();
        ignoreCriteriaData.Id = id;
        ignoreCriteriaData.Name = name;
        ignoreCriteriaData.Undo = undo;
        return ignoreCriteriaData;
    }


    @Override
    public void onCreate(SQLiteDatabase db) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS "+CUISINE_TABLE_NAME);
        this.onCreate(db);
    }

    public void tempFix() {
        db.execSQL("DROP TABLE IF EXISTS "+CUISINE_TABLE_NAME);
    }


}
