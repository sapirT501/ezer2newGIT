package com.example.ezer2new;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
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

import com.google.android.material.navigation.NavigationView;
import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.ParseAnalytics;
import com.parse.ParseException;
import com.parse.ParseGeoPoint;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

public class yourMassage extends AppCompatActivity  implements NavigationView.OnNavigationItemSelectedListener {
    ListView listView;
    DrawerLayout drawerLayout;
    NavigationView navigationView;
    ArrayList<String> title=new ArrayList<String>();
    ArrayList<String> desc=new ArrayList<String>();
    ArrayList<String> locID =new ArrayList<String>();
    MyAdapter adapter;
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.nav_map:
                Intent i = new Intent(yourMassage.this,ViewRequestsActivity.class);
                startActivity(i);
                break;
            case R.id.nav_home:
                Intent intent = new Intent(yourMassage.this,contectUs.class);
                startActivity(intent);
                break;
            case R.id.nav_logout:
                ParseUser.logOut();
                intent = new Intent(yourMassage.this,LoginActivity.class);
                startActivity(intent);
                break;
            case R.id.nav_profile:
                intent = new Intent(yourMassage.this,Profile.class);
                startActivity(intent);
                break;
            case R.id.nav_message:
                intent = new Intent(yourMassage.this,yourMassage.class);
                startActivity(intent);
                break;
            case R.id.nav_branch:
                intent = new Intent(yourMassage.this,yessActivity.class);
                startActivity(intent);
                break;


        }
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_your_massage);
        getWindow().setBackgroundDrawable(getResources().getDrawable(R.drawable.mygardient));
        setTitle("הודעות");
        /**Navigatio**/
        drawerLayout=findViewById(R.id.constraint_layout4);
        navigationView=findViewById(R.id.nac_bar4);
        Menu menu =navigationView.getMenu();
        menu.findItem(R.id.nav_login).setVisible(false);
        navigationView.bringToFront();
        ActionBarDrawerToggle toggle =new ActionBarDrawerToggle(this,drawerLayout,R.string.nav_open,R.string.nav_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.setCheckedItem(R.id.nav_message);
        /*****************************************/
         listView =(ListView) findViewById(R.id.listView) ;
        adapter= new MyAdapter(this,title,desc);

        listView.setAdapter(adapter);
        /**If item was clicked show delete alert**/
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
               final int which_item=i;//The item was clicked
               new AlertDialog.Builder(yourMassage.this).setIcon(R.drawable.ic_baseline_delete_24).setTitle("מחיקת פריט").setMessage("האם לבצע את מחיקת הפריט?").setPositiveButton(
                       "אישור", new DialogInterface.OnClickListener() {
                           @Override
                           public void onClick(DialogInterface dialogInterface, int i) {
                               //find the object with the same object id and delete it
                               ParseQuery<ParseObject> query = ParseQuery.getQuery("usersMassege");
                               try {
                                   ParseObject object= query.get(locID.get(which_item));
                                   object.deleteInBackground();
                               } catch (ParseException e) {
                                   e.printStackTrace();
                               }

                               //remove from temporary data
                               locID.remove(which_item);
                               title.remove(which_item);
                              desc.remove(which_item);
                               Intent intent = new Intent(yourMassage.this, yourMassage.class);
                               startActivity(intent);
                           }
                       }
               ).setNegativeButton("ביטול", null).show();


                return true;
            }
        });

        /**Display masseges on screen*/
        ParseQuery<ParseObject> query= ParseQuery.getQuery("usersMassege");
        ParseUser currentUser = ParseUser.getCurrentUser();
        if (currentUser != null) {
            query.whereEqualTo("userMail", currentUser.getEmail().toString());

            query.findInBackground(new FindCallback<ParseObject>() {
                @Override
                public void done(List<ParseObject> objects, ParseException e) {
                    if (e == null) {


                        if (objects.size() > 0) {
                            for (ParseObject object : objects) {
                                String s =  object.get("subject").toString();
                                String m = object.get("massege").toString();
                                locID.add(object.getObjectId().toString());
                                title.add(s);
                                desc.add(m);


                            }

                        }
                    } else {
                        title.add("אין הודעות");
                        desc.add(" ");

                    }
                    adapter.notifyDataSetChanged();
                }
            });


        }

    }
    /**Custom adapter**/
    class MyAdapter extends ArrayAdapter<String>{
        Context context;
        ArrayList<String> rTitle;
        ArrayList<String> rMsg;
        MyAdapter(Context context1, ArrayList<String> title, ArrayList<String>  msg){
            super(context1,R.layout.row,R.id.subject,title);
            context=context1;
            rTitle=title;
            rMsg=msg;
        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            LayoutInflater layoutInflater =(LayoutInflater) getApplication().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View row =layoutInflater.inflate(R.layout.row,parent,false);
            TextView title=(TextView) row.findViewById(R.id.subject);
            TextView msg=(TextView) row.findViewById(R.id.massege);
             title.setText(rTitle.get(position));
             msg.setText(rMsg.get(position));
             return  row;
        }
    }
}
