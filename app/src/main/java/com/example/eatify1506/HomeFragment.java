package com.example.eatify1506;


import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.parse.DeleteCallback;
import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.GetDataCallback;
import com.parse.ParseAnalytics;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends Fragment {

    RecyclerView recyclerView;
    ArrayList<RecipeFeed> recipeFeedArrayList = new ArrayList<>();
    AdapterFeed adapterFeed;
    SwipeRefreshLayout swipeRefreshLayout;

    public HomeFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View rootView = inflater.inflate(R.layout.fragment_home, container, false);

        swipeRefreshLayout = rootView.findViewById(R.id.swipeRefreshLayout);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                updateFeed();
            }
        });


        recyclerView = rootView.findViewById(R.id.recyclerView);

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
                // Bitmap openRecipeImageBitmap = temp.getRecipeImg();

                Log.i("OpenRecipeName", "inside main " + openRecipeName);
                Log.i("OpenRecipeId", "inside main " + openRecipeId);

                // intent.putExtra("recipeImage", openRecipeImageBitmap);
                intent.putExtra("recipeId", openRecipeId);
                intent.putExtra("username", openRecipeUsername);
                startActivity(intent);
            }
        };
        adapterFeed = new AdapterFeed(getActivity(), recipeFeedArrayList, listener, longClickListener);
        recyclerView.setAdapter(adapterFeed);

        updateFeed();

        return rootView;
    }

    public void updateFeed() {
        recipeFeedArrayList.clear();
        final ParseQuery<ParseObject> query = new ParseQuery<ParseObject>("RecipesTest");
        query.whereNotEqualTo("username", "fren");
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

        swipeRefreshLayout.setRefreshing(false);
    }

}
