package com.example.ezer2new;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.RelativeLayout;

import com.firebase.geofire.GeoFire;
import com.firebase.geofire.GeoLocation;
import com.firebase.geofire.GeoQuery;
import com.firebase.geofire.GeoQueryEventListener;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.util.ArrayList;
import java.util.List;


public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {
        DrawerLayout drawerLayout;
        NavigationView navigationView;
    private GoogleMap mMap;
    LocationManager locationManager;
    LocationListener locationListener;
    Intent intent;
    RelativeLayout mapLayout;


     public void acceptRequest(View view){
            ParseQuery<ParseObject> query =ParseQuery.getQuery("Locations");
            query.whereEqualTo("oId",intent.getIntExtra("locationId",0));
             query.findInBackground(new FindCallback<ParseObject>() {
                 @Override
                 public void done(List<ParseObject> objects, ParseException e) {
                     if(e==null){

                         if(objects.size()>0)
                         {
                             for(ParseObject object : objects)
                             {
                                 object.put("Available","no");//so it wont be displayed next time after someone already chose to nevigate to that location

                                 object.saveInBackground(new SaveCallback() {
                                     @Override
                                     public void done(ParseException e) {
                                         if(e==null)
                                         {//Open google maps navigator as a new intent
                                             Intent directionsIntent= new Intent(android.content.Intent.ACTION_VIEW, Uri.parse("http://maps.google.com/maps?saddr=" + intent.getDoubleExtra("yourLatitude", 0) + "," + intent.getDoubleExtra("yourLongitude", 0) + "&daddr=" + intent.getDoubleExtra("requestLatitude", 0) + "," + intent.getDoubleExtra("requestLongtitude", 0)));
                                             startActivity(directionsIntent);
                                         }
                                     }
                                 });

                             }
                         }
                     }
                 }
             });
        }


@Override
protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);


    getWindow().setBackgroundDrawable(getResources().getDrawable(R.drawable.mygardient));
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
        .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
     mapLayout=(RelativeLayout) findViewById(R.id.map_layout);

        }

/**
 * Manipulates the map once available.
 * This callback is triggered when the map is ready to be used.
 * This is where we can add markers or lines, add listeners or move the camera. In this case,
 * we just add a marker near Sydney, Australia.
 * If Google Play services is not installed on the device, the user will be prompted to install
 * it inside the SupportMapFragment. This method will only be triggered once the user has
 * installed Google Play services and returned to the app.
 */
@Override
public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
    intent = getIntent();
    //Log.i("kk","kk");

    mapLayout.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
        @Override
        public void onGlobalLayout() {

            LatLng  userLocation =new LatLng(intent.getDoubleExtra("yourLatitude",0),intent.getDoubleExtra("yourLongitude",0));
            LatLng requestLocation= new LatLng(intent.getDoubleExtra("requestLatitude",0),intent.getDoubleExtra("requestLongtitude",0));
            //this is how we can add several markers into one map
            ArrayList<Marker> markers =new ArrayList<>();
            markers.add(  mMap.addMarker(new MarkerOptions().position(userLocation).title("your Location")));
            markers.add(  mMap.addMarker(new MarkerOptions().position(requestLocation).title("request").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE))));
            LatLngBounds.Builder builder=new LatLngBounds.Builder();
            for(Marker marker :markers){
                builder.include(marker.getPosition());//adding al the marker position into the builder
            }
            LatLngBounds bounds=builder.build();//setting it up for display
            int padding=60;//offset from edjest of map in pixels
            CameraUpdate cu=CameraUpdateFactory.newLatLngBounds(bounds,padding);//update camera
            mMap.animateCamera(cu);
        }
    });
        }




        }