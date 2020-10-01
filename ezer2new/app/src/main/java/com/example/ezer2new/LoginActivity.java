package com.example.ezer2new;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.RequestPasswordResetCallback;

public class LoginActivity extends AppCompatActivity {
    FirebaseAuth mFirebaseAuth;
    private FirebaseAuth.AuthStateListener mAuthStateListener;
    TextInputEditText mail1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Button rollButton = (Button)findViewById(R.id.button2);
        //final Button forgetButton = (Button)findViewById(R.id.forget);
        final EditText emailTextView=(EditText) findViewById(R.id.edit1);
        final EditText passwordTextView=(EditText) findViewById(R.id.edit2);

        rollButton.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email=emailTextView.getText().toString();
                String pwd=passwordTextView.getText().toString();
                if(pwd.isEmpty()&& email.isEmpty()){
                    Toast.makeText(LoginActivity.this,"fields Are",Toast.LENGTH_SHORT).show();
                }
                else if(email.isEmpty()){
                    emailTextView.setError("email r");
                    emailTextView.requestFocus();
                }
                else if(pwd.isEmpty()){
                    passwordTextView.setError("pwd r");
                    passwordTextView.requestFocus();
                }

                else if(!(pwd.isEmpty()&& email.isEmpty()))
                {


                    ParseUser.logInInBackground(email, pwd, new LogInCallback() {
                        public void done(ParseUser user, ParseException e) {
                            if (user != null) {
                                Intent i=new Intent(LoginActivity.this,yessActivity.class);
                                startActivity(i);
                            } else {
                                Toast.makeText(LoginActivity.this,"something went wrong",Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
                else{Toast.makeText(LoginActivity.this,"errrrr",Toast.LENGTH_SHORT).show();
                }
            } });



    }


}

