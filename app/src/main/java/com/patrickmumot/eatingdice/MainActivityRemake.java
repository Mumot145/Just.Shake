package com.patrickmumot.eatingdice;

import android.Manifest;
import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.graphics.Typeface;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.preference.PreferenceManager;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatCheckBox;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.GestureDetector;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.daasuu.bl.BubbleLayout;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.patrickmumot.eatingdice.API.GooglePlaces;
import com.patrickmumot.eatingdice.Adapter.FullViewPagerAdapter;
import com.patrickmumot.eatingdice.Adapter.ViewPagerAdapter;
import com.patrickmumot.eatingdice.Animation.BounceInterpolator;
import com.patrickmumot.eatingdice.Controller.DataController;


import com.patrickmumot.eatingdice.Elements.SwipeRelativeLayout;
import com.patrickmumot.eatingdice.Enum.Direction;
import com.patrickmumot.eatingdice.Enum.detailType;
import com.patrickmumot.eatingdice.Event.ShakeDetector;
import com.patrickmumot.eatingdice.Fragment.newDiceFragment;
import com.patrickmumot.eatingdice.Fragment.ReviewFragment;
import com.patrickmumot.eatingdice.GooglePlaceModels.GoogleDetails;
import com.patrickmumot.eatingdice.GooglePlaceModels.GooglePhoto;
import com.patrickmumot.eatingdice.GooglePlaceModels.GooglePlace;
import com.patrickmumot.eatingdice.Interface.IGooglePlacesResponse;
import com.patrickmumot.eatingdice.Interface.IPhotoInteraction;
import com.patrickmumot.eatingdice.Interface.IScrollListener;
import com.patrickmumot.eatingdice.Listener.ScrollGestureListener;
import com.patrickmumot.eatingdice.Models.Cuisine;
import com.patrickmumot.eatingdice.Models.IgnoreCriteria;
import com.patrickmumot.eatingdice.Models.Review;
import com.patrickmumot.eatingdice.Service.LocationService;
import com.patrickmumot.eatingdice.Table.CuisineTable;
import com.patrickmumot.eatingdice.dummy.DummyContent;
import com.squareup.picasso.Picasso;

import java.lang.reflect.Field;
import java.security.Provider;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.AdRequest;

import org.w3c.dom.Text;

/**
 * Created by User on 4/16/2018.
 */

