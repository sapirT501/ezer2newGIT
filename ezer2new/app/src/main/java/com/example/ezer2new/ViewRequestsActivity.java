package com.example.ezer2new;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.Manifest;
import android.app.Activity;
import android.app.StatusBarManager;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.geofire.GeoFire;
import com.firebase.geofire.GeoLocation;
import com.firebase.geofire.GeoQuery;
import com.firebase.geofire.GeoQueryDataEventListener;
import com.firebase.geofire.GeoQueryEventListener;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.LocationSettingsStates;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.events.Event;
import com.parse.FindCallback;
import com.parse.ParseAnalytics;
import com.parse.ParseException;
import com.parse.ParseGeoPoint;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;


import java.util.ArrayList;
import java.util.EventListener;
import java.util.List;
import java.util.Map;

public class ViewRequestsActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    DrawerLayout drawerLayout;
    NavigationView navigationView;
    ListView requestListView;
    ArrayList<String> request = new ArrayList<String>();
    ArrayList<Integer> locID = new ArrayList<Integer>();
    ArrayList<Double> requestLatitude = new ArrayList<Double>();
    ArrayList<Double> requestLongtitude = new ArrayList<Double>();
    ArrayAdapter arrayAdapter;
    LocationManager locationManager;
    LocationListener locationListener;

    private LocationSettingsRequest.Builder builder;

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.nav_map:
                Intent i = new Intent(ViewRequestsActivity.this, ViewRequestsActivity.class);
                startActivity(i);
                break;
            case R.id.nav_home:
                Intent intent = new Intent(ViewRequestsActivity.this, contectUs.class);
                startActivity(intent);
                break;
            case R.id.nav_logout:
                ParseUser.logOut();
                intent = new Intent(ViewRequestsActivity.this, LoginActivity.class);
                startActivity(intent);
                break;
            case R.id.nav_profile:
                intent = new Intent(ViewRequestsActivity.this, Profile.class);
                startActivity(intent);
                break;
            case R.id.nav_message:
                intent = new Intent(ViewRequestsActivity.this, yourMassage.class);
                startActivity(intent);
                break;
            case R.id.nav_branch:
                intent = new Intent(ViewRequestsActivity.this, yessActivity.class);
                startActivity(intent);
                break;


        }
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    public void updateListView(Location location) {
        if (location != null) {
            request.clear();
            ParseQuery<ParseObject> query = ParseQuery.getQuery("Locations");
            final ParseGeoPoint geoPointLocation = new ParseGeoPoint(location.getLatitude(), location.getLongitude());
            query.whereNear("location", geoPointLocation);
            query.setLimit(10);//The limit of location displayed
            query.findInBackground(new FindCallback<ParseObject>() {
                @Override
                public void done(List<ParseObject> objects, ParseException e) {
                    if (e == null) {
                        request.clear();
                        requestLatitude.clear();
                        requestLongtitude.clear();
                        if (objects.size() > 0) {
                            for (ParseObject object : objects) {
                                ParseGeoPoint requestLocation = (ParseGeoPoint) object.get("location");//Get the request location
                                if (requestLocation != null && object.get("Available").toString().matches("yes")) {
                                    Double distanceInKm = geoPointLocation.distanceInMilesTo(requestLocation) * (1.6);//turn miles to kilometers
                                    Double distanceOnDP = (double) Math.round(distanceInKm * 10) / 10;//level of occurecy

                                    request.add(distanceOnDP.toString() + " קילומטר " + "\n" + object.get("desc").toString());
                                    requestLatitude.add(requestLocation.getLatitude());
                                    requestLongtitude.add(requestLocation.getLongitude());
                                    locID.add((int) object.get("oId"));
                                }
                            }

                        }
                    } else {
                        request.add("אין קריאות בקרבתך");
                    }
                    arrayAdapter.notifyDataSetChanged();
                }
            });
        }


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        final LocationSettingsStates states = LocationSettingsStates.fromIntent(data);
        switch (requestCode) {
            case 1:
                switch (resultCode) {
                    case Activity.RESULT_OK:
                        // All required changes were successfully made

                        break;
                    case Activity.RESULT_CANCELED:
                        // The user was asked to change settings, but chose not to


                        break;
                    default:
                        break;
                }
                break;
        }
    }

           @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode==1) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {

                    locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);//if i have permission do so
                    Location lastKnownLocation= locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);

                    updateListView(lastKnownLocation);
                }

            }

        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_requests);
        ParseAnalytics.trackAppOpenedInBackground(getIntent());
        setTitle("קרוב אליי");

        /**Navigatio**/
        drawerLayout = findViewById(R.id.constraint_layout3);
        navigationView = findViewById(R.id.nac_bar3);
        Menu menu = navigationView.getMenu();
        menu.findItem(R.id.nav_login).setVisible(false);
        navigationView.bringToFront();
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.nav_open, R.string.nav_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.setCheckedItem(R.id.nav_map);
        /*****************************************/


        /******GPS**/
        locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(@NonNull Location location) {
                updateListView(location);
            }

            @Override
            public void onProviderDisabled(@NonNull String provider) {

            }

            public void onProviderEnabled(@NonNull String provider) {

            }


        };

        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, 1);//if i dont have permmission ask the user
        } else {


            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
            //getting user location in lunch time
            Location lastKnownLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            //setting that location on the map
            if (lastKnownLocation != null) {
                updateListView(lastKnownLocation);
            }

        }
        /*****************************/


        getWindow().setBackgroundDrawable(getResources().getDrawable(R.drawable.mygardient));
        requestListView = (ListView) findViewById(R.id.list);
        arrayAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, request);
        //arrayAdapter= new ViewRequestsActivity.MyAdapter(getWindow().getContext(),title,desc);
        requestListView.setAdapter(arrayAdapter);

        requestListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if (ContextCompat.checkSelfPermission(ViewRequestsActivity.this, android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                    //check that we can get location that we need to send
                    // i-number of row that user pressed on l-
                    Location lastKnownLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                    if (requestLatitude.size() > i && requestLongtitude.size() > i && locID.size() > i && lastKnownLocation != null) {

                        Intent intent = new Intent(getApplicationContext(), MapsActivity.class);
                        intent.putExtra("requestLatitude", requestLatitude.get(i));
                        intent.putExtra("requestLongtitude", requestLongtitude.get(i));
                        intent.putExtra("yourLatitude", lastKnownLocation.getLatitude());
                        intent.putExtra("yourLongitude", lastKnownLocation.getLongitude());

                        intent.putExtra("locationId", locID.get(i));
                        startActivity(intent);
                    }
                }
            }
        });
        /**Ask for permission and turn on **/

        final LocationRequest locationRequest = new LocationRequest();//How much accurate the location should be
        builder = new LocationSettingsRequest.Builder().addLocationRequest(locationRequest);
        Task<LocationSettingsResponse> result = LocationServices.getSettingsClient(ViewRequestsActivity.this).checkLocationSettings(builder.build());
        result.addOnCompleteListener(new OnCompleteListener<LocationSettingsResponse>() {
            @Override
            public void onComplete(@NonNull Task<LocationSettingsResponse> task) {
                try {
                    task.getResult(ApiException.class);//If GPS enabled nothing happens.Otherwize exception is thrown.
                } catch (ApiException e) {
                    switch (e.getStatusCode()) {

                        case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                            // Location settings are not satisfied. But could be fixed by showing the user
                            // a dialog.

                            try {
                                ResolvableApiException resolvableApiException = (ResolvableApiException) e;
                                resolvableApiException.startResolutionForResult(ViewRequestsActivity.this, 1);


                            } catch (IntentSender.SendIntentException ex) {
                                ex.printStackTrace();
                            } catch (ClassCastException ex) {
                            }

                            break;
                        case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                            break;
                    }
                }
            }
        });
        FusedLocationProviderClient fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        LocationCallback locationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                super.onLocationResult(locationResult);
                if (locationResult != null) {
                 updateListView(locationResult.getLastLocation());
                }
            }
        };
    }





    }








