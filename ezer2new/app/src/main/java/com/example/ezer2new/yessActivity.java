package com.example.ezer2new;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.parse.ParseUser;


public class yessActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    FirebaseAuth m;
    DatabaseReference rootRef, demoRef;
    DrawerLayout drawerLayout;
    NavigationView navigationView;
    Toolbar toolbar;
    WebView browser;
    class myWebViewClient extends WebViewClient {
        WebView browser;

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return true;
        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_yess);
        /**Setting web view**/
        browser = (WebView) findViewById(R.id.web);
        browser.setWebViewClient(new myWebViewClient());
        browser.getSettings().setLoadsImagesAutomatically(true);
        browser.getSettings().setJavaScriptEnabled(true);

        browser.loadUrl("https://www.ami.org.il/all-branches/");

        /***********NavigationBar****************/
        drawerLayout=findViewById(R.id.constraint_layout);
        navigationView=findViewById(R.id.nac_bar);

        Menu menu =navigationView.getMenu();
        menu.findItem(R.id.nav_login).setVisible(false);

        navigationView.bringToFront();
        ActionBarDrawerToggle toggle =new ActionBarDrawerToggle(this,drawerLayout,R.string.nav_open,R.string.nav_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);

        navigationView.setCheckedItem(R.id.nav_branch);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.nav_map:
                Intent i = new Intent(yessActivity.this,ViewRequestsActivity.class);
                startActivity(i);
                break;
            case R.id.nav_home:
                Intent intent = new Intent(yessActivity.this,contectUs.class);
                startActivity(intent);
                break;
            case R.id.nav_logout:
                ParseUser.logOut();
                 intent = new Intent(yessActivity.this,LoginActivity.class);
                startActivity(intent);
                break;
            case R.id.nav_profile:
                intent = new Intent(yessActivity.this,Profile.class);
                startActivity(intent);
                break;
            case R.id.nav_message:
                intent = new Intent(yessActivity.this,yourMassage.class);
                startActivity(intent);
                break;
            case R.id.nav_branch:
                break;


        }
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }
}