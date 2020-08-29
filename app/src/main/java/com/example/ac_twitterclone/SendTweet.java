package com.example.ac_twitterclone;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.parse.LogOutCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.shashank.sony.fancytoastlib.FancyToast;

public class SendTweet extends AppCompatActivity {
    EditText edtSendTweet;
    Button btnSendTweet;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_tweet);

        edtSendTweet = findViewById(R.id.edtSendTweet);


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.tweet_menu,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch(item.getItemId()){
            case(R.id.send_tweet):
                ParseObject parseObject = new ParseObject("MyTweet");
                parseObject.put("tweet",edtSendTweet.getText().toString());
                parseObject.put("user", ParseUser.getCurrentUser().getUsername());
                final ProgressDialog progressDialog = new ProgressDialog(this);
                progressDialog.setMessage("Sending...");
                progressDialog.show();
                parseObject.saveInBackground(new SaveCallback() {
                    @Override
                    public void done(ParseException e) {
                        if(e == null){
                            FancyToast.makeText(SendTweet.this,"Tweet sent", Toast.LENGTH_SHORT,FancyToast.INFO,false).show();
                            Intent intent = new Intent(SendTweet.this,ViewTweets.class);
                            startActivity(intent);
                        }
                        else{
                            FancyToast.makeText(SendTweet.this,e.getMessage(), Toast.LENGTH_SHORT,FancyToast.ERROR,false).show();
                            Intent intent = new Intent(SendTweet.this,ViewTweets.class);
                            startActivity(intent);
                        }
                    }
                });
                break;
        }

        return super.onOptionsItemSelected(item);
    }
}