public class MainActivityRemake extends AppCompatActivity implements IGooglePlacesResponse,
        ReviewFragment.OnFragmentInteractionListener,
        newDiceFragment.OnDiceInteractionListener,
        OnMapReadyCallback, IScrollListener {
    private AppCompatActivity activityRef;
    private InterstitialAd mInterstitialAd;

    String mapStyleJson = "[\n" +
            "  {\n" +
            "    \"featureType\": \"administrative\",\n" +
            "    \"elementType\": \"geometry\",\n" +
            "    \"stylers\": [\n" +
            "      {\n" +
            "        \"visibility\": \"off\"\n" +
            "      }\n" +
            "    ]\n" +
            "  },\n" +
            "  {\n" +
            "    \"featureType\": \"poi\",\n" +
            "    \"stylers\": [\n" +
            "      {\n" +
            "        \"visibility\": \"off\"\n" +
            "      }\n" +
            "    ]\n" +
            "  },\n" +
            "  {\n" +
            "    \"featureType\": \"road\",\n" +
            "    \"elementType\": \"labels.icon\",\n" +
            "    \"stylers\": [\n" +
            "      {\n" +
            "        \"visibility\": \"off\"\n" +
            "      }\n" +
            "    ]\n" +
            "  },\n" +
            "  {\n" +
            "    \"featureType\": \"transit\",\n" +
            "    \"stylers\": [\n" +
            "      {\n" +
            "        \"visibility\": \"off\"\n" +
            "      }\n" +
            "    ]\n" +
            "  }\n" +
            "]";

    public static Location myLocation;
    public static Location downloadLocation;
    public static boolean expanding;
    private String provider;

    public static int screenWidth;
    public static int screenHeight;
    public static int restaurantImageHeight;
    public static int currentPlaceIndex = 0;
    private int currentPhotoIndex = 0;

    public static double distance;
    public static boolean delivery;

    public static String locationsFound = "0";
    public static boolean somethingSelected = false;


    private static final int INITIAL_LOGIN = 0;
    private static final int NORMAL_LOGIN = 1;

    private int DISPLAY_INITIAL = 0;
    private int DISPLAY_LOADING = 1;
    private int DISPLAY_EMPTY = 2;
    private int DISPLAY_FOUND = 3;

    private int TOP = 0;
    private int LEFT = 1;
    private int RIGHT = 2;


    private int shakeCount = 0;
    private double locationsSelected = 0;
    private int STATE = 0;
    private int searchLocations = 0;
    private int backCount = 2;
    private int totalShakeCount;


    private boolean shook = false;
    private boolean firstShake = true;
    public static boolean ready = false;
    private boolean infoDisplayed = false;
    private boolean diceInfoDisplayed = false;
    private boolean categoriesSelected = false;
    private boolean placeSaved = false;
    private boolean firstCheck = true;
    private boolean expanded = false;
    private boolean imageDisplay = false;
    private boolean firstMapPan = false;
    private boolean inMain = true;
    public boolean paused =  false;

    private static final int LOCATION_PERMISSION_REQUEST_CODE = 10;
    private static final int PHONE_PERMISSION_REQUEST_CODE = 5;

    private LocationManager locationManager;
    private LocationListener locationListener;
    private GooglePlaces googlePlacesApi;
    private SQL sql;
    private SharedPreferences prefs;
    private ViewPager mViewPager;
    private ViewPager fullPageViewPager;
    private ViewPagerAdapter mViewPagerAdapter;
    private FullViewPagerAdapter mFullViewPagerAdapter;

    private DrawerLayout mDrawerLayout;
    private RelativeLayout initialDisplay;
    private RelativeLayout loadingDisplay;
    private RelativeLayout mainDisplay;
    private SwipeRelativeLayout reviewLayout;
    private SwipeRelativeLayout mapLayout;
    private ConstraintLayout diceDisplay;
    private ConstraintLayout restaurantDisplay;
    private ConstraintLayout detailsDisplay;
    private ConstraintLayout placeDisplay;
    private RelativeLayout fullImageDisplay;
    private ScrollView reviewDisplay;
    private ConstraintLayout frameDisplay;
    private LinearLayout detailsButtonsDisplay;
    private LinearLayout distanceLayout;
    private LinearLayout closingLayout;
    private LinearLayout priceLevelLayout;
    private BubbleLayout saveBubble;
    private BubbleLayout categoriesBubble;
    private BubbleLayout diceBubble;
    private ProgressBar emptyProgress;

    public static ConstraintLayout emptyDisplay;

    private TextView emptyReviews;
    private TextView nameTextView;
    private TextView distanceTextView;
    private TextView reviewTextView;
    private TextView mapTextView;
    private TextView statusTextView;
    private TextView categoriesTextView;
    private TextView closingTextView;
    private TextView initialTextView;
    private TextView priceLevelTextView;
    private TextView titleTextView;
    private TextView saveTextView;
    private TextView diceTextView;

    private ImageView mapActive;
    private ImageView reviewsActive;
    private ImageView fullImage;
    private com.patrickmumot.eatingdice.Elements.SwipeImageView cusineImage;
    public static ImageView leftArrow;
    public static ImageView rightArrow;
    public static ImageView leftArrowFull;
    public static ImageView rightArrowFull;
    private ImageView reviewDownArrow;
    private ImageView mapDownArrow;
    private ImageView deliveryRestaurant;

    private Button websiteButton;
    private Button phoneButton;
    private Button backButton;
    private Button nextButton;
    private Button directionsButton;

    private Toolbar myToolbar;
    private RatingBar ratingBar;
    private MapFragment mMapFragment;
    private ToggleButton saveButton;
    private ToggleButton ignoreButton;
    public static newDiceFragment diceFragment;

    private SensorManager mSensorManager;
    private Sensor mAccelerometer;
    private ShakeDetector mShakeDetector;
    private Bitmap restaurantBitmap;
    private MediaPlayer myMediaPlayer = new MediaPlayer();
    private Runnable saveRunnable;
    private Runnable diceRunnable;
    private Runnable categoriesRunnable;
    private Handler handler = new Handler();
    private ScrollGestureListener reviewListener;
    private ScrollGestureListener mapListener;
    private IScrollListener scrollListener;
    private GestureDetector reviewGestureDetector;
    private GestureDetector mapGestureDetector;


    public static List<Cuisine> initialCuisineList = new ArrayList<>();
    public static List<GooglePlace> googlePlaces = new ArrayList<>();
    public static List<GooglePlace> currentPlaces = null;
    private List<GooglePlace> seenLocations = new ArrayList<>();
    private List<String> selectedLocationsNames = new ArrayList<>();
    private List<android.app.Fragment> selectedReviewFragments = new ArrayList<>();
    public static List<IgnoreCriteria> ignoreCriteria = new ArrayList<>();


    public static Cuisine searchedCuisine = null;
    public GooglePlace searchedPlace = null;
    private detailType currentDetailType = null;
    public static detailType lastDetailType = null;
    private Date currentTime;


    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    @Override
    public void onDiceInteraction() {

        if(firstShake){
            handler.removeCallbacks(diceRunnable);
            firstShake = false;
        }

        shake();
    }

    @Override
    public void onScrollHorizontal(float dx) {
    }

    @Override
    public void onScrollVertical(float dy, detailType type) {

        //scroll down
        if(dy < 0){
            if(expanded){
                openDetails(currentDetailType);
            }
        }
        //scroll up
        else if(dy > 0){
            if(!expanded){
                openDetails(type);
            }
        }
    }

    @Override
    public void onTapped(detailType detailType, GooglePhoto photo, int index) {
        currentPhotoIndex =  index;
        if(detailType != null){
            if(photo != null){
                displayFullImage(true, photo);
            } else {
                openDetails(detailType);
            }
        } else {
            displayFullImage(false, null);
        }
    }


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_remake);
        initialDisplay = (RelativeLayout) findViewById(R.id.InitialDisplay);
        mainDisplay = (RelativeLayout) findViewById(R.id.MainDisplay);
        detailsDisplay = (ConstraintLayout) findViewById(R.id.detailsLayout);
        placeDisplay = (ConstraintLayout) findViewById(R.id.restaurantTitleLayout);
        diceDisplay = (ConstraintLayout) findViewById(R.id.diceLayout);
        emptyDisplay = (ConstraintLayout) findViewById(R.id.emptyContraint);
        restaurantDisplay = (ConstraintLayout) findViewById(R.id.restaurantConstraint);
        fullImageDisplay = (RelativeLayout) findViewById(R.id.ImageDisplay);
        reviewDisplay = (ScrollView) findViewById(R.id.displayReviews);
        frameDisplay = (ConstraintLayout) findViewById(R.id.mapFrame);
        reviewLayout = (SwipeRelativeLayout) findViewById(R.id.reviewLayout);
        mapLayout = (SwipeRelativeLayout) findViewById(R.id.mapLayout);
        detailsButtonsDisplay = (LinearLayout) findViewById(R.id.showDetailsLayout);
        distanceLayout = (LinearLayout) findViewById(R.id.distanceLayout);
        closingLayout = (LinearLayout) findViewById(R.id.closingLayout);
        priceLevelLayout = (LinearLayout) findViewById(R.id.priceLevelLayout);
        mViewPager = (ViewPager) findViewById(R.id.imageViewPager);
        fullPageViewPager = (ViewPager) findViewById(R.id.fullPageViewPager);
        websiteButton = (Button) findViewById(R.id.webButton);
        phoneButton = (Button) findViewById(R.id.phoneButton);
        backButton = (Button) findViewById(R.id.backButton);
        nextButton = (Button) findViewById(R.id.nextButton);
        directionsButton = (Button) findViewById(R.id.directionsButton);
        deliveryRestaurant = (ImageView) findViewById(R.id.deliveryRestaurant);
        mViewPagerAdapter = new ViewPagerAdapter(this);
        mFullViewPagerAdapter = new FullViewPagerAdapter(this);
        mViewPager.setAdapter(mViewPagerAdapter);
        fullPageViewPager.setAdapter(mFullViewPagerAdapter);
        nameTextView = (TextView) findViewById(R.id.locationName);
        distanceTextView = (TextView) findViewById(R.id.distance);
        ratingBar = (RatingBar) findViewById(R.id.mainRatingBar);
        reviewTextView = (TextView) findViewById(R.id.reviewsTextView);
        reviewsActive = (ImageView) findViewById(R.id.reviewsActive);
        mapTextView = (TextView) findViewById(R.id.mapTextView);
        closingTextView = (TextView) findViewById(R.id.closingTextView);
        categoriesTextView = (TextView) findViewById(R.id.categoriesTextView);
        initialTextView = (TextView) findViewById(R.id.initialTextView);
        priceLevelTextView = (TextView) findViewById(R.id.priceLevelTextView);
        saveTextView = (TextView) findViewById(R.id.saveTextView);
        diceTextView = (TextView) findViewById(R.id.diceTextView);
        mapActive = (ImageView) findViewById(R.id.mapActive);
        leftArrow = (ImageView) findViewById(R.id.leftArrow);
        rightArrow = (ImageView) findViewById(R.id.rightArrow);
        leftArrowFull = (ImageView) findViewById(R.id.leftArrowFull);
        rightArrowFull = (ImageView) findViewById(R.id.rightArrowFull);
        saveButton = (ToggleButton) findViewById(R.id.acceptButton);
        ignoreButton = (ToggleButton) findViewById(R.id.hideImageView);
        statusTextView = (TextView) findViewById(R.id.status);
        saveBubble = (BubbleLayout) findViewById(R.id.saveBubble);
        diceBubble = (BubbleLayout) findViewById(R.id.diceBubble);
        categoriesBubble = (BubbleLayout) findViewById(R.id.categoriesBubble);
        emptyReviews = (TextView) findViewById(R.id.emptyReviews);
        reviewDownArrow = (ImageView) findViewById(R.id.reviewArrowDown);
        mapDownArrow = (ImageView) findViewById(R.id.mapArrowDown);
        emptyProgress = (ProgressBar) findViewById(R.id.emptyProgressBar);
        cusineImage = (com.patrickmumot.eatingdice.Elements.SwipeImageView) findViewById(R.id.restaurantImage);
        titleTextView = (TextView) findViewById(R.id.justShakeTitle);
        currentTime = Calendar.getInstance().getTime();
        if (this instanceof IScrollListener) {
            scrollListener = (IScrollListener) this;
        } else {
            throw new RuntimeException(this.toString()
                    + " must implement IScrollListener");
        }

        fullImageDisplay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("viewpager","click");
                displayFullImage(false, null);
            }
        });

        diceRunnable = new Runnable() {
            @Override
            public void run() {
                Log.i("diceInfoDisplayed", diceInfoDisplayed+"");
                if (!diceInfoDisplayed) {
                    diceInfoDisplayed = true;
                    Log.i("firstShake", firstShake+"");
                   // if(firstShake) {
                        diceBubble.setVisibility(View.VISIBLE);
                   // }
                    //diceBubble.setVisibility(View.VISIBLE);
                    handler.postDelayed(diceRunnable, 4000);
                } else {
                    diceBubble.setVisibility(View.INVISIBLE);
                    // diceBubble.setVisibility(View.INVISIBLE);
                }
            }
        };

        saveRunnable = new Runnable() {
            @Override
            public void run() {
                if (!infoDisplayed) {
                    infoDisplayed = true;
                    saveBubble.setVisibility(View.VISIBLE);
                    //diceBubble.setVisibility(View.VISIBLE);
                    handler.postDelayed(saveRunnable, 4000);
                } else {
                    saveBubble.setVisibility(View.INVISIBLE);
                   // diceBubble.setVisibility(View.INVISIBLE);
                }
            }
        };


        categoriesRunnable = new Runnable() {
            @Override
            public void run() {
                if (!categoriesSelected) {
                    categoriesSelected = true;
                    categoriesBubble.setVisibility(View.VISIBLE);
                    handler.postDelayed(categoriesRunnable, 4000);
                } else {
                    categoriesBubble.setVisibility(View.INVISIBLE);
                }
            }
        };

        mDrawerLayout = findViewById(R.id.drawer_layout);

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(MenuItem menuItem) {
                        // set item as selected to persist highlight
                        menuItem.setChecked(true);
                        // close drawer when item is tapped
                        mDrawerLayout.closeDrawers();

                        // Add code here to update the UI based on the item selected
                        // For example, swap UI fragments here

                        return true;
                    }
                });

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                back();
            }
        });
        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                next();
            }
        });
        directionsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GetDirections();
            }
        });
        MobileAds.initialize(this, "ca-app-pub-4797792331734502~2334093847");
        myMediaPlayer = MediaPlayer.create(MainActivityRemake.this, R.raw.dice_roll);


        mMapFragment = (MapFragment) getFragmentManager()
                .findFragmentById(R.id.mapFragment);

        Typeface custom_font = Typeface.createFromAsset(getAssets(), "fonts/SourceSansPro-SemiBold.ttf");
        Typeface regular_font = Typeface.createFromAsset(getAssets(), "fonts/SourceSansPro-Regular.ttf");
        reviewTextView.setTypeface(custom_font);
        mapTextView.setTypeface(custom_font);
        nameTextView.setTypeface(custom_font);
        statusTextView.setTypeface(custom_font);
        categoriesTextView.setTypeface(custom_font);
        saveTextView.setTypeface(custom_font);
        diceTextView.setTypeface(custom_font);

        distanceTextView.setTypeface(regular_font);
        initialTextView.setTypeface(regular_font);

        //statusTextView.setTypeface(regular_font);

        Typeface logo_font = Typeface.createFromAsset(getAssets(), "fonts/coolvetica.ttf");
        titleTextView.setTypeface(logo_font);

        reviewListener = new ScrollGestureListener();
        reviewListener.setInfo(scrollListener, null, detailType.Review, 0);
        reviewGestureDetector = new GestureDetector(this,reviewListener);
        reviewLayout.setmGestureDetector(reviewGestureDetector);

        mapListener = new ScrollGestureListener();
        mapListener.setInfo(scrollListener, null, detailType.Map, 0);
        mapGestureDetector = new GestureDetector(this,mapListener);
        mapLayout.setmGestureDetector(mapGestureDetector);

        deliveryRestaurant.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CenterToast(searchedPlace.Name + " delivers! Call or visit their website.");
            }
        });

        prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        int loginState = checkLoginState();
        checkCurrentSavedPlace();
        distance = getDistance();
        delivery = getDelivery();
        activityRef = this;
        googlePlacesApi = new GooglePlaces(this);
        sql = new SQL(this);
        totalShakeCount = 0;
        inMain = true;
        setUpAd();
        //startSearch = true;
        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        mAccelerometer = mSensorManager
                .getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        mShakeDetector = new ShakeDetector();
        fullImageDisplay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                displayFullImage(false, null);
            }
        });

        nameTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CenterToast(searchedPlace.Name);
            }
        });

        websiteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (searchedPlace != null && searchedPlace.Details.WebsiteUrl != "") {
                    Uri uri = Uri.parse(searchedPlace.Details.WebsiteUrl);
                    Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                    startActivity(intent);
                }
            }
        });

        phoneButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (searchedPlace != null && searchedPlace.Details.InternationalPhoneNumber != "") {
                    openPhone();
                }
            }
        });

        locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        locationListener = new LocationListener() {
            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {
            }

            @Override
            public void onProviderEnabled(String provider) {
            }

            @Override
            public void onProviderDisabled(String provider) {
            }

            @Override
            public void onLocationChanged(Location location) {
                Log.i("location", location +"");
                myLocation = location;
                if (searchedPlace != null && searchedPlace.Details != null) {
                    double distance = DataController.round(DataController.distance(searchedPlace.Details.Geometry.Latitude, myLocation.getLatitude(),
                            searchedPlace.Details.Geometry.Longitude, myLocation.getLongitude(), 0, 0) / 1000, 1);
                    distanceTextView.setText(String.valueOf(distance));
                    if(searchedPlace.Saved && distance <= 0.1 && !searchedPlace.complete){
                        sql.completePlace(searchedPlace);
                        searchedPlace.complete = true;
                    }
                }

                // Do work with new location. Implementation of this method will be covered later.
            }
        };
        final Animation myAnim = AnimationUtils.loadAnimation(this, R.anim.bounce);
        BounceInterpolator interpolator = new BounceInterpolator(0.2, 20);
        myAnim.setInterpolator(interpolator);
        diceFragment = (newDiceFragment) getSupportFragmentManager().findFragmentById(R.id.diceFragment);
        diceFragment.setAnimation(myAnim);
        saveBubble.setVisibility(View.INVISIBLE);
        diceBubble.setVisibility(View.INVISIBLE);
        diceFragment.startShake();
        saveButton.setText(null);
        saveButton.setTextOn(null);
        saveButton.setTextOff(null);
        saveButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                infoDisplayed = true;
                saveBubble.setVisibility(View.INVISIBLE);
                if (isChecked) {
                    setCurrentSavedPlace();
                } else {
                    emptyCurrentSavedPlace();
                }

                placeSaved = isChecked;
                searchedPlace.Saved = isChecked;
            }
        });


        ignoreButton.setText(null);
        ignoreButton.setTextOn(null);
        ignoreButton.setTextOff(null);
        ignoreButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    searchedPlace.IgnoreId = sql.addIgnore(searchedPlace);
                } else {
                    if(searchedPlace.IgnoreId != 0){
                        sql.removeIgnore(searchedPlace.IgnoreId);
                        searchedPlace.IgnoreId = 0;
                    }
                }

                ignoreCriteria = sql.getAllIgnores();
            }
        });

        switch (loginState) {
            case INITIAL_LOGIN:
                initialStartUse();
                STATE = INITIAL_LOGIN;
                break;

            case NORMAL_LOGIN:
                regularStartUse();
                break;

            default:
                break;
        }
    }

    private void setUpAd(){
        mInterstitialAd = new InterstitialAd(this);
        mInterstitialAd.setAdUnitId("ca-app-pub-4797792331734502/5507052094"); //

        mInterstitialAd.loadAd(new AdRequest.Builder().build());

        mInterstitialAd.setAdListener(new AdListener() {
            @Override
            public void onAdClosed() {
                // Load the next interstitial.
                mInterstitialAd.loadAd(new AdRequest.Builder().build());
            }

        });
    }

    private void openDetails(detailType type) {
        if(!expanding){
            if(currentDetailType == null) {  //must expand
                currentDetailType = type;
                lastDetailType = type;
                setDetails();
                toggleDetails(true);
                if(type == detailType.Map)
                {
                    frameDisplay.setVisibility(View.VISIBLE);
                    reviewDisplay.setVisibility(View.INVISIBLE);
                    fixMap(searchedPlace.Details.Geometry);
                } else {
                    frameDisplay.setVisibility(View.INVISIBLE);
                    reviewDisplay.setVisibility(View.VISIBLE);
                }
                activateDetails(type);
            } else
            {
                if (currentDetailType == type) { // same type
                    currentDetailType = null;
                    toggleDetails(false);
                    activateDetails(null);
                } else {
                    currentDetailType = type;
                    lastDetailType = type;
                    activateDetails(type);

                    if(type == detailType.Map)
                    {
                        frameDisplay.setVisibility(View.VISIBLE);
                        reviewDisplay.setVisibility(View.INVISIBLE);
                    } else {
                        frameDisplay.setVisibility(View.INVISIBLE);
                        reviewDisplay.setVisibility(View.VISIBLE);
                    }
                }
            }
        }
    }

    private boolean checkIfContainsInIgnore(List<IgnoreCriteria> ignores, String name){
        for (IgnoreCriteria ignore:ignores) {
            if(ignore.GetName().contains(name)){
                return true;
            }
        }
        return false;
    }

    private boolean checkIfContainsInPlaceList(String name){
        for (String location:selectedLocationsNames) {
            if(location.contains(name) || name.contains(location)){
                return true;
            }
        }
        return false;
    }

    private void activateDetails(detailType detail){
        if(detail == null){
            mapDownArrow.setVisibility(View.INVISIBLE);
            reviewDownArrow.setVisibility(View.INVISIBLE);
            mapActive.setVisibility(View.INVISIBLE);
            reviewsActive.setVisibility(View.INVISIBLE);
        } else {
            if(detail == detailType.Map){
                reviewsActive.setVisibility(View.INVISIBLE);
                mapActive.setVisibility(View.VISIBLE);
                mapDownArrow.setVisibility(View.VISIBLE);
                reviewDownArrow.setVisibility(View.INVISIBLE);
            } else if(detail == detailType.Review){
                reviewsActive.setVisibility(View.VISIBLE);
                mapActive.setVisibility(View.INVISIBLE);
                mapDownArrow.setVisibility(View.INVISIBLE);
                reviewDownArrow.setVisibility(View.VISIBLE);
            }
        }
    }

    private void setDetails() {
        if(currentDetailType == detailType.Review){
            frameDisplay.getLayoutParams().width = 0;
            reviewDisplay.getLayoutParams().width = screenWidth;
            frameDisplay.setVisibility(View.INVISIBLE);
            reviewDisplay.setVisibility(View.VISIBLE);
        } else {
            reviewDisplay.getLayoutParams().width = 0;
            frameDisplay.getLayoutParams().width = screenWidth;
            reviewDisplay.setVisibility(View.INVISIBLE);
            frameDisplay.setVisibility(View.VISIBLE);
        }
    }

    private void toggleDetails(boolean open){
        int startHeight;
        int endHeight;
        final View view = findViewById(R.id.detailsLayout);
        startHeight = view.getHeight();
        expanding = true;

        if(open){
            expanded = true;
            diceFragment.resizeDice(true);
            endHeight =  (int) (screenHeight / 3.2 );
            closingLayout.setVisibility(View.INVISIBLE);
            priceLevelLayout.setVisibility(View.INVISIBLE);
        } else {
            expanded = false;
            diceFragment.resizeDice(false);
            closingLayout.setVisibility(View.VISIBLE);
            priceLevelLayout.setVisibility(View.VISIBLE);
            endHeight = 0;
        }
        ValueAnimator slideAnimator = ValueAnimator
                .ofInt(startHeight, endHeight)
                .setDuration(500);

        slideAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                // get the value the interpolator is at
                Integer value = (Integer) animation.getAnimatedValue();
                // I'm going to set the layout's height 1:1 to the tick
                view.getLayoutParams().height = value.intValue();
                // force all layouts to see which ones are affected by
                // this layouts height change
                view.requestLayout();
            }
        });
        slideAnimator.addListener(new AnimatorListenerAdapter()
        {
            @Override
            public void onAnimationEnd(Animator animation)
            {
                expanding = false;
                // done
            }
        });

