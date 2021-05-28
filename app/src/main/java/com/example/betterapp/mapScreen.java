package com.example.betterapp;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;
import android.os.Looper;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.SettingsClient;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.zxing.integration.android.IntentIntegrator;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

public class mapScreen extends AppCompatActivity implements swiper{


    public SupportMapFragment mapFragment;

    public FusedLocationProviderClient locserv;

    public boolean mapopen;

    LatLng[] markerslatlng = {new LatLng(50,0), new LatLng(123, 143), new  LatLng(49.78, 0.75), new LatLng(50.8, 0.6), new LatLng(50.4, 0.5), new LatLng(50.86, 0.58)};

    ArrayList<Marker> markers = new ArrayList<Marker>();

    public MarkerOptions usermark = new MarkerOptions().position(new LatLng(0, 0)).title("YOU AM HERE");
    public Marker user;

    //moving location working
    public LocationCallback locationCallback = new LocationCallback() {
        @Override
        public void onLocationResult(LocationResult locationResult){
            if(locationResult==null){return;}
            for(Location location : locationResult.getLocations()){
                Log.i("location", location.toString());
                mapFragment.getMapAsync(new OnMapReadyCallback() {
                    @Override
                    public void onMapReady(GoogleMap googleMap) {
                        Log.i("ready", "map is ready");

                        LatLng userloc = new LatLng(location.getLatitude(), location.getLongitude());
                        user.setPosition(userloc);//changes users marker position to current location

                        //zoom 15;
                        googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(userloc, 14));


                        //user.setvisable(false);


                        //5 markers ^,, about 2/3 show

                        for (Marker loc : markers){
                            if (((loc.getPosition().latitude - userloc.latitude) < 0.5 && (loc.getPosition().latitude - userloc.latitude) > -0.5) && ((loc.getPosition().longitude - userloc.longitude) < 0.5 && (loc.getPosition().latitude - userloc.longitude) > -0.5)) {
                                Log.i("inrange", "asdjhsadddddddddd");
                                //MarkerOptions mk = new MarkerOptions().position(loc).title("treasure");
                                loc.setVisible(true);

                            }
                            else{
                                //hide
                                loc.setVisible(false);//hide markers not near user for performence

                            }
                        }
                    }
                });
            }
        }
    };
    public LocationRequest request;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mapscreen);

        mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);

        locserv = LocationServices.getFusedLocationProviderClient(this);

        request = LocationRequest.create();
        request.setInterval(1000);
        request.setFastestInterval(1000);
        request.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        mapFragment.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap googleMap) {
                user = googleMap.addMarker(usermark);
                for (LatLng latlng : markerslatlng) {
                    MarkerOptions mko = new MarkerOptions().position(latlng).title("treasure");
                    Marker mk = googleMap.addMarker(mko);
                    markers.add(mk);
                }
            }
        });
        if (ContextCompat.checkSelfPermission(mapScreen.this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(mapScreen.this, new String[]{
                    Manifest.permission.ACCESS_FINE_LOCATION
            }, 100);
        }
        else {
            mapopen = true;
            checkstartLocationUpdates();

        }
            /*
            while(mapopen){
                try {
                    Thread.sleep(1000);
                    generateMarkers();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }*/
        }

    private void checkstartLocationUpdates() {
        LocationSettingsRequest locrequest = new LocationSettingsRequest.Builder().addLocationRequest(request).build();
        SettingsClient client = LocationServices.getSettingsClient(this);
        Task<LocationSettingsResponse> task = client.checkLocationSettings(locrequest);
        task.addOnSuccessListener(new OnSuccessListener<LocationSettingsResponse>() {
            @Override
            public void onSuccess(LocationSettingsResponse locationSettingsResponse) {
                startLocationUpdates();
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == 44){
            //might move &&
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                mapopen = true;
                Log.i("aaaaaaaaaaaa", "bbbbbbbbbbbbbbbbbbbb");
                //getcurrentlocation();
            }
        }
    }

    public void startLocationUpdates(){
        locserv.requestLocationUpdates(request, locationCallback, Looper.getMainLooper());
    }

    float x1, x2, y1, y2;
    long starttime, endtime;

    @Override
    public boolean onTouchEvent(MotionEvent touchEvent){
        if (touchEvent.getAction() == MotionEvent.ACTION_DOWN) {
            x1 = touchEvent.getX();
            y1 = touchEvent.getY();
            //start timer
            starttime = System.nanoTime();
            Log.i("start", String.valueOf(starttime));
        } else if (touchEvent.getAction() == MotionEvent.ACTION_UP) {
            x2 = touchEvent.getX();
            y2 = touchEvent.getY();
            //stop timer
            endtime = System.nanoTime();
            Log.i("end", String.valueOf(endtime));
            long time = (endtime - starttime)/1000000;

            left_swipe(x1, x2, time);
            right_swipe(x1, x2, time);
        }
        return false;
    }

    @Override
    public void left_swipe(float x1, float x2, long time){
        Log.i("speed",String.valueOf((x1 - x2) / time));
        if( ( (x1 - x2) / time) > 3 ) {
            Intent intent = new Intent(mapScreen.this, mainScreen.class);

            startActivity(intent);
            finish();
        }
    }

    //unused
    @Override
    public void right_swipe(float x1, float x2, long time){
    }
    //unused
    @Override
    public void up_swipe(float y1, float y2, long time){}
    //unused
    @Override
    public void down_swipe(float y1, float y2, long time){}

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mapopen = false;
        mainLoop.open = false;
        Log.i("destroy", "closing the app");
       // mainLoop.save();
        save();
    }
    public void save(){
        //store data tojson string
        Log.i("saving", "save");
        StringBuilder sb = new StringBuilder();
        String json;//???

        sb.append("{");
        String scorejson = String.format("\"score\":%s ,", mainLoop.score);
        String multjson = String.format("\"mult\":%s ,", mainLoop.mult);
        String crewjson = String.format("\"crew\":{\"total\":%s, \"cost\":%s},", mainLoop.totalcrew, mainLoop.crewcost);
        String shipjson = String.format("\"ships\":{\"total\":%s, \"cost\":%s},",mainLoop.totalships ,mainLoop.shipcost);
        sb.append(scorejson);
        sb.append(multjson);
        sb.append(crewjson);
        sb.append(shipjson);
        //do ships + crew before
        for (int index = 0; index < mainLoop.items.length;index++ ) {
            if (index > 0){
                sb.append(",");
            }
            itemClass item = mainLoop.items[index];

            String name = item.name;
            int cost = item.cost;
            int weight = item.weight;
            int profit = item.profit;
            int total = item.total;

            //String itemjson = " \"${name}\":{ \"total\":${total},\"weight\": ${weight}, \"cost\": ${cost}, \"profit\":${profit} }";

            String itemjson = String.format(" \"%s\":{ \"total\":%s,\"weight\": %s, \"cost\": %s, \"profit\":%s }", name, total, weight, cost, profit);
            Log.i("aaa", itemjson);
            sb.append(itemjson);
        }
        sb.append("}");
        json = sb.toString();
        Log.i("jsonsojso", json);
        //Log.i("saj", json.getBytes());
        FileOutputStream fos = null;

        try {
            fos = openFileOutput("data.txt", 0);

            String test = "{\"score\":10000.0 ,\"mult\":1.0 ,\"crew\":{\"total\":0, \"cost\":1000},\"ships\":{\"total\":0, \"cost\":2000}, \"rum\":{ \"total\":10,\"weight\": 10, \"cost\": 10, \"profit\":10 }}";
            fos.write(test.getBytes());

        } catch (FileNotFoundException e) {
            Log.i(e.toString(), "problem saving");
        } catch (IOException a) {
            a.printStackTrace();
            Log.i(a.toString(), "problem saving");
            //e.printStackTrace();
        } finally {
            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException e) {
                    Log.i(e.toString(), "problem saving");
                    //e.printStackTrace();
                }
            }
        }
    }

}

