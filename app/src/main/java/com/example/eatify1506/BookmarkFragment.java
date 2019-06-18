package com.example.eatify1506;


import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.GetDataCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class BookmarkFragment extends Fragment {

    RecyclerView recyclerView;
    ArrayList<RecipeFeed> recipeFeedArrayList = new ArrayList<>();
    AdapterFeed adapterFeed;

    public BookmarkFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View rootView = inflater.inflate(R.layout.fragment_bookmark, container, false);

        recyclerView = rootView.findViewById(R.id.bookmarkRecyclerView);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);


        AdapterFeed.RecyclerViewOnItemLongClickListener longClickListener = new AdapterFeed.RecyclerViewOnItemLongClickListener() {
            @Override
            public boolean onItemLongClicked(int position) {
                Log.i("item clicked", "position " + position);

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

        final ParseQuery<ParseObject> query = new ParseQuery<ParseObject>("UserInfo");
        query.whereEqualTo("username", ParseUser.getCurrentUser().getUsername());

        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, ParseException e) {

                if (e == null) {
                    if (objects.size() > 0) {
                        for (final ParseObject object : objects) {

                            String json = object.getString("bookmarkList");

                            Gson gson = new Gson();

                            Type type = new TypeToken<ArrayList<String>>() {
                            }.getType();

                            ArrayList<String> bookmarkList = gson.fromJson(json, type);

                            for (String recipeId : bookmarkList) {

                                ParseQuery<ParseObject> recipeQuery = new ParseQuery<ParseObject>("RecipesTest");
                                recipeQuery.whereEqualTo("objectId", recipeId);

                                recipeQuery.getFirstInBackground(new GetCallback<ParseObject>() {
                                    @Override
                                    public void done(ParseObject object, ParseException e) {
                                        if (e == null) {
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
                                });

                            }


                        }
                    }
                }

            }
        });

    }
}
