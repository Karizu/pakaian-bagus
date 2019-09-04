package com.example.pakaianbagus.util;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;

import com.example.pakaianbagus.models.Locations;
import com.example.pakaianbagus.presentation.home.HomeFragment;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Created by alfianhpratama on 03/09/2019.
 * Organization: UTeam
 */

public class GetLocation {
    private Context context;
    private Fragment fragment;
    private List<Locations> lokasiList = new ArrayList<>();

    public GetLocation(Context context, Fragment fragment) {
        this.context = context;
        this.fragment = fragment;
    }

    @SuppressLint("MissingPermission")
    public List getLocation() {
        LocationManager locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        LocationListener locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                Locations result = updateGPSInfo(location);
                if (result.getLatitude() != null && result.getLongitude() != null) {
                    ((HomeFragment) fragment).setLocation(result.getLongitude(), result.getLatitude());
                }
            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {

            }

            @Override
            public void onProviderEnabled(String provider) {

            }

            @Override
            public void onProviderDisabled(String provider) {

            }
        };

        if (Build.VERSION.SDK_INT < 23) {
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, locationListener);
        } else {
            if (ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions((Activity) context, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
            } else {
                locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, locationListener);
                Location currentLocation = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                updateGPSInfo(currentLocation);
            }
        }
        return lokasiList;
    }

    @SuppressLint("SetTextI18n")
    private Locations updateGPSInfo(Location location) {
        Locations result = new Locations();
        Geocoder geocoder = new Geocoder(context, Locale.getDefault());
        try {
            if (location != null) {
                List<Address> listAddresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
                if (listAddresses != null && listAddresses.size() > 0) {
                    result.setLatitude(location.getLatitude());
                    result.setLongitude(location.getLongitude());
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }
}
