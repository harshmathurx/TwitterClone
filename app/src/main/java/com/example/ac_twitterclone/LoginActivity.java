package com.example.ac_twitterclone;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.parse.SignUpCallback;
import com.shashank.sony.fancytoastlib.FancyToast;

public class LoginActivity extends AppCompatActivity {
    EditText edtSignUpUsername, edtSignUpPassword, edtSignUpEmail,edtLoginUsername, edtLoginPassword;
    Button btnSignUp, btnLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        if(ParseUser.getCurrentUser() != null){
            transitionToUsersActivity();
        }
        edtSignUpEmail = findViewById(R.id.edtSignUpEmail);
        edtSignUpUsername = findViewById(R.id.edtSignUpUsername);
        edtSignUpPassword = findViewById(R.id.edtSignUpPassword);
        edtLoginUsername = findViewById(R.id.edtLoginUsername);
        edtLoginPassword = findViewById(R.id.edtLoginPassword);

        btnLogin = findViewById(R.id.btnLogin);
        btnSignUp = findViewById(R.id.btnSignUp);

        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final ParseUser parseUser = new ParseUser();
                parseUser.setUsername(edtSignUpUsername.getText().toString());
                parseUser.setPassword(edtSignUpPassword.getText().toString());
                parseUser.setEmail(edtSignUpEmail.getText().toString());
                parseUser.signUpInBackground(new SignUpCallback() {
                    @Override
                    public void done(ParseException e) {
                        if (e == null) {
                            FancyToast.makeText(LoginActivity.this, parseUser.get("username") + " is logged in now!!!", Toast.LENGTH_SHORT, FancyToast.SUCCESS, false).show();
                            transitionToUsersActivity();
                        } else {
                            ParseUser.logOut();
                            FancyToast.makeText(LoginActivity.this, e.getMessage(), Toast.LENGTH_SHORT, FancyToast.ERROR, false).show();
                        }
                    }
                });

            }

        });

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ParseUser.logInInBackground(edtLoginUsername.getText().toString(),edtLoginPassword.getText().toString(), new LogInCallback() {
                    @Override
                    public void done(ParseUser parseUser, ParseException e) {
                        if (parseUser != null) {
                            FancyToast.makeText(LoginActivity.this,"Login Successful",Toast.LENGTH_SHORT,FancyToast.SUCCESS,false).show();
                            Intent intent = new Intent(LoginActivity.this,ViewTweets.class);
                            startActivity(intent);
                        } else {
                            ParseUser.logOut();
                            Toast.makeText(LoginActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    }
                });
            }
        });

    }


    public void transitionToUsersActivity(){
        Intent intent = new Intent(LoginActivity.this, ViewTweets.class);
        startActivity(intent);
    }
}