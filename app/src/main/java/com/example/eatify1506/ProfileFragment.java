package com.example.eatify1506;


import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
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

import static android.app.Activity.RESULT_OK;


/**
 * A simple {@link Fragment} subclass.
 */
public class ProfileFragment extends Fragment {


    String username;

    Bitmap userImageChangeBitmap;
    CircleImageView profileUserImage;

    RecyclerView recyclerView;
    ArrayList<RecipeFeed> recipeFeedArrayList = new ArrayList<>();
    AdapterFeed adapterFeed;

    public ProfileFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View rootView = inflater.inflate(R.layout.fragment_profile, container, false);


        Button profileFollowButton = rootView.findViewById(R.id.profileFollowButton);
        profileUserImage = rootView.findViewById(R.id.fragmentProfileUserImage);

        final TextView profileFollowerNumber = rootView.findViewById(R.id.profileFollowerNumber);
        final TextView profileFollowingNumber = rootView.findViewById(R.id.profileFollowingNumber);
        final TextView profileRecipeNumber = rootView.findViewById(R.id.profileRecipeNumber);
        final TextView profileUsername = rootView.findViewById(R.id.profileUsername);

        username = ParseUser.getCurrentUser().getUsername();


        profileUsername.setText(username);

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

        recyclerView = rootView.findViewById(R.id.profileRecyclerView);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
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
                    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
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

                Intent intent = new Intent(getActivity(), RecipeActivity.class);
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
        adapterFeed = new AdapterFeed(getActivity(), recipeFeedArrayList, listener, longClickListener);
        recyclerView.setAdapter(adapterFeed);

        updateFeed();


        profileUserImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Log.i("mediaPermissionCheck", "executed");

                Log.i("global var username", "test " + username);
                Log.i("currentUsername", "test " + ParseUser.getCurrentUser().getUsername());

                if (username.equals(ParseUser.getCurrentUser().getUsername())) {
                    Log.i("is users own profile", "true");
                    if (getActivity().checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                        requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
                    } else {
                        selectPhoto();
                    }
                }

            }
        });

        RelativeLayout followerRelativeLayout = rootView.findViewById(R.id.followerRelativeLayout);
        RelativeLayout followingRelativeLayout = rootView.findViewById(R.id.followingRelativeLayout);

        followerRelativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), FollowerActivity.class);
                intent.putExtra("username", username);
                startActivity(intent);
            }
        });

        followingRelativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), FollowingActivity.class);
                intent.putExtra("username", username);
                startActivity(intent);
            }
        });

        return rootView;
    }

    @Override
    public void onRequestPermissionsResult ( int requestCode,
                                             @NonNull String[] permissions, @NonNull int[] grantResults){
        // super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == 1 && grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            // Toast.makeText(Add3Activity.this, "Read media permission granted.", Toast.LENGTH_SHORT).show();
            selectPhoto();
        } else {
            Toast.makeText(getActivity(), "Read media permission denied.", Toast.LENGTH_SHORT).show();
        }
    }

    public void selectPhoto () {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, 1);
    }


    @Override
    public void onActivityResult ( int requestCode, int resultCode,
                                   @Nullable Intent data){
        // super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == RESULT_OK && data != null) {
            Uri selectedImage = data.getData();

            try {
                userImageChangeBitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), selectedImage);
                Log.i("User Photo", "Receieved");

                profileUserImage.setImageBitmap(userImageChangeBitmap);

                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                userImageChangeBitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);

                byte[] userImageByteArray = stream.toByteArray();

                final ParseFile userImageFile = new ParseFile("image.png", userImageByteArray);

                ParseQuery<ParseObject> userInfoQuery = new ParseQuery<ParseObject>("UserInfo");
                userInfoQuery.whereEqualTo("username", username);

                userInfoQuery.getFirstInBackground(new GetCallback<ParseObject>() {
                    @Override
                    public void done(ParseObject object, ParseException e) {
                        if (e == null) {
                            object.put("image", userImageFile);
                            object.saveInBackground(new SaveCallback() {
                                @Override
                                public void done(ParseException e) {

                                    if (e == null) {
                                        Log.i("Update user photo", "Done!");
                                    } else {
                                        Log.i("Update user photo", "Error.");
                                    }
                                }
                            });
                        }
                    }
                });

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void updateFeed() {
        recipeFeedArrayList.clear();
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
