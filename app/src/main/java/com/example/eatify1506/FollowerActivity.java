package com.example.eatify1506;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.parse.GetCallback;
import com.parse.GetDataCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.lang.reflect.Type;
import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class FollowerActivity extends AppCompatActivity {

    String username;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_follower);

        Intent in = getIntent();

        username = in.getStringExtra("username");

        if (!username.equals(ParseUser.getCurrentUser().getUsername())) {
            TextView followerTextView = findViewById(R.id.followerTextView);
            followerTextView.setText("Takipçiler");
        }

        ParseQuery<ParseObject> query = new ParseQuery<ParseObject>("UserInfo");

        query.whereEqualTo("username", username);

        query.getFirstInBackground(new GetCallback<ParseObject>() {
            @Override
            public void done(ParseObject object, ParseException e) {
                if (e==null) {
                    Log.i("UserInfo query for follower list", "Success");


                    String json = object.getString("followerList");

                    Gson gson = new Gson();
                    Type type = new TypeToken<ArrayList<String>>() {}.getType();

                    final ArrayList<String> followerList = gson.fromJson(json, type);

                    LinearLayout linearLayout = findViewById(R.id.selfFollowerList);

                    for (int i = 0; i < followerList.size(); i++) {
                        final int inner_i = i;

                        LinearLayout rowLinearLayout = new LinearLayout(FollowerActivity.this);
                        rowLinearLayout.setOrientation(LinearLayout.HORIZONTAL);
                        LinearLayout.LayoutParams rowParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT, 100);
                        rowParams.setMargins(100, 20, 100, 20);
                        rowLinearLayout.setPadding(20,50,20,50);
                        // rowParams.gravity = Gravity.LEFT | Gravity.CENTER_VERTICAL;
                        rowLinearLayout.setBackgroundColor(getColor(R.color.rowBackground));
                        rowLinearLayout.setLayoutParams(rowParams);

                        final CircleImageView followingUserImg = new CircleImageView(FollowerActivity.this);
                        LinearLayout.LayoutParams imgParams = new LinearLayout.LayoutParams(200, 200, 25);
                        final ParseQuery<ParseObject> followingImgQuery = new ParseQuery<ParseObject>("UserInfo");
                        followingImgQuery.whereEqualTo("username", followerList.get(i));

                        followingImgQuery.getFirstInBackground(new GetCallback<ParseObject>() {
                            @Override
                            public void done(ParseObject object, ParseException e) {
                                if (e == null) {
                                    ParseFile file = (ParseFile) object.get("image");

                                    if (file != null) {
                                        file.getDataInBackground(new GetDataCallback() {
                                            @Override
                                            public void done(byte[] data, ParseException e) {
                                                if (e == null && data != null) {
                                                    Log.i("following user img downloaded", "success");
                                                    Bitmap bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);
                                                    followingUserImg.setImageBitmap(bitmap);
                                                }
                                            }
                                        });
                                    } else {
                                        followingUserImg.setImageResource(R.drawable.test_name);
                                    }

                                }
                            }
                        });

                        followingUserImg.setLayoutParams(imgParams);

                        TextView followingUsername = new TextView(FollowerActivity.this);
                        LinearLayout.LayoutParams nameParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, 75);
                        followingUsername.setText(followerList.get(i));
                        nameParams.gravity = Gravity.CENTER_VERTICAL | Gravity.START;

                        followingUsername.setLayoutParams(nameParams);

                        rowLinearLayout.addView(followingUserImg);
                        rowLinearLayout.addView(followingUsername);

                        rowLinearLayout.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent intent = new Intent(FollowerActivity.this, ProfileActivity.class);
                                intent.putExtra("username", followerList.get(inner_i));
                                startActivity(intent);
                            }
                        });

                        linearLayout.addView(rowLinearLayout);
                    }

                }
            }
        });


    }
}
