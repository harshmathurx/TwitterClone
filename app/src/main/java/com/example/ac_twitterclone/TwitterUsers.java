package com.example.ac_twitterclone;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckedTextView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.LogOutCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.shashank.sony.fancytoastlib.FancyToast;

import java.util.ArrayList;
import java.util.List;

public class TwitterUsers extends AppCompatActivity implements AdapterView.OnItemClickListener {

    private ListView usersListView;
    private ArrayList<String> usersArrayList;
    private ArrayAdapter adapter;
    private String followedUser = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_twitter_users);
        usersListView = findViewById(R.id.listView);

        FancyToast.makeText(TwitterUsers.this, ParseUser.getCurrentUser().get("username").toString() + " is logged in", Toast.LENGTH_SHORT,FancyToast.SUCCESS,false).show();

        usersArrayList = new ArrayList<>();
        adapter = new ArrayAdapter(this,android.R.layout.simple_list_item_checked,usersArrayList);
        usersListView.setChoiceMode(AbsListView.CHOICE_MODE_MULTIPLE);
        usersListView.setOnItemClickListener(TwitterUsers.this);

        try{
            ParseQuery<ParseUser> parseQuery = ParseUser.getQuery();

            parseQuery.whereNotEqualTo("username",ParseUser.getCurrentUser().getUsername());

            parseQuery.findInBackground(new FindCallback<ParseUser>() {
                @Override
                public void done(List<ParseUser> usersList, ParseException e) {
                    if(e == null ){

                        if(usersList.size() > 0){

                            for(ParseUser user : usersList){
                                usersArrayList.add(user.getUsername());

                            }
                            usersListView.setAdapter(adapter);

                            for(String user : usersArrayList){
                                if(ParseUser.getCurrentUser().getList("fanOf") != null) {
                                    if (ParseUser.getCurrentUser().getList("fanOf").contains(user)) {
                                        followedUser = followedUser + user;
                                        usersListView.setItemChecked(usersArrayList.indexOf(user), true);
//                                        FancyToast.makeText(TwitterUsers.this,ParseUser.getCurrentUser().getUsername() + " is following" + followedUser,Toast.LENGTH_LONG,FancyToast.INFO,false).show();
                                    }
                                }
                            }
                        }
                        else{
                            FancyToast.makeText(TwitterUsers.this,usersList.size()+"",Toast.LENGTH_SHORT,FancyToast.ERROR,false).show();
                        }
                    }
                }
            });
        }catch (Exception e){
            FancyToast.makeText(this,e.getMessage(),Toast.LENGTH_SHORT,FancyToast.ERROR,false).show();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.my_menu,menu);
        return super.onCreateOptionsMenu(menu);

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch(item.getItemId()){
            case(R.id.logout_item):
                ParseUser.getCurrentUser().logOutInBackground(new LogOutCallback() {
                    @Override
                    public void done(ParseException e) {
                        if(e == null){
                            Intent intent = new Intent(TwitterUsers.this,MainActivity.class);
                            startActivity(intent);
                            finish();
                        }
                    }
                });
                break;
            case(R.id.tweet):
                Intent intent = new Intent(TwitterUsers.this,SendTweet.class);
                startActivity(intent);
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        CheckedTextView checkedTextView = (CheckedTextView) view;

        if(checkedTextView.isChecked()){
            FancyToast.makeText(TwitterUsers.this,usersArrayList.get(i) + " is followed",Toast.LENGTH_SHORT,FancyToast.INFO,false).show();
            ParseUser.getCurrentUser().add("fanOf",usersArrayList.get(i));
        }else{
            FancyToast.makeText(TwitterUsers.this,usersArrayList.get(i) + " is now unfollowed",Toast.LENGTH_SHORT,FancyToast.INFO,false).show();

            ParseUser.getCurrentUser().getList("fanOf").remove(usersArrayList.get(i));
            List currentUserFanOfList = ParseUser.getCurrentUser().getList("fanOf");
            ParseUser.getCurrentUser().remove("fanOf");
            ParseUser.getCurrentUser().put("fanOf",currentUserFanOfList);
        }

        ParseUser.getCurrentUser().saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if(e == null){
                    FancyToast.makeText(TwitterUsers.this,"saved",Toast.LENGTH_SHORT,FancyToast.SUCCESS,false).show();
                }
            }
        });
    }


}