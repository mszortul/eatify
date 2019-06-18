package com.example.eatify1506;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import androidx.fragment.app.FragmentTransaction;

import androidx.fragment.app.FragmentManager;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.GetCallback;
import com.parse.ParseAnalytics;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class MainActivity extends AppCompatActivity {

    HomeFragment homeFragment = new HomeFragment();
    SearchFragment searchFragment = new SearchFragment();
    AddFragment addFragment = new AddFragment();
    BookmarkFragment bookmarkFragment = new BookmarkFragment();
    ProfileFragment profileFragment = new ProfileFragment();

    ImageView home;
    ImageView add;
    ImageView search;
    ImageView bookmark;
    ImageView profile;

    String username;
    Bitmap userImageChangeBitmap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (ParseUser.getCurrentUser() != null) {
            //TextView sampleTextView = findViewById(R.id.topTitle);
            //sampleTextView.setText(ParseUser.getCurrentUser().getUsername());
        }

        home = findViewById(R.id.homeImageView);
        add = findViewById(R.id.addImageView);
        search = findViewById(R.id.searchImageView);
        bookmark = findViewById(R.id.bookmarkImageView);
        profile = findViewById(R.id.profileImageView);


        username = ParseUser.getCurrentUser().getUsername();

        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = manager.beginTransaction();
        fragmentTransaction.replace(R.id.vpContainer, homeFragment);
        fragmentTransaction.commit();
        changeColors(0);

        ParseAnalytics.trackAppOpenedInBackground(getIntent());

    }

    public void logOut(View view) {
        ParseUser.logOut();

        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
        finish();
    }


    public void changeColors(int i) {
        switch (i) {
            case 0:
                home.setImageResource(R.drawable.ic_home_black_24dp);
                search.setImageResource(R.drawable.ic_search_grey);
                add.setImageResource(R.drawable.ic_add_circle_outline_grey);
                bookmark.setImageResource(R.drawable.ic_bookmark_border_grey);
                profile.setImageResource(R.drawable.ic_person_grey);
                break;
            case 1:
                home.setImageResource(R.drawable.ic_home_grey);
                search.setImageResource(R.drawable.ic_search_black_24dp);
                add.setImageResource(R.drawable.ic_add_circle_outline_grey);
                bookmark.setImageResource(R.drawable.ic_bookmark_border_grey);
                profile.setImageResource(R.drawable.ic_person_grey);
                break;
            case 2:
                home.setImageResource(R.drawable.ic_home_grey);
                search.setImageResource(R.drawable.ic_search_grey);
                add.setImageResource(R.drawable.ic_add_circle_outline_black_24dp);
                bookmark.setImageResource(R.drawable.ic_bookmark_border_grey);
                profile.setImageResource(R.drawable.ic_person_grey);
                break;
            case 3:
                home.setImageResource(R.drawable.ic_home_grey);
                search.setImageResource(R.drawable.ic_search_grey);
                add.setImageResource(R.drawable.ic_add_circle_outline_grey);
                bookmark.setImageResource(R.drawable.ic_bookmark_border_black_24dp);
                profile.setImageResource(R.drawable.ic_person_grey);
                break;
            case 4:
                home.setImageResource(R.drawable.ic_home_grey);
                search.setImageResource(R.drawable.ic_search_grey);
                add.setImageResource(R.drawable.ic_add_circle_outline_grey);
                bookmark.setImageResource(R.drawable.ic_bookmark_border_grey);
                profile.setImageResource(R.drawable.ic_person_black_24dp);
                break;
        }
    }


    public void toolbarClick(View v) {

        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = manager.beginTransaction();

        switch (v.getId()) {
            case R.id.homeImageView:
                // HomeFragment homeFragment = new HomeFragment();
                changeColors(0);
                fragmentTransaction.replace(R.id.vpContainer, homeFragment);
                fragmentTransaction.commit();
                break;

            case R.id.appLogo:
                // HomeFragment homeFragment = new HomeFragment();
                changeColors(0);
                fragmentTransaction.replace(R.id.vpContainer, homeFragment);
                fragmentTransaction.commit();
                break;
            case R.id.searchImageView:
                // SearchFragment searchFragment= new SearchFragment();
                changeColors(1);
                fragmentTransaction.replace(R.id.vpContainer, searchFragment);
                fragmentTransaction.commit();
                break;
            case R.id.addImageView:
                // AddFragment addFragment = new AddFragment();
                /*
                changeColors(2);
                fragmentTransaction.replace(R.id.vpContainer, addFragment);
                fragmentTransaction.commit();
                */

                Intent intent = new Intent(getApplicationContext(), AddActivity.class);
                startActivity(intent);
                break;

            case R.id.bookmarkImageView:
                // BookmarkFragment bookmarkFragment = new BookmarkFragment();
                changeColors(3);
                fragmentTransaction.replace(R.id.vpContainer, bookmarkFragment);
                fragmentTransaction.commit();
                break;

            case R.id.profileImageView:
                // ProfileFragment profileFragment = new ProfileFragment();
                changeColors(4);
                fragmentTransaction.replace(R.id.vpContainer, profileFragment);
                fragmentTransaction.commit();
                break;
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.main_menu, menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == R.id.mainShare) {
            Log.i("Menu option clicked", "Share");
        } else if (item.getItemId() == R.id.mainLogout) {
            ParseUser.logOut();

            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
            finish();
        }

        return super.onOptionsItemSelected(item);
    }

}
