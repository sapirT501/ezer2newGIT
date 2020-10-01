package com.example.ezer2new;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;
import com.parse.ParseAnalytics;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.sql.Timestamp;

public class contectUs extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    DrawerLayout drawerLayout;
    NavigationView navigationView;
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.nav_map:
                Intent i = new Intent(contectUs.this,ViewRequestsActivity.class);
                startActivity(i);
                break;
            case R.id.nav_home:
                Intent intent = new Intent(contectUs.this,contectUs.class);
                startActivity(intent);
                break;
            case R.id.nav_logout:
                ParseUser.logOut();
                intent = new Intent(contectUs.this,LoginActivity.class);
                startActivity(intent);
                break;
            case R.id.nav_profile:
                intent = new Intent(contectUs.this,Profile.class);
                startActivity(intent);
                break;
            case R.id.nav_message:
                intent = new Intent(contectUs.this,yourMassage.class);
                startActivity(intent);
                break;
            case R.id.nav_branch:
                intent = new Intent(contectUs.this,yessActivity.class);
                startActivity(intent);
                break;


        }
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contect_us);

        /**Navigatio**/
        drawerLayout=findViewById(R.id.constraint_layout2);
        navigationView=findViewById(R.id.nac_bar2);
        Menu menu =navigationView.getMenu();
        menu.findItem(R.id.nav_login).setVisible(false);
        navigationView.bringToFront();
        ActionBarDrawerToggle toggle =new ActionBarDrawerToggle(this,drawerLayout,R.string.nav_open,R.string.nav_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.setCheckedItem(R.id.nav_home);
        /*****************************************/


        getWindow().setBackgroundDrawable(getResources().getDrawable(R.drawable.mygardient));
        setTitle("יצירת קשר");
        final EditText editText =(EditText) findViewById(R.id.editTextTextMultiLine);
        Button snd=(Button) findViewById(R.id.button3);
        snd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!editText.getText().toString().isEmpty()) {
                    ParseObject contactUsMassage = new ParseObject("contactUs");
                    contactUsMassage.put("massege",editText.getText().toString() );
                    ParseUser currentUser = ParseUser.getCurrentUser();
                    if (currentUser != null) {
                        contactUsMassage.put("userMail",currentUser.getEmail());
                    } else {

                    }

                    contactUsMassage.saveInBackground(new SaveCallback() {
                        @Override
                        public void done(ParseException e) {
                            if(e==null){
                                Toast.makeText(contectUs.this,"נשלח בהצלחה ",Toast.LENGTH_SHORT).show();

                            }else{ Toast.makeText(contectUs.this,"בעיה בהתחברות לשרת, נסה/י שנית מאוחר יותר ",Toast.LENGTH_SHORT).show();}
                        }
                    });
                    editText.setText("");
                }
                else{
                    Toast.makeText(contectUs.this,"יש להקליד טקסט לפני השליחה",Toast.LENGTH_SHORT).show();
                }
            }
        });
        Button dial=(Button) findViewById(R.id.button4);
        dial.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel:000-000000"));
                startActivity(intent);
            }
        });

    }
}