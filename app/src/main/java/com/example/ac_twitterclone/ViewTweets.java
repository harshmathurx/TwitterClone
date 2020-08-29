package com.example.ac_twitterclone;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.parse.FindCallback;
import com.parse.LogOutCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ViewTweets extends AppCompatActivity {

    private ListView viewTweetsListView;
    private TextView txtNoUsers;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_tweets);

        txtNoUsers = findViewById(R.id.txtNoUsers);
        viewTweetsListView = findViewById(R.id.viewTweetsListView);

        final ArrayList<HashMap<String, String>> tweetList = new ArrayList<>();
        final SimpleAdapter adapter = new SimpleAdapter(ViewTweets.this, tweetList,
                android.R.layout.simple_expandable_list_item_2, new String[]{"tweetUsername", "tweetValue"},
                new int[]{android.R.id.text1, android.R.id.text2});

        try {
            ParseQuery<ParseObject> parseQuery = ParseQuery.getQuery("MyTweet");
            parseQuery.whereContainedIn("user", ParseUser.getCurrentUser().getList("fanOf"));
            parseQuery.findInBackground(new FindCallback<ParseObject>() {
                @Override
                public void done(List<ParseObject> objects, ParseException e) {
                    if (objects.size() > 0 && e == null) {
                        for (ParseObject tweetObject : objects) {
                            HashMap<String, String> userTweet = new HashMap<>();
                            userTweet.put("tweetUsername", tweetObject.getString("user"));
                            userTweet.put("tweetValue", tweetObject.getString("tweet"));
                            tweetList.add(userTweet);
                        }
                        viewTweetsListView.setAdapter(adapter);
                    } else {
                        viewTweetsListView.animate().alpha(0);
                        txtNoUsers.animate().alpha(1);
                    }
//                }
//            });
                }
            });
        }
            catch(Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.view_tweet_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case (R.id.view_tweets_logout_item):
                ParseUser.getCurrentUser().logOutInBackground(new LogOutCallback() {
                    @Override
                    public void done(ParseException e) {
                        if (e == null) {
                            Intent intent = new Intent(ViewTweets.this, LoginActivity.class);
                            startActivity(intent);
                            finish();
                        }
                    }
                });
                break;
            case (R.id.send_tweets_tweet):
                Intent intent = new Intent(ViewTweets.this, SendTweet.class);
                startActivity(intent);
                break;
            case(R.id.view_tweets_show_users):
                Intent intent1 = new Intent(ViewTweets.this,TwitterUsers.class);
                startActivity(intent1);
                break;
//            case(R.id.view_tweets_switch_users):
//                Intent

        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    //    @Override
//    public void onClick(View view) {
//
//    }


}



