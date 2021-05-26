package com.example.betterapp;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class mapScreen extends AppCompatActivity implements LocationListener {


    public SupportMapFragment mapFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mapscreen);

        mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);

        if (ContextCompat.checkSelfPermission(mapScreen.this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(mapScreen.this, new String[]{
                    Manifest.permission.ACCESS_FINE_LOCATION
            }, 100);
        }
        }


    @Override
    public void onLocationChanged(@NonNull Location location) {

        LatLng latlng = new LatLng(location.getLatitude(), location.getLongitude());
        Log.i("adskljsdf",String.valueOf(location.getSpeed()));
        Log.i("jsdhgfjsdkh", latlng.toString());

        mapFragment.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(@NonNull GoogleMap googlemap) {
                //map = googlemap;
                //add location markers for barcodes here



                //here be the stuffs
                MarkerOptions options = new MarkerOptions().position(latlng).title("YOU AM HERE");

                googlemap.animateCamera(CameraUpdateFactory.newLatLngZoom(latlng, 10));
                googlemap.addMarker(options);
            }
        });
    }

    }

