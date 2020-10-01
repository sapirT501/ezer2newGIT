package com.example.ezer2new;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    //FirebaseAuth mFirebaseAuth;
    private static  int SPLASH=4000;
    Animation topAnim,botAnim;
    TextView sol;
    ImageView logo;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        /**ANIMATE SPLASH SCREEN**/
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);//Get the splash screen as full screen
        topAnim=AnimationUtils.loadAnimation(this,R.anim.top_animation);//After creating animation extract it into a variable
        botAnim= AnimationUtils.loadAnimation(this,R.anim.bottom_animation);
        sol= findViewById(R.id.slogan);
        logo= findViewById(R.id.imageView3);

        sol.setAnimation(botAnim);//Applying the animation setting on item
        logo.setAnimation(topAnim);
        new Handler().postDelayed(new Runnable() {//Activates the splash screen after we deffined the splash screen settings
            @Override
            public void run() {
                Intent i = new Intent(MainActivity.this,LoginActivity.class);
                startActivity(i);
                 finish();
            }
        },SPLASH);



    }
}