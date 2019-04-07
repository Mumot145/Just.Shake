package com.patrickmumot.eatingdice;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.patrickmumot.eatingdice.API.GooglePlaces;
import com.patrickmumot.eatingdice.Controller.DataController;
import com.patrickmumot.eatingdice.Enum.Direction;
import com.patrickmumot.eatingdice.Fragment.DiceFragment;
import com.patrickmumot.eatingdice.Fragment.newDiceFragment;
import com.patrickmumot.eatingdice.GooglePlaceModels.GoogleDetails;
import com.patrickmumot.eatingdice.GooglePlaceModels.GooglePlace;
import com.patrickmumot.eatingdice.Interface.IGooglePlacesResponse;
import com.patrickmumot.eatingdice.Models.Cuisine;
import com.patrickmumot.eatingdice.dummy.DummyContent;
import com.warkiz.widget.IndicatorSeekBar;
import com.warkiz.widget.OnSeekChangeListener;
import com.warkiz.widget.SeekParams;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class CategoriesActivity extends AppCompatActivity
        implements ZomatoCuisineItemFragment.OnListFragmentInteractionListener,
        newDiceFragment.OnDiceInteractionListener,
        IGooglePlacesResponse {
    static public int searchDistance = 10;
    static public int searchLocations = 0;

    static public List<Integer> changedCuisineIds = new ArrayList<>();
    static public Cuisine tappedCuisine = new Cuisine();
    private ZomatoCuisineItemFragment cuisineFragment = new ZomatoCuisineItemFragment();
    private Cuisine initialHighlightedCuisine;
    private SQL sql;

    private com.warkiz.widget.IndicatorSeekBar distanceSeekBar;
    private RelativeLayout categoriesRelative;
    private RelativeLayout loadingLayout;
    private GooglePlaces googlePlaces;
    private Switch deliverySwitch;
    private List<String> selectedLocations = new ArrayList<>();
    private TextView distanceTextView;
    private TextView deliveryTextView;
    private TextView ratingTextView;

    private newDiceFragment categoriesDice;

    private boolean initialSearch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_categories);
        searchLocations = 0;
        distanceSeekBar = (com.warkiz.widget.IndicatorSeekBar) findViewById(R.id.distanceSeekBar);
        categoriesRelative = (RelativeLayout) findViewById(R.id.categoriesRelative);
        loadingLayout = (RelativeLayout) findViewById(R.id.loadingLayout);
        deliverySwitch = (Switch) findViewById(R.id.deliverySwitch);
        distanceTextView = (TextView) findViewById(R.id.distanceTextView);
        deliveryTextView = (TextView) findViewById(R.id.deliveryTextView);
        ratingTextView = (TextView) findViewById(R.id.ratingTextView);
        cuisineFragment = new ZomatoCuisineItemFragment();
        googlePlaces = new GooglePlaces(this);
        categoriesDice = (newDiceFragment) getSupportFragmentManager().findFragmentById(R.id.categoriesDice);

        distanceSeekBar.setProgress((int)MainActivityRemake.distance);
        searchDistance = (int)MainActivityRemake.distance;
        sql = new SQL(this);
        Typeface custom_font = Typeface.createFromAsset(getAssets(),  "fonts/SourceSansPro-Regular.ttf");
        ratingTextView.setTypeface(custom_font);
        deliveryTextView.setTypeface(custom_font);
        distanceTextView.setTypeface(custom_font);
        DummyContent.ITEMS.clear();
        DummyContent.ITEMSDELIVERY.clear();
        initialHighlightedCuisine = null;
        initialSearch = true;
        deliverySwitch.setChecked(MainActivityRemake.delivery);
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.N){
            Cuisine cuisine = MainActivityRemake.initialCuisineList.get(0);
            MainActivityRemake.initialCuisineList.remove(cuisine);
            MainActivityRemake.initialCuisineList.sort(new Comparator<Cuisine>() {
                @Override
                public int compare(Cuisine o1, Cuisine o2) {
                    return o2.getLookedCount() - o1.getLookedCount();
                }
            });
            MainActivityRemake.initialCuisineList.add(0, cuisine);
        }


        for (Cuisine cuisine:MainActivityRemake.initialCuisineList) {
            if(cuisine.getLookFor() && initialHighlightedCuisine == null){
                initialHighlightedCuisine = cuisine;
            } else if(cuisine.getLookFor()){
                cuisine.setLookFor(false);
            }

            String image = cuisine.getCuisineImage();
            cuisine.setCuisineResId(getResId(image, R.drawable.class));
            DummyContent.ITEMS.add(cuisine);
            if(cuisine.getDelivery()){
                DummyContent.ITEMSDELIVERY.add(cuisine);
            }
        }
        if(initialHighlightedCuisine == null){
            Cuisine cuisine = MainActivityRemake.initialCuisineList.get(0);
            cuisine.setLookFor(true);
            initialHighlightedCuisine = cuisine;
        }

        categoriesLoading(false);
        deliverySwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                MainActivityRemake.delivery = isChecked;
                cuisineFragment.setDelivery(isChecked);
                if(isChecked){
                    distanceSeekBar.setEnabled(false);
                } else {
                    distanceSeekBar.setEnabled(true);
                }
            }
        });
        if(MainActivityRemake.delivery){
            distanceSeekBar.setEnabled(false);
        } else {
            distanceSeekBar.setEnabled(true);
        }
        distanceSeekBar.setIndicatorTextFormat("${PROGRESS} km");
        distanceSeekBar.setOnSeekChangeListener(new OnSeekChangeListener() {
            @Override
            public void onSeeking(SeekParams seekParams) {
                searchDistance = seekParams.progress;
            }

            @Override
            public void onStartTrackingTouch(IndicatorSeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(IndicatorSeekBar seekBar) {

            }
        });

        android.support.v4.app.FragmentManager fragmentManager = getSupportFragmentManager();
        android.support.v4.app.FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.content, cuisineFragment).commit();
    }

    public static int getResId(String resName, Class<?> c) {
        try {
            Field idField = c.getDeclaredField(resName);
            return idField.getInt(idField);
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
    }

    private void categoriesLoading(boolean loading){
        if(loading){
            categoriesDice.startShake();
            loadingLayout.setVisibility(View.VISIBLE);
            categoriesRelative.setVisibility(View.INVISIBLE);
        } else {
            categoriesDice.endShake();
            loadingLayout.setVisibility(View.INVISIBLE);
            categoriesRelative.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onBackPressed() {
        MainActivityRemake.somethingSelected = false;
        if(MainActivityRemake.doubleBackToExitPressedOnce){
            ActivityCompat.finishAffinity(this);
        } else {
            MainActivityRemake.doubleBackToExitPressedOnce = false;
            new Handler().postDelayed(new Runnable() {

                @Override
                public void run() {
                    MainActivityRemake.doubleBackToExitPressedOnce=false;
                }
            }, 3000);

            super.onBackPressed();
        }
    }


    @Override
    public void onListFragmentInteraction(Cuisine item) {
        categoriesLoading(true);
        if(MainActivityRemake.delivery){
            tappedCuisine = DummyContent.ITEMSDELIVERY.get(DummyContent.ITEMSDELIVERY.indexOf(item));
        } else {
            tappedCuisine = DummyContent.ITEMS.get(DummyContent.ITEMS.indexOf(item));
        }

        tappedCuisine.setNextPage("");
        tappedCuisine.end = false;
        MainActivityRemake.searchedCuisine = tappedCuisine;
        selectedLocations.clear();
        String Type = "";
        if(MainActivityRemake.delivery){
            Type = "meal_delivery";
        } else {
            Type = tappedCuisine.getType();
        }
        MainActivityRemake.downloadLocation = MainActivityRemake.myLocation;
        googlePlaces.SearchNearMe(MainActivityRemake.myLocation, Type, tappedCuisine.getKeyWord(), tappedCuisine.getNextPage(), Direction.NEARBY);
    }

    @Override
    public void getGooglePlacesMain(List<GooglePlace> googlePlaceList) {
    }

    @Override
    public void getGooglePlaces(List<GooglePlace> googlePlaceList) {
        MainActivityRemake.distance = searchDistance;
        String placeNumberString = "";
        if(initialSearch){
            if(googlePlaceList.size() > 0){
                initialSearch = false;
                MainActivityRemake.googlePlaces.clear();
            } else {
                CenterToast("There are no " + MainActivityRemake.searchedCuisine.getName() + " open at this time, choose something else!");
                categoriesLoading(false);
                return;
            }
        }

        for (GooglePlace place : googlePlaceList) {
            String shortName;
            String[] nameSplit = place.Name.split(" ");
            if (nameSplit.length > 1) {
                shortName = nameSplit[0] + " " + nameSplit[1];
            } else if (nameSplit.length == 1) {
                shortName = nameSplit[0];
            } else {
                shortName = place.Name;
            }

            if (!selectedLocations.contains(shortName) && (place.Distance <= searchDistance || (MainActivityRemake.delivery && place.Distance <= 20))) {
                selectedLocations.add(shortName);
                MainActivityRemake.googlePlaces.add(place);
                searchLocations++;
            }
        }
        if (searchLocations > 20) {
            placeNumberString = "20+";
        } else {
            placeNumberString = String.valueOf(searchLocations);
        }

        if (MainActivityRemake.googlePlaces.size() > 0) {

            if (tappedCuisine.end) {
                tappedCuisine.setLookFor(true);
                tappedCuisine.setLookedCount(tappedCuisine.getLookedCount() + 1);
                MainActivityRemake.searchedCuisine = tappedCuisine;
                sql.editCuisineLookFor(tappedCuisine.getId(), true);
                sql.editCuisineLookForCount(tappedCuisine.getId(), tappedCuisine.getLookedCount());
                initialSearch = true;
                if(initialHighlightedCuisine != null && initialHighlightedCuisine != tappedCuisine){
                    DummyContent.ITEMS.get(DummyContent.ITEMS.indexOf(initialHighlightedCuisine)).setLookFor(false);
                    sql.editCuisineLookFor(initialHighlightedCuisine.getId(), false);
                }

                if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.N){
                    MainActivityRemake.googlePlaces.sort(new Comparator<GooglePlace>() {
                        @Override
                        public int compare(GooglePlace o1, GooglePlace o2) {
                            if (o1.Distance < o2.Distance) return -1;
                            if (o1.Distance > o2.Distance) return 1;
                            return 0;
                        }
                    });
                }
                setResult(RESULT_OK);
                finish();
                MainActivityRemake.ready = true;

                MainActivityRemake.currentPlaces = null;
                MainActivityRemake.locationsFound = placeNumberString;
                DataController.EmptyUsedNumbers();
            } else {
                String Type = "";
                if (MainActivityRemake.delivery) {
                    Type = "meal_delivery";
                } else {
                    Type = tappedCuisine.getType();
                }

                googlePlaces.SearchNearMe(MainActivityRemake.myLocation, Type, tappedCuisine.getKeyWord(), tappedCuisine.getNextPage(), Direction.NEARBY);
            }
        } else {
            initialSearch = true;
            CenterToast("No " + MainActivityRemake.searchedCuisine.getName() + " within "+searchDistance+"! Try increasing distance.");
            categoriesLoading(false);
        }

    }

    private void CenterToast(String message){
        Toast toast = Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();
    }

    @Override
    public void getGoogleDetails(GoogleDetails googleDetails) {

    }

    @Override
    public void onDiceInteraction() {

    }
}