// create a new animationset
        AnimatorSet set = new AnimatorSet();
// since this is the only animation we are going to run we just use
// play
        set.play(slideAnimator);
// this is how you set the parabola which controls acceleration
        set.setInterpolator(new AccelerateDecelerateInterpolator());
// start the animation
        set.start();

    }

    private void openPhone() {
        if (ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(activityRef, new String[]{Manifest.permission.CALL_PHONE}, PHONE_PERMISSION_REQUEST_CODE);
            return;
        }
        Uri uri = Uri.parse("tel:" + searchedPlace.Details.InternationalPhoneNumber);
        Intent intent = new Intent(Intent.ACTION_DIAL, uri);
        startActivity(intent);
    }

    private void CenterToast(String message){
        Toast toast = Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_category:
                // foundRestaurants.clear();
                if(!placeSaved){
                    startCategoriesActivity();
                } else {
                    CenterToast(searchedPlace.Name + " is saved, click on X!");
                }

                // User chose the "Settings" item, show the app settings UI...
                return true;
            case android.R.id.home:
                mDrawerLayout.openDrawer(GravityCompat.START);
                return true;
            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);

        }
    }

    private void startCategoriesActivity() {
        saveBubble.setVisibility(View.INVISIBLE);
        diceBubble.setVisibility(View.INVISIBLE);
        categoriesBubble.setVisibility(View.INVISIBLE);
        inMain = false;
        handler.removeCallbacks(saveRunnable);
        handler.removeCallbacks(categoriesRunnable);
        categoriesSelected = true;
        Intent intent = new Intent(this, CategoriesActivity.class);
        startActivityForResult(intent, 1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == 1) {

                updateStatus();
                infoDisplayed = false;
                if(currentDetailType != null){
                    openDetails(currentDetailType);
                }

                seenLocations.clear();
                somethingSelected = true;
                locationsSelected = 0;
                shakeCount = 0;
                firstShake = true;
                totalShakeCount = 0;
                currentPlaceIndex = 0;
                diceFragment.endShake();
                DataController.EmptyUsedNumbers();
                backButton.setVisibility(View.INVISIBLE);
                nextButton.setVisibility(View.INVISIBLE);
                emptyDisplay.setVisibility(View.VISIBLE);
                detailsButtonsDisplay.setVisibility(View.INVISIBLE);
                restaurantDisplay.setVisibility(View.INVISIBLE);
                setDistance(distance);
                setDelivery(delivery);
                diceInfoDisplayed = false;
                //handler.postDelayed(diceRunnable, 10000);
                searchLocations = 0;
                shake();
            }
        } else {
            Log.i("check if", "search place");
            inMain = true;
            String type = "restaurant";
            if(delivery){
                type = "meal_delivery";
            } else {
                type = searchedCuisine.getType();
            }

            if(searchedPlace == null){
                downloadLocation = myLocation;
                googlePlacesApi.SearchNearMe(downloadLocation, type, MainActivityRemake.searchedCuisine.getKeyWord(), MainActivityRemake.searchedCuisine.getNextPage(), Direction.NEARBYMAIN);
            }
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return true;
    }

    private void shake() {
        if (searchedPlace != null && searchedPlace.Saved && placeSaved) {
            CenterToast(searchedPlace.Name + " is saved, click on X!");
            return;
        }

        if(ready){
            if (searchedCuisine != null) {
                if(shook) {
                    return;
                }

                shakeCount++;
                totalShakeCount++;


                /*handler.removeCallbacks(diceRunnable);
                handler.removeCallbacks(saveRunnable);*/
                initialTextView.setVisibility(View.INVISIBLE);
                emptyProgress.setVisibility(View.VISIBLE);
                myMediaPlayer.seekTo(0);
                myMediaPlayer.start();
                Vibrator v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
                // Vibrate for 500 milliseconds
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    v.vibrate(VibrationEffect.createOneShot(500,VibrationEffect.DEFAULT_AMPLITUDE));
                }else{
                    //deprecated in API 26
                    v.vibrate(500);
                }

                shook = true;
                diceFragment.startShake();
                STATE = DISPLAY_LOADING;
                //saveCheckBox.setChecked(false);
                emptyCurrentSavedPlace();
                setNewLocation();
            } else {
                startCategoriesActivity();
                diceFragment.endShake();
            }
        }
    }

    private void back(){
        if(!placeSaved){
            if((currentPlaceIndex) > 0){
                currentPlaceIndex--;
                searchedPlace = seenLocations.get(currentPlaceIndex);
                googlePlacesApi.SearchDetails(searchedPlace.PlaceId);
            }
        } else {
            CenterToast(searchedPlace.Name + " is saved, click on X!");
        }
    }

    private void next(){
        if(!placeSaved){

            if((seenLocations.size()-(currentPlaceIndex+ 1)) > 0){

                currentPlaceIndex++;
                searchedPlace = seenLocations.get(currentPlaceIndex);
                googlePlacesApi.SearchDetails(searchedPlace.PlaceId);
            }
        } else {
            CenterToast(searchedPlace.Name + " is saved, click on X!");
        }
    }

    private void setNewLocation() {
        int locationAmount = googlePlaces.size();
        Log.i("newlocations", locationAmount+"");
        if (locationAmount > 0) {
            if(locationsSelected > (googlePlaces.size()-1)){
                locationsSelected = 0;

                firstCheck = false;
                DataController.EmptyUsedNumbers();
                CenterToast("Restarting places.");
            }
            if(firstCheck){
                searchedPlace = DataController.GetPlaceClosest(googlePlaces, locationsSelected);
            } else {
                searchedPlace = DataController.GetPlaceRandom(googlePlaces);
            }
            locationsSelected++;
            Log.i("loc selected", searchedPlace.Name);
            googlePlacesApi.SearchDetails(searchedPlace.PlaceId);
        } else {
            diceFragment.endShake();
            CenterToast("No places found around you. Look for a new cuisine!");
        }
    }

    private int checkLoginState() {
        return prefs.getInt("loginState", INITIAL_LOGIN);
    }

    private double getDistance() {
        return prefs.getFloat("distance", 10);
    }

    private boolean getDelivery() {
        return prefs.getBoolean("delivery", false);
    }

    private boolean update() {
        //UPDATE FOR PATCH
        boolean update = prefs.getBoolean("update_0_12", true);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putBoolean("update_0_12", false);
        editor.commit();
        return update;
    }
    

    private void checkCurrentSavedPlace() {
        String googlePlaceId = prefs.getString("googlePlaceId", null);
        String googlePlaceName = prefs.getString("googlePlaceName", null);
        if (googlePlaceId != null && googlePlaceName != null) {
            searchedPlace = new GooglePlace();
            searchedPlace.PlaceId = googlePlaceId;
            searchedPlace.Name = googlePlaceName;
            searchedPlace.Saved = true;
        }
    }

    private void setLoginState(int state) {
        SharedPreferences.Editor editor = prefs.edit();
        editor.putInt("loginState", state);
        editor.commit();
    }

    private void setDistance(double newDistance) {
        SharedPreferences.Editor editor = prefs.edit();
        editor.putFloat("distance", (float)newDistance);
        editor.commit();
    }

    private void setDelivery(boolean isDelivery) {
        SharedPreferences.Editor editor = prefs.edit();
        editor.putBoolean("delivery", isDelivery);
        editor.commit();
    }

    private void setCurrentSavedPlace() {
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("googlePlaceId", searchedPlace.PlaceId);
        editor.putString("googlePlaceName", searchedPlace.Name);
        editor.commit();
        searchedPlace.Id = (int) sql.savePlace(searchedPlace, totalShakeCount, delivery);
    }

    private void emptyCurrentSavedPlace() {
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("googlePlaceId", null);
        editor.putString("googlePlaceName", null);
        editor.commit();
    }

    private void initialStartUse() {
        setUpInitialCuisines();
        setUpInitialPage();
        sql.initiateCuisineTable();


        //setUpLocation();
    }

    private void setUpInitialCuisines() {
        int index = 0;
        for (String cuisineName : CuisineTable.cuisines) {
            Cuisine cuisine = new Cuisine();
            cuisine.setId(index);
            cuisine.setName(cuisineName);

            initialCuisineList.add(cuisine);
            index++;
        }

    }

    private void setUpInitialPage() {
        Button goButton = (Button) findViewById(R.id.goButton);
        initialDisplay.setVisibility(View.VISIBLE);
        mainDisplay.setVisibility(View.INVISIBLE);
        diceFragment.endShake();
        goButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                        ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    getPermissions();

                } else {
                    setUpLocation();
                }
            }
        });
    }

    private void displayFullImage(boolean visible, GooglePhoto photo) {
        imageDisplay = visible;

        if (visible) {
            //Picasso.with(this).load(googlePlacesApi.CreatePhotoUrl(photo)).into(fullImage);
            //mViewPagerAdapter.setImages(null);
            mFullViewPagerAdapter.setImages(searchedPlace.Details.Photos);
            fullPageViewPager.setAdapter( mFullViewPagerAdapter );
            fullPageViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                @Override
                public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

                }

                @Override
                public void onPageSelected(int position) {
                    if(position == 0){
                        setImageVisible( leftArrowFull, false);
                        setImageVisible( rightArrowFull, true);
                    } else if(position == searchedPlace.Details.Photos.size()-1){
                        setImageVisible( leftArrowFull, true);
                        setImageVisible( rightArrowFull, false);
                    } else {
                        setImageVisible( leftArrowFull, true);
                        setImageVisible( rightArrowFull, true);
                    }
                }

                @Override
                public void onPageScrollStateChanged(int state) {

                }
            });

            if(searchedPlace.Details.Photos.size() > 1){
                if(currentPhotoIndex == 0){
                    setImageVisible( leftArrowFull, false);
                    setImageVisible( rightArrowFull, true);
                } else if(currentPhotoIndex == searchedPlace.Details.Photos.size()-1){
                    setImageVisible( leftArrowFull, true);
                    setImageVisible( rightArrowFull, false);
                } else {
                    setImageVisible( leftArrowFull, true);
                    setImageVisible( rightArrowFull, true);
                }
            } else {
                setImageVisible( leftArrowFull, false);
                setImageVisible( rightArrowFull, false);
            }

            fullPageViewPager.setCurrentItem(currentPhotoIndex);
            mFullViewPagerAdapter.notifyDataSetChanged();

            fullImageDisplay.setVisibility(View.VISIBLE);

        } else {
            //fullImage.setImageBitmap(null);

            mViewPager.setCurrentItem(currentPhotoIndex);
            fullImageDisplay.setVisibility(View.INVISIBLE);
        }
    }

    private void setUpLocation() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            getPermissions();
            return;
        }
        Log.i("location", myLocation+"");
        if (myLocation == null) {
            handler.postDelayed(new Runnable(){
                @Override
                public void run() {
                    setUpLocation();
                }
            }, 1000);
        } else {
            getSQLCuisines();
            setUpMainEmptyDisplay();
        }
    }

    private void setUpMainEmptyDisplay() {
        myToolbar = (Toolbar) findViewById(R.id.mainToolbar);
        setSupportActionBar(myToolbar);
        emptyProgress.setVisibility(View.VISIBLE);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        myToolbar.setTitle("");
        myToolbar.setSubtitle("");

        ActionBar actionbar = getSupportActionBar();
        actionbar.setDisplayHomeAsUpEnabled(true);
        actionbar.setHomeAsUpIndicator(R.drawable.main_menu);
        Point size = new Point();
        getWindowManager().getDefaultDisplay().getSize(size);

        initialDisplay.setVisibility(View.INVISIBLE);
        mainDisplay.setVisibility(View.VISIBLE);
        screenWidth = size.x;
        screenHeight = size.y;

        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int height = displayMetrics.heightPixels;
        int width = displayMetrics.widthPixels;
        mShakeDetector.setOnShakeListener(new ShakeDetector.OnShakeListener() {
            @Override
            public void onShake(int count) {
                shake();
            }
        });

        mMapFragment.getMapAsync(this);
        if (searchedPlace != null) {

            googlePlacesApi.SearchDetails(searchedPlace.PlaceId);
            saveButton.setChecked(true);
            Log.i("searchedPLace", "activated - "+saveButton.isActivated());
            placeSaved = true;
            handler.removeCallbacks(saveRunnable);
            backButton.setVisibility(View.INVISIBLE);
            nextButton.setVisibility(View.INVISIBLE);
        }
        handler.postDelayed(categoriesRunnable, 10000);
        setUpSearch();

    }

    private void setUpSearch(){

        if (searchedCuisine == null) {
            searchedCuisine = initialCuisineList.get(0);
        }

        String type = "restaurant";
        if(delivery){
            type = "meal_delivery";
        } else {
            type = searchedCuisine.getType();
        }
        downloadLocation = myLocation;
        searchedCuisine.setNextPage("");
        Log.i("searched", searchedCuisine.getName() + " - " + searchedCuisine.getKeyWord());
        statusTextView.setText("Finding " + searchedCuisine.getName() + DataController.round(distance/1000, 1) + " km near you...");
        googlePlacesApi.SearchNearMe(downloadLocation, type, searchedCuisine.getKeyWord(), searchedCuisine.getNextPage(), Direction.NEARBYMAIN);
    }

    private void updateStatus(){
        String searchString = "";
        if(searchedCuisine.getId() == 1){
            searchString = "place(s)";
        } else {
            searchString = searchedCuisine.getName() + " place(s)";
        }
        if(delivery){
            statusTextView.setText(locationsFound + " " +searchString + "  with delivery");
        } else {
            statusTextView.setText(locationsFound + " " + searchString + " within " + (int)distance + " km");
        }

    }

    @Override
    public void onResume() {
        if(myLocation != null){
            // Add the following line to register the Session Manager Listener onResume
            inMain = true;
            //  startLocationListener();
            mSensorManager.registerListener(mShakeDetector, mAccelerometer, SensorManager.SENSOR_DELAY_UI);

            Log.i("resume", searchedPlace + " - search place ; " + paused + " - startedSearch");
            if(downloadLocation != null && downloadLocation.distanceTo(myLocation) > 1000){
                String type = "";
                if(delivery){
                    type = "meal_delivery";
                } else {
                    type = searchedCuisine.getType();
                }

                googlePlacesApi.SearchNearMe(downloadLocation, type, searchedCuisine.getKeyWord(), searchedCuisine.getNextPage(), Direction.NEARBYMAIN);
            } else if(searchedPlace == null && paused){
                searchedCuisine.setNextPage("");
                setSearchedCuisine();
                googlePlaces.clear();
                setUpSearch();
            }
            paused = false;
        }

      // Log.i("onReusme", "YES - " + googlePlaces.size() + " - " + searchedCuisine.getName() + " and "+ searchedCuisine.end);
        super.onResume();
    }

    private void startLocationListener(){
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }

        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 100, locationListener);
    }

    @Override
    public void onPause() {
        // Add the following line to unregister the Sensor Manager onPause
        mSensorManager.unregisterListener(mShakeDetector);
        inMain = false;
        paused = true;
        super.onPause();
    }

    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                setLoginState(NORMAL_LOGIN);
                startService(new Intent(this, LocationService.class));
                inMain = true;
                setUpLocation();
            }
        }
    }

    private void getPermissions() {
        if (Build.VERSION.SDK_INT >= 23) { // Marshmallow
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION }, LOCATION_PERMISSION_REQUEST_CODE);
        }
    }


    private void getSQLCuisines() {
        boolean update = update();
        if(update){
            //update
            sql.tempFix();
        }
        initiateSQL();
        initialCuisineList = sql.getAllCuisines();
        if(initialCuisineList.size() == 0){
            sql.populateInitialCuisines();
            initialCuisineList = sql.getAllCuisines();
        }
        ignoreCriteria = sql.getAllIgnores();
        setSearchedCuisine();
    }

    private void initiateSQL(){
        sql.initiateCuisineTable();
        sql.initiatePlaceSelectedHistoryTable();
        sql.initiatePlaceIgnoreTable();
    }

    private void setSearchedCuisine(){
        for (Cuisine cuisine : initialCuisineList) {
            if (cuisine.getLookFor()) {
                searchedCuisine = cuisine;
            }
        }
    }

    private void regularStartUse() {
        startService(new Intent(this, LocationService.class));
        setUpLocation();
    }
    @Override
    public void getGooglePlaces(List<GooglePlace> googlePlaceList) {

    }

    @Override
    public void getGooglePlacesMain(List<GooglePlace> googlePlaceList) {
        if(googlePlaceList.size() == 0 && searchedCuisine.getNextPage() == ""){
            startCategoriesActivity();
            CenterToast("There are no " + searchedCuisine.getName() + " open at this time.");
            return;
        } else if(googlePlaceList.size() == 0) {
            Log.i("NONE", "no places");
            shake();
        } else {
            Log.i("loadingmore", googlePlaceList.size()+" places - " + searchedCuisine.end);
            String placeNumberString = "";
            for (GooglePlace place:googlePlaceList) {
                if (!checkIfContainsInPlaceList(place.Name) && !existsInIgnore(place) && (place.Distance <= distance || (delivery && place.Distance <= 20))) {
                    selectedLocationsNames.add(place.Name);
                    googlePlaces.add(place);
                    searchLocations ++;
                }
            }
            if(searchLocations > 20){
                placeNumberString = "20+";
            } else {
                placeNumberString = String.valueOf(searchLocations);
            }
            Log.i("loadingmore", " b4 - ");
            if(searchedCuisine.end){

                ready = true;
                if(googlePlaces.size() == 0){
                    startCategoriesActivity();
                    CenterToast("0 places for "+ searchedCuisine.getName()+", Increase range or choose different cuisine.");
                    diceFragment.endShake();
                } else {
                    if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.N){
                        googlePlaces.sort(new Comparator<GooglePlace>() {
                            @Override
                            public int compare(GooglePlace o1, GooglePlace o2) {
                                if (o1.Distance < o2.Distance) return -1;
                                if (o1.Distance > o2.Distance) return 1;
                                return 0;
                            }
                        });
                    }

                    DataController.EmptyUsedNumbers();
                    firstCheck = true;
                    currentPlaceIndex = 0;
                    locationsFound = placeNumberString;
                    updateStatus();
                    //if(!placeSaved){
                    //    initialTextView.setVisibility(View.VISIBLE);
                    //    emptyProgress.setVisibility(View.INVISIBLE);
                    //}
                    diceFragment.endShake();
                    if(inMain){
                        shake();
                    }
                }
            } else {
                Log.i("loadingmore", " not end - "+inMain);
                String Type = "";
                if(delivery){
                    Type = "meal_delivery";
                } else {
                    Type = searchedCuisine.getType();
                }
                Log.i("loadingmore", " after??");
                Log.i("looking again", searchedCuisine.getKeyWord());
                if(inMain) {
                    googlePlacesApi.SearchNearMe(downloadLocation, Type, searchedCuisine.getKeyWord(), MainActivityRemake.searchedCuisine.getNextPage(), Direction.NEARBYMAIN);
                }
            }
        }
    }

    public boolean existsInIgnore(GooglePlace place){
        for (IgnoreCriteria ignore:ignoreCriteria) {
            String ignorePlaceName = ignore.GetName();
            if(ignorePlaceName.contains(place.Name) || place.Name.contains(ignorePlaceName)){
                return true;
            }
        }
        return false;
    }

    @Override
    public void getGoogleDetails(GoogleDetails googleDetails) {
        Log.i("details", "received - " + googleDetails.OpenNow);
        searchedPlace.Details = googleDetails;
        if(inMain){
            setUpNewLocation();
        }

    }

    private void setUpNewLocation() {
        if(searchedPlace.Details.OpenNow){

            setUpReviews();

            restaurantImageHeight = cusineImage.getHeight();
            Log.i("photos", searchedPlace.Name + " - size: "+searchedPlace.Details.Photos.size());
            if (searchedPlace.Details.Photos.size() > 0) {
                cusineImage.setVisibility(View.INVISIBLE);
                mViewPager.setVisibility(View.VISIBLE);
                mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                    @Override
                    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

                    }

                    @Override
                    public void onPageSelected(int position) {
                        if(position == 0){
                            setImageVisible( leftArrow, false);
                            setImageVisible( rightArrow, true);
                        } else if(position == searchedPlace.Details.Photos.size()-1){
                            setImageVisible( leftArrow, true);
                            setImageVisible( rightArrow, false);
                        } else {
                            setImageVisible( leftArrow, true);
                            setImageVisible( rightArrow, true);
                        }
                    }

                    @Override
                    public void onPageScrollStateChanged(int state) {

                    }
                });
                mViewPagerAdapter.setImages(null);
                mViewPager.setAdapter( mViewPagerAdapter );
                mViewPagerAdapter.setImages(searchedPlace.Details.Photos);
                mViewPagerAdapter.setCuisine(searchedCuisine);

                mViewPagerAdapter.notifyDataSetChanged();
            } else {
                Picasso.with(this).load(getResId(searchedCuisine.getCuisineRegImage(), R.drawable.class)).resize(screenWidth, restaurantImageHeight)
                        .centerCrop().into(cusineImage);
                mViewPager.setVisibility(View.INVISIBLE);
                cusineImage.setVisibility(View.VISIBLE);
                cusineImage.setmGestureDetector(reviewGestureDetector);
                leftArrow.setVisibility(View.INVISIBLE);
                rightArrow.setVisibility(View.INVISIBLE);
            }

            CompleteSearch();
        } else {
            if(googlePlaces.contains(searchedPlace)){
                googlePlaces.remove(searchedPlace);
            }
            if(searchedPlace.Saved){
                CenterToast(searchedPlace.Name + " is saved, but it is currently closed!");
                searchedPlace = null;
                emptyCurrentSavedPlace();
            }

            setNewLocation();
        }

    }

    private void setImageVisible(ImageView image, boolean visible){
        if(visible){
            image.setVisibility(View.VISIBLE);
        } else {
            image.setVisibility(View.INVISIBLE);
        }
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
    private void fixMap(final com.patrickmumot.eatingdice.Models.Location targetlocation) {
        if(firstMapPan){
            mMapFragment.getMapAsync(new OnMapReadyCallback() {
                @Override
                public void onMapReady(final GoogleMap googleMap) {
                    googleMap.clear();

                    LatLng targetLatLng = new LatLng(targetlocation.Latitude, targetlocation.Longitude);
                    LatLng myLatLng = new LatLng(myLocation.getLatitude(), myLocation.getLongitude());
                    LatLngBounds.Builder builder = new LatLngBounds.Builder();
                    builder.include(myLatLng);
                    builder.include(targetLatLng);
                    firstMapPan = false;
                    final LatLngBounds bounds = builder.build();
                    if (bounds != null) {
                        final CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(bounds, 20);
                        String title = searchedPlace.Details.FormattedAddress;
                        MarkerOptions markerOptions;
                        if(title != null){
                            markerOptions = new MarkerOptions()
                                    .position(targetLatLng)
                                    .title(title);
                        } else {
                            markerOptions = new MarkerOptions()
                                    .position(targetLatLng)
                                    .title(searchedPlace.Name);
                        }


                        final Marker marker = googleMap.addMarker(markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.marker)));

                        googleMap.setOnMapLoadedCallback(new GoogleMap.OnMapLoadedCallback() {
                            @Override
                            public void onMapLoaded() {
                                if(cu != null){
                                    googleMap.animateCamera(cu, new GoogleMap.CancelableCallback() {
                                        public void onCancel() {
                                        }

                                        public void onFinish() {
                                            CameraUpdate zout = CameraUpdateFactory.zoomBy((float) -1.0);
                                            googleMap.animateCamera(zout);
                                            marker.showInfoWindow();
                                        }
                                    });
                                }

                            }
                        });

                    }

                }
            });
        }
    }



    private void setUpReviews() {
        removeReviews();
        displayReviews();
    }

    private void removeReviews() {
        for (android.app.Fragment fragment : selectedReviewFragments) {
            getFragmentManager().beginTransaction().remove(fragment).commit();
        }
    }

    private void displayReviews() {
        int i = 0;
        if(searchedPlace.Details.Reviews.size() > 0 ){
            emptyReviews.setVisibility(View.GONE);
            for (Review review : searchedPlace.Details.Reviews) {

                android.app.Fragment fragment = ReviewFragment.newInstance(review.AuthorName, review.ProfilePhotoUrl, review.Text, review.Rating);
                selectedReviewFragments.add(fragment);
                getFragmentManager().beginTransaction().add(R.id.fullReviewLayout, fragment, "review" + i).commit();
                i++;
            }
        } else {
            emptyReviews.setVisibility(View.VISIBLE);
        }

    }
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        //No call for super(). Bug on API Level > 11.
    }

    private void CompleteSearch() {


        STATE = DISPLAY_FOUND;

        firstMapPan = true;
        fixMap(searchedPlace.Details.Geometry);
        diceFragment.endShake();

        ratingBar.setRating((float) searchedPlace.Details.Rating);
        nameTextView.setText(searchedPlace.Name);

        double checkDistance = DataController.round(DataController.distance(searchedPlace.Details.Geometry.Latitude, myLocation.getLatitude(),
                searchedPlace.Details.Geometry.Longitude, myLocation.getLongitude(),0,0)/1000,1);


        distanceTextView.setText(String.valueOf(checkDistance));
        emptyDisplay.setVisibility(View.INVISIBLE);
        if(searchedPlace.Details.InternationalPhoneNumber != null  && searchedPlace.Details.InternationalPhoneNumber!= ""){
            phoneButton.setVisibility(View.VISIBLE);
        } else {
            phoneButton.setVisibility(View.INVISIBLE);
        }
        Log.i("price", searchedPlace.PriceLevel+"");
        if(searchedPlace.PriceLevel > 0  && !expanded){
            priceLevelLayout.setVisibility(View.VISIBLE);
            String dollars = "";
            if(searchedPlace.PriceLevel > 0){
                for (int i = 1; i <= searchedPlace.PriceLevel; i++){
                    dollars += "$";
                }
            }

            priceLevelTextView.setText(dollars);
        } else {
            priceLevelLayout.setVisibility(View.INVISIBLE);
        }
        Log.i("firstShakecomplete", firstShake+"");
        if(firstShake){
            handler.postDelayed(diceRunnable, 10000);
        }

        if(delivery){
            deliveryRestaurant.setVisibility(View.VISIBLE);
            distanceLayout.setVisibility(View.INVISIBLE);
        } else {
            deliveryRestaurant.setVisibility(View.INVISIBLE);
            distanceLayout.setVisibility(View.VISIBLE);
        }
        mapDownArrow.setVisibility(View.INVISIBLE);
        reviewDownArrow.setVisibility(View.INVISIBLE);

        if(searchedPlace.Details.WebsiteUrl != null  && searchedPlace.Details.WebsiteUrl!= ""){
            websiteButton.setVisibility(View.VISIBLE);
        } else {
            websiteButton.setVisibility(View.INVISIBLE);
        }
        Calendar calendar = Calendar.getInstance();
        int day = calendar.get(Calendar.DAY_OF_WEEK);
        String openTime = searchedPlace.Details.GetCloseHours(day - 1);
        if(openTime == "" || expanded){
            closingLayout.setVisibility(View.INVISIBLE);
        } else {
            closingTextView.setText(openTime);
            closingLayout.setVisibility(View.VISIBLE);
        }
        leftArrow.setVisibility(View.INVISIBLE);
        if(searchedPlace.Details.Photos.size() > 1){
            rightArrow.setVisibility(View.VISIBLE);
        } else {
            rightArrow.setVisibility(View.INVISIBLE);
        }
        restaurantDisplay.setVisibility(View.VISIBLE);
        detailsButtonsDisplay.setVisibility(View.VISIBLE);
        if(!infoDisplayed){
            handler.postDelayed(saveRunnable, 10000);
        }

        if(shook){
            seenLocations.add(searchedPlace);
            currentPlaceIndex = seenLocations.size()-1;
        }
        shook = false;
        Log.i("place saved", seenLocations.size()+" - INDEX - " + currentPlaceIndex);
        //Log.i("place saved", searchedPlace.Name+" vs " + seenLocations.get(0).Name);
        if(!placeSaved){
            if(seenLocations.size() > 0 && searchedPlace.equals(seenLocations.get(0)) && currentPlaceIndex == 0){
                backButton.setVisibility(View.INVISIBLE);
            } else {
                backButton.setVisibility(View.VISIBLE);
            }
            if (seenLocations.size() > 0 && searchedPlace.equals(seenLocations.get(seenLocations.size() - 1))) {
                nextButton.setVisibility(View.INVISIBLE);
            } else {
                nextButton.setVisibility(View.VISIBLE);
            }
        } else {
            nextButton.setVisibility(View.INVISIBLE);
            backButton.setVisibility(View.INVISIBLE);
        }
        if(mInterstitialAd.isLoaded()){
            if(shakeCount > 6){
                mInterstitialAd.show();
                shakeCount = 0;
            }
        } else {
            setUpAd();
        }

    }


    private void GetDirections(){
        Uri gmmIntentUri = Uri.parse("google.navigation:q="+ searchedPlace.Details.Geometry.Latitude+","+searchedPlace.Details.Geometry.Longitude+"");
        Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
        mapIntent.setPackage("com.google.android.apps.maps");
        startActivity(mapIntent);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        googleMap.getUiSettings().setMapToolbarEnabled(false);
        googleMap.setMapStyle(new MapStyleOptions(mapStyleJson));
        googleMap.setMyLocationEnabled(true);
        googleMap.setBuildingsEnabled(true);
        LatLng coordinate = new LatLng(myLocation.getLatitude(), myLocation.getLongitude()); //Store these lat lng values somewhere. These should be constant.
        CameraUpdate location = CameraUpdateFactory.newLatLngZoom(
                coordinate, 15);
        googleMap.animateCamera(location);
    }

    public static boolean doubleBackToExitPressedOnce = false;

    @Override
    public void onBackPressed() {
        if(imageDisplay){
            imageDisplay = false;
            displayFullImage(false, null);
            super.onBackPressed();
            return;
        }

        if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
            return;
        }



        if(somethingSelected){
            startCategoriesActivity();
        }

        doubleBackToExitPressedOnce = true;
        CenterToast("Please click BACK again to exit");

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                doubleBackToExitPressedOnce=false;
            }
        }, 3000);
    }
}
