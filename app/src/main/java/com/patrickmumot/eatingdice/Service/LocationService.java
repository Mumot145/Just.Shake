package com.patrickmumot.eatingdice.Service;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import android.Manifest;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.app.ActivityCompat;
import android.util.Log;

import com.patrickmumot.eatingdice.MainActivityRemake;

public class LocationService extends Service {
    private LocationManager mLocationManager;
    private LocationListener mLocationListener = new LocationListener() {
        @Override
        public synchronized void onLocationChanged(Location l) {
            Log.i("serviceloc", "location "+l);
            mLocation = l;
            MainActivityRemake.myLocation = l;
            stopSelf();
        }

        @Override
        public void onProviderDisabled(String provider) {
        }

        @Override
        public void onProviderEnabled(String provider) {
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {
        }
    };

    private LocationSettings mLocationSettings;
    private boolean mAlarm = false;
    private Location mLocation = null;
    private Timer mStopTimer = null;

    @Override
    public void onCreate() {
        super.onCreate();
        Log.i("serviceloc", "create");
        mLocationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mStopTimer != null) {
            mStopTimer.cancel();
        }
        mLocationManager.removeUpdates(mLocationListener);
        PartialLock.release();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (intent.getBooleanExtra("ALARM", false)) {
            mAlarm = true;
        }
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return Service.START_REDELIVER_INTENT;
        }
        if (!mLocationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
            Log.w("serviceloc", "network provider not enabled, will try to use last known location");
            Location l = getLastKnownLocation();

            if (l != null) {
                mLocation = l;
                MainActivityRemake.myLocation = l;
            } else {
                Log.w("serviceloc", "unable to obtain last known location");
            }
            stopSelf();
        } else {
            mLocationManager.requestLocationUpdates(
                    LocationManager.NETWORK_PROVIDER, 100, 0, mLocationListener);

            mStopTimer = new Timer();
            mStopTimer.schedule(new TimerTask() {

                @Override
                public void run() {
                    // stop after 1 minute, regardless of
                    // whether we successfully got the location
                    // or not
                    stopSelf();
                    mStopTimer = null;
                }
            }, 1000 * 60);
        }
        return START_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    private Location getLastKnownLocation() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return null;
        }
        List<String> providers = mLocationManager.getProviders(true);
        Location bestLocation = null;
        for (String provider : providers) {
            Location l = mLocationManager.getLastKnownLocation(provider);
            if (l == null) {
                continue;
            }
            if (bestLocation == null
                    || l.getAccuracy() < bestLocation.getAccuracy()) {
                Log.d("service", "found best last known location: "+l);
                bestLocation = l;
            }
        }
        mLocationManager.requestLocationUpdates(
                LocationManager.GPS_PROVIDER, 0, 0, mLocationListener);
        if (bestLocation == null) {
            return null;
        }
        return bestLocation;
    }
}