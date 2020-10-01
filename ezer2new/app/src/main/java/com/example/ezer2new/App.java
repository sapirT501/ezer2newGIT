package com.example.ezer2new;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import com.parse.Parse;
import com.parse.ParseACL;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.SaveCallback;


import android.app.Application;
import android.util.Log;

public class App extends Application {
   @Override
   public void onCreate() {
        super.onCreate();
        Parse.enableLocalDatastore(this);

        Parse.initialize(new Parse.Configuration.Builder(getApplicationContext())
                .applicationId("myappID")
                // if defined
                .clientKey("eXYDdgtInyo5")
                .server("http://3.23.94.236/parse/")
                .build()
        );
              ParseACL defaultACL= new ParseACL();
       ParseACL.setDefaultACL(defaultACL,true);
    }
}