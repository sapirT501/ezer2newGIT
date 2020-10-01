package com.example.ezer2new;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;
import com.google.android.material.textfield.TextInputEditText;
import com.parse.GetCallback;
import com.parse.GetDataCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

public class Profile extends AppCompatActivity  implements NavigationView.OnNavigationItemSelectedListener {
    DrawerLayout drawerLayout;
    NavigationView navigationView;
    TextView name;
    TextView mail;
    TextView phone;
    TextInputEditText name1;
    TextInputEditText mail1;
    TextInputEditText phone1;
    ImageView photo;
    Bitmap mIcon_val;
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.nav_map:
                Intent i = new Intent(Profile.this,ViewRequestsActivity.class);
                startActivity(i);
                break;
            case R.id.nav_home:
                Intent intent = new Intent(Profile.this,contectUs.class);
                startActivity(intent);
                break;
            case R.id.nav_logout:
                ParseUser.logOut();
                intent = new Intent(Profile.this,LoginActivity.class);
                startActivity(intent);
                break;
            case R.id.nav_profile:
                intent = new Intent(Profile.this,Profile.class);
                startActivity(intent);
                break;
            case R.id.nav_message:
                intent = new Intent(Profile.this,yourMassage.class);
                startActivity(intent);
                break;

            case R.id.nav_branch:
                intent = new Intent(Profile.this,yessActivity.class);
                startActivity(intent);
                break;


        }
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        /**Navigatio**/
        drawerLayout=findViewById(R.id.constraint_layout5);
        navigationView=findViewById(R.id.nac_bar5);
        Menu menu =navigationView.getMenu();
        menu.findItem(R.id.nav_login).setVisible(false);
        navigationView.bringToFront();
        ActionBarDrawerToggle toggle =new ActionBarDrawerToggle(this,drawerLayout,R.string.nav_open,R.string.nav_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);

        navigationView.setCheckedItem(R.id.nav_profile);
        /*****************************************/

        getWindow().setBackgroundDrawable(getResources().getDrawable(R.drawable.mygardient));
        setTitle(" ");
        name =(TextView) findViewById(R.id.profile_name);
        mail =(TextView) findViewById(R.id.profile_mail);
        phone =(TextView) findViewById(R.id.profile_phone);
      //  photo=(ImageView) findViewById(R.id.profile_image);
        final ParseUser currentUser = ParseUser.getCurrentUser();
        if (currentUser != null) {
            name.setText(currentUser.get("fullName").toString());
            mail.setText(currentUser.getEmail());
            phone.setText(currentUser.get("phonerNumber").toString());

            //final ParseFile applicantResume = (ParseFile)currentUser.get("photo");

            //applicantResume.getDataInBackground();

           // Picasso.get().load("https://i.pinimg.com/originals/87/64/ce/8764cefa9f88169972a8a20f2a69e8ce.jpg").placeholder(R.drawable.patient_profile_pic).resize(100, 100).into(photo);

        }
        Button btn =(Button) findViewById(R.id.button_profile);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Defining the popup screen
                AlertDialog.Builder dialogBuilder= new AlertDialog.Builder(getWindow().getContext());
                final View afiliate = getLayoutInflater().inflate(R.layout.alert,null);//Instantiates a layout XML file into its corresponding View objects.Inflate a new view hierarchy from the specified xml resource.
                name1 =(TextInputEditText) afiliate.findViewById(R.id.pop_name);//Access via the new view object
                mail1 =(TextInputEditText) afiliate.findViewById(R.id.pop_mail);
                phone1 =(TextInputEditText) afiliate.findViewById(R.id.pop_phone);
                if (currentUser != null) {
                    name1.setText(currentUser.get("fullName").toString());
                    mail1.setText(currentUser.getEmail());
                    phone1.setText(currentUser.get("phonerNumber").toString());


                Button update=afiliate.findViewById(R.id.button_update);
                update.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        currentUser.setEmail(mail1.getText().toString());
                        currentUser.put("fullName",name1.getText().toString());
                        currentUser.put("email", mail1.getText().toString());
                        currentUser.put("phonerNumber", phone1.getText().toString());

                        Intent intent
                                = new Intent(Profile.this,Profile.class);
                        startActivity(intent);

                    }
                });  }
                //After defining pop when button clicked
                dialogBuilder.setView(afiliate);
                AlertDialog dialog = dialogBuilder.create();
                dialog.show();
            }

        });


    }
}