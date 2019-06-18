package com.example.eatify1506;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.parse.DeleteCallback;
import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.GetDataCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class ProfileActivity extends AppCompatActivity {

    String username;

    Bitmap userImageChangeBitmap;
    CircleImageView profileUserImage;

    RecyclerView recyclerView;
    ArrayList<RecipeFeed> recipeFeedArrayList = new ArrayList<>();
    AdapterFeed adapterFeed;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        Intent in = getIntent();

        final Button profileFollowButton = findViewById(R.id.profileFollowButton);
        profileUserImage = findViewById(R.id.profileUserImage);

        final TextView profileFollowerNumber = findViewById(R.id.profileFollowerNumber);
        final TextView profileFollowingNumber = findViewById(R.id.profileFollowingNumber);
        final TextView profileRecipeNumber = findViewById(R.id.profileRecipeNumber);
        final TextView profileUsername = findViewById(R.id.profileUsername);


        final ImageView reportProfileFlag = findViewById(R.id.reportProfileFlag);

        username = in.getStringExtra("username");

        profileUsername.setText(username);

        if (username.equals(ParseUser.getCurrentUser().getUsername())) {
            profileFollowButton.setVisibility(View.GONE);
            reportProfileFlag.setVisibility(View.GONE);
        } else {
            reportProfileFlag.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // what to when report flag is clicked
                    ParseQuery<ParseObject> alreadyReportedQuery = new ParseQuery<ParseObject>("Reports");
                    alreadyReportedQuery.whereEqualTo("profileReportUsername", username);
                    alreadyReportedQuery.whereEqualTo("reportOwner", ParseUser.getCurrentUser().getUsername());

                    alreadyReportedQuery.findInBackground(new FindCallback<ParseObject>() {
                        @Override
                        public void done(List<ParseObject> objects, ParseException e) {
                            if (e == null) {
                                if (objects.size() > 0) {
                                    Toast.makeText(ProfileActivity.this, "Kullanıcıyı daha önceden rapor ettiniz.", Toast.LENGTH_SHORT).show();
                                } else {
                                    AlertDialog.Builder builder = new AlertDialog.Builder(ProfileActivity.this);
                                    builder.setTitle("Kullanıcıyı rapor etmek istediğinize emin misiniz?");

                                    // Specify the type of input expected; this, for example, sets the input as a password, and will mask the text

                                    // Set up the buttons
                                    builder.setPositiveButton("Evet", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            ParseObject reportObject = new ParseObject("Reports");
                                            reportObject.put("profileReportUsername", username);
                                            reportObject.put("reportOwner", ParseUser.getCurrentUser().getUsername());

                                            reportObject.saveInBackground(new SaveCallback() {
                                                @Override
                                                public void done(ParseException e) {
                                                    if (e == null) {
                                                        Toast.makeText(ProfileActivity.this, "Kullanıcı rapor edildi.", Toast.LENGTH_SHORT).show();
                                                        Log.i("Report saved", "true");
                                                    }
                                                }
                                            });
                                        }
                                    });
                                    builder.setNegativeButton("Hayır", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            dialog.cancel();
                                        }
                                    });

                                    builder.show();
                                }
                            }
                        }
                    });
                }
            });
        }

        ParseQuery<ParseObject> query = new ParseQuery<ParseObject>("UserInfo");
        query.whereEqualTo("username", username);

        query.getFirstInBackground(new GetCallback<ParseObject>() {
            @Override
            public void done(ParseObject object, ParseException e) {
                if (e == null) {
                    int followerNumber = object.getInt("followerNumber");
                    int followingNumber = object.getInt("followingNumber");
                    int recipeNumber = object.getInt("recipeNumber");

                    profileFollowerNumber.setText(Integer.toString(followerNumber));
                    profileFollowingNumber.setText(Integer.toString(followingNumber));
                    profileRecipeNumber.setText(Integer.toString(recipeNumber));

                    ParseFile userImage = (ParseFile) object.get("image");

                    String json = object.getString("followerList");
                    Gson gson = new Gson();
                    Type type = new TypeToken<ArrayList<String>>() {
                    }.getType();

                    ArrayList<String> followerList = gson.fromJson(json, type);

                    if (followerList.contains(ParseUser.getCurrentUser().getUsername())) {
                        profileFollowButton.setText("Takip ediliyor");
                    } else  {
                        profileFollowButton.setText("Takip et");
                    }


                    if (userImage != null) {
                        userImage.getDataInBackground(new GetDataCallback() {
                            @Override
                            public void done(byte[] data, ParseException e) {
                                if (e == null && data != null) {
                                    Bitmap bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);
                                    profileUserImage.setImageBitmap(bitmap);
                                }
                            }
                        });
                    } else {
                        profileUserImage.setImageResource(R.drawable.test_name);
                    }
                }
            }
        });

        recyclerView = findViewById(R.id.profileRecyclerView);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        AdapterFeed.RecyclerViewOnItemLongClickListener longClickListener = new AdapterFeed.RecyclerViewOnItemLongClickListener() {
            @Override
            public boolean onItemLongClicked(int position) {

                final int pos = position;

                Log.i("Long pressed", "position " + position);

                RecipeFeed temp = recipeFeedArrayList.get(position);

                String recipeOwner = temp.getUsername();
                Log.i("recipe id outer", "test "+temp.getRecipeId());

                if (recipeOwner.equals(ParseUser.getCurrentUser().getUsername())) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(ProfileActivity.this);
                    builder.setTitle("Tarifi silmek istediğinizden emin misiniz?");

                    builder.setPositiveButton("Evet", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                            RecipeFeed temp = recipeFeedArrayList.get(pos);

                            final String recipeIdRemove = temp.getRecipeId();

                            ParseQuery<ParseObject> removeRecipeQuery = new ParseQuery<ParseObject>("RecipesTest");
                            removeRecipeQuery.whereEqualTo("objectId", recipeIdRemove);

                            Log.i("recipe id to remove", "test" + recipeIdRemove);


                            removeRecipeQuery.getFirstInBackground(new GetCallback<ParseObject>() {
                                @Override
                                public void done(ParseObject object, ParseException e) {
                                    if (e == null) {

                                        // Done, it works.
                                        final String jsonBookmark = object.getString("bookmarkUsers");
                                        Gson gson = new Gson();
                                        Type type = new TypeToken<ArrayList<String>>() {
                                        }.getType();

                                        // This contains all users that have bookmarked the recipe about to be deleted.
                                        ArrayList<String> usernamesRemove = gson.fromJson(jsonBookmark, type);

                                        for (String deleteItsBookmark : usernamesRemove) {
                                            ParseQuery<ParseObject> usernameRemoveQuery = new ParseQuery<ParseObject>("UserInfo");
                                            usernameRemoveQuery.whereEqualTo("username", deleteItsBookmark);

                                            // This deletes from one users bookmark list
                                            usernameRemoveQuery.getFirstInBackground(new GetCallback<ParseObject>() {
                                                @Override
                                                public void done(ParseObject object, ParseException e) {
                                                    if (e == null) {
                                                        String usersBookmarkJson = object.getString("bookmarkList");
                                                        Gson gson = new Gson();
                                                        Type type = new TypeToken<ArrayList<String>>() {
                                                        }.getType();

                                                        ArrayList<String> usersBookmarks = gson.fromJson(usersBookmarkJson, type);

                                                        usersBookmarks.remove(recipeIdRemove);

                                                        Gson gson1 = new Gson();
                                                        String usersBookmarkJsonSave = gson1.toJson(usersBookmarks);

                                                        object.put("bookmarkList", usersBookmarkJsonSave);

                                                        object.saveInBackground(new SaveCallback() {
                                                            @Override
                                                            public void done(ParseException e) {
                                                                if(e == null) {
                                                                    Log.i("Recipe removed from a users bookmarks", "success");
                                                                }
                                                            }
                                                        });


                                                    }
                                                }
                                            });
                                        }

                                        // If we are here we already deleted the recipeIdRemove from users' list, so we can finally delete the object itself / it was painful, im happy that its over :)
                                        object.deleteInBackground(new DeleteCallback() {
                                            @Override
                                            public void done(ParseException e) {
                                                if (e == null) {
                                                    Log.i("object removed from server", "success");
                                                }
                                            }
                                        });
                                    }
                                }
                            });

                            recipeFeedArrayList.remove(pos);
                            adapterFeed.notifyDataSetChanged();
                        }
                    });
                    builder.setNegativeButton("Hayır", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    });

                    builder.show();
                }

                return true;
            }
        };

        RecyclerViewClickListener listener = new RecyclerViewClickListener() {
            @Override
            public void onClick(View view, int position) {
                Log.i("Pressed to position", Integer.toString(position));

                Intent intent = new Intent(getApplicationContext(), RecipeActivity.class);
                RecipeFeed temp = recipeFeedArrayList.get(position);

                String openRecipeId = temp.getRecipeId();
                String openRecipeName = temp.getRecipeName();
                String openRecipeUsername = temp.getUsername();

                Log.i("OpenRecipeName", "inside main " + openRecipeName);
                Log.i("OpenRecipeId", "inside main " + openRecipeId);

                intent.putExtra("recipeId", openRecipeId);
                intent.putExtra("username", openRecipeUsername);
                startActivity(intent);
            }
        };
        adapterFeed = new AdapterFeed(this, recipeFeedArrayList, listener, longClickListener);
        recyclerView.setAdapter(adapterFeed);

        updateFeed();

        RelativeLayout followerRelativeLayout = findViewById(R.id.followerLayout);
        RelativeLayout followingRelativeLayout = findViewById(R.id.followingLayout);

        followerRelativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ProfileActivity.this, FollowerActivity.class);
                intent.putExtra("username", username);
                startActivity(intent);
            }
        });

        followingRelativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ProfileActivity.this, FollowingActivity.class);
                intent.putExtra("username", username);
                startActivity(intent);
            }
        });

        profileFollowButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (profileFollowButton.getText().toString().equals("Takip ediliyor")) {
                    Log.i("already following", "true");
                    profileFollowButton.setText("Takip et");

                    ParseQuery<ParseObject> unfollowQuery = new ParseQuery<ParseObject>("UserInfo");
                    unfollowQuery.whereEqualTo("username", username);

                    unfollowQuery.getFirstInBackground(new GetCallback<ParseObject>() {
                        @Override
                        public void done(ParseObject object, ParseException e) {
                            if (e  == null) {
                                int followerNumber = object.getInt("followerNumber") - 1;
                                object.put("followerNumber", followerNumber);

                                String json = object.getString("followerList");
                                Gson gson = new Gson();
                                Type type = new TypeToken<ArrayList<String>>() {
                                }.getType();

                                ArrayList<String> followerList = gson.fromJson(json, type);

                                followerList.remove(ParseUser.getCurrentUser().getUsername());

                                Gson gson1 = new Gson();
                                String updatedFollowerList = gson1.toJson(followerList);
                                object.put("followerList", updatedFollowerList);

                                object.saveInBackground(new SaveCallback() {
                                    @Override
                                    public void done(ParseException e) {
                                        if (e==null) {
                                            Log.i("follower list updated", "success");
                                        }
                                    }
                                });
                            }
                        }
                    });

                    ParseQuery<ParseObject> unfollowingQuery = new ParseQuery<ParseObject>("UserInfo");
                    unfollowingQuery.whereEqualTo("username", ParseUser.getCurrentUser().getUsername());

                    unfollowingQuery.getFirstInBackground(new GetCallback<ParseObject>() {
                        @Override
                        public void done(ParseObject object, ParseException e) {
                            if (e==null) {
                                int followingNumber = object.getInt("followingNumber") - 1;
                                object.put("followingNumber", followingNumber);

                                String json = object.getString("followingList");

                                Gson gson = new Gson();
                                Type type = new TypeToken<ArrayList<String>>() {
                                }.getType();

                                ArrayList<String> followingList = gson.fromJson(json, type);

                                followingList.remove(username);

                                Gson gson1 = new Gson();
                                String updatedFollowerList = gson1.toJson(followingList);

                                object.put("followingList", updatedFollowerList);

                                object.saveInBackground(new SaveCallback() {
                                    @Override
                                    public void done(ParseException e) {
                                        if (e==null) {
                                            Log.i("following list updated", "success");
                                        }
                                    }
                                });

                            }
                        }
                    });


                } else {
                    Log.i("already following", "false");
                    profileFollowButton.setText("Takip ediliyor");

                    ParseQuery<ParseObject> followQuery = new ParseQuery<ParseObject>("UserInfo");
                    followQuery.whereEqualTo("username", username);

                    followQuery.getFirstInBackground(new GetCallback<ParseObject>() {
                        @Override
                        public void done(ParseObject object, ParseException e) {
                            if (e  == null) {
                                Log.i("follow query", "done");
                                int followerNumber = object.getInt("followerNumber") + 1;
                                object.put("followerNumber", followerNumber);

                                String json = object.getString("followerList");

                                Log.i("retrieved followerList", "test " + json);

                                Gson gson = new Gson();
                                Type type = new TypeToken<ArrayList<String>>() {
                                }.getType();

                                ArrayList<String> followerList = gson.fromJson(json, type);

                                followerList.add(ParseUser.getCurrentUser().getUsername());

                                Gson gson1 = new Gson();
                                String updatedFollowerList = gson1.toJson(followerList);

                                Log.i("updated follower list", "test " + updatedFollowerList);

                                object.put("followerList", updatedFollowerList);

                                object.saveInBackground(new SaveCallback() {
                                    @Override
                                    public void done(ParseException e) {
                                        if (e==null) {
                                            Log.i("follower list updated", "success");
                                        }
                                    }
                                });
                            } else {
                                Log.i("Error while", "updating follower number and list");
                            }
                        }
                    });

                    ParseQuery<ParseObject> followingQuery = new ParseQuery<ParseObject>("UserInfo");
                    followingQuery.whereEqualTo("username", ParseUser.getCurrentUser().getUsername());

                    followingQuery.getFirstInBackground(new GetCallback<ParseObject>() {
                        @Override
                        public void done(ParseObject object, ParseException e) {
                            if (e==null) {
                                int followingNumber = object.getInt("followingNumber") + 1;
                                object.put("followingNumber", followingNumber);

                                String json = object.getString("followingList");

                                Gson gson = new Gson();
                                Type type = new TypeToken<ArrayList<String>>() {
                                }.getType();

                                ArrayList<String> followingList = gson.fromJson(json, type);

                                followingList.add(username);

                                Gson gson1 = new Gson();
                                String updatedFollowerList = gson1.toJson(followingList);

                                object.put("followingList", updatedFollowerList);

                                object.saveInBackground(new SaveCallback() {
                                    @Override
                                    public void done(ParseException e) {
                                        if (e==null) {
                                            Log.i("following list updated", "success");
                                        }
                                    }
                                });

                            }
                        }
                    });

                }
            }
        });
    }

    private void updateFeed() {
        final ParseQuery<ParseObject> query = new ParseQuery<ParseObject>("RecipesTest");
        query.whereEqualTo("username", username);
        query.orderByDescending("createdAt");

        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, ParseException e) {

                if (e == null) {
                    if (objects.size() > 0) {
                        for (final ParseObject object : objects) {
                            ParseFile file = (ParseFile) object.get("image");
                            final String recipeName = object.getString("headline");
                            Log.i("recipe name", recipeName);
                            final String username = object.getString("username");
                            Log.i("username", username);
                            final String recipeId = object.getObjectId();

                            if (file != null) {

                                file.getDataInBackground(new GetDataCallback() {
                                    @Override
                                    public void done(byte[] data, ParseException e) {
                                        if (e == null && data != null) {

                                            Bitmap bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);

                                            RecipeFeed recipeFeed = new RecipeFeed(recipeName, username, bitmap, "default1", recipeId);
                                            recipeFeedArrayList.add(recipeFeed);

                                            adapterFeed.notifyDataSetChanged();
                                        }
                                    }
                                });
                            }
                        }
                    }
                }

            }
        });
    }
}