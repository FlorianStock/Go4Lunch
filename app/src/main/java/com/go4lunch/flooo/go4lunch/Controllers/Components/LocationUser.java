package com.go4lunch.flooo.go4lunch.Controllers.Components;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;

import java.text.ParseException;

public class LocationUser implements ActivityCompat.OnRequestPermissionsResultCallback
{

    private LocationManager mlocationmanager;
    private LocationListener mlocationListener;

    private Context context;
    private Activity activity;

    private  int MIN_DISTANCE = 10;
    private  int MIN_TIME = 0;

    private Location locationLast;

    public interface CallBacks
    {
        void currentPositionUserChanged(@Nullable Location locationUser) throws ParseException;
    }

    public LocationUser(Context context, Activity activity,int minDist,int minTime, final CallBacks callBacks)
    {
        this.activity = activity;
        this.context = context;
        MIN_DISTANCE = minDist;
        MIN_TIME  = minTime;

        mlocationmanager = (LocationManager) activity.getSystemService(Context.LOCATION_SERVICE);

        mlocationListener = new LocationListener()
        {
            @Override
            public void onLocationChanged(Location location)
            {
                try
                {
                    callBacks.currentPositionUserChanged(location);
                }
                catch (ParseException e)
                {
                    e.printStackTrace();
                }
            }

            @Override
            public void onStatusChanged(String s, int i, Bundle bundle)
            {

            }

            @Override
            public void onProviderEnabled(String s)
            {

            }

            @Override
            public void onProviderDisabled(String s)
            {

            }
        };

        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED)
        {
            ActivityCompat.requestPermissions(activity,new String[]{Manifest.permission.ACCESS_FINE_LOCATION},1);

        }
        else
        {

            locationLast = mlocationmanager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            mlocationmanager.requestLocationUpdates(LocationManager.GPS_PROVIDER, MIN_TIME, MIN_DISTANCE, mlocationListener);

            try
            {
                callBacks.currentPositionUserChanged(locationLast);
            }
            catch (ParseException e)
            {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults)
    {

        if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
        {
            if(ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED)
            {
                locationLast = mlocationmanager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                mlocationmanager.requestLocationUpdates(LocationManager.GPS_PROVIDER, MIN_TIME, MIN_DISTANCE, mlocationListener);
            }
        }
    }

    public Location getLocation() {
        return locationLast;
    }
}
