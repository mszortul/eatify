package com.example.eatify1506;


import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
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

import org.w3c.dom.Text;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class SearchFragment extends Fragment {

    RecyclerView recyclerView;
    ArrayList<CategoryModel> categoryModelArrayList = new ArrayList<>();
    CategoryAdapterFeed categoryAdapterFeed;

    ArrayList<RecipeFeed> recipeFeedArrayList = new ArrayList<>();
    AdapterFeed adapterFeed;
    Spinner dropdownSpinner;
    boolean downloadAgain;
    String selectedCategory = "";
    String searchString = "";

    boolean adapter_changed = false;


    // TODO: Add search feature

    public SearchFragment() {
        // Required empty public constructor
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_search, container, false);


        recyclerView = rootView.findViewById(R.id.categoryRecyclerView);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);

        categoryAdapterFeed = new CategoryAdapterFeed(getActivity(), categoryModelArrayList);
        recyclerView.setAdapter(categoryAdapterFeed);


        dropdownSpinner = rootView.findViewById(R.id.dropdownSpinner);
        dropdownSpinner.setVisibility(View.GONE);

        // final TextView currentSpinnerItem = rootView.findViewById(R.id.currentSpinnerItem);

        final String[] timeFilters = new String[]{
                "Tümü",
                "Bu ay",
                "Bu hafta",
                "Bugün"
        };

        ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, timeFilters);


        spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        dropdownSpinner.setAdapter(spinnerArrayAdapter);


        dropdownSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                //currentSpinnerItem.setText(timeFilters[position]);
                Log.i("item row", " " + position);
                Log.i("item content", " " + timeFilters[position]);


                // TODO: Needs to be filtered properly for month and week options
                switch (position) {
                    case 0:
                    case 1:
                    case 2:
                        if (downloadAgain) {
                            if (selectedCategory.isEmpty()) {
                                recipeFeedArrayList.clear();
                                // if selected category is empty, it means that we already have made a search with string so just repeat that
                                ParseQuery<ParseObject> researchQuery = new ParseQuery<ParseObject>("RecipesTest");
                                researchQuery.whereContains("ingJson", searchString);

                                researchQuery.findInBackground(new FindCallback<ParseObject>() {
                                    @Override
                                    public void done(List<ParseObject> objects, ParseException e) {
                                        if (e == null && objects != null) {
                                            if (objects.size() > 0) {
                                                for (ParseObject object : objects) {
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
                            } else {
                                // We should search by category because user hasn't used the search string yet
                                recipeFeedArrayList.clear();
                                ParseQuery<ParseObject> recategoryQuery = new ParseQuery<ParseObject>("RecipesTest");
                                recategoryQuery.whereEqualTo("category", selectedCategory);

                                recategoryQuery.findInBackground(new FindCallback<ParseObject>() {
                                    @Override
                                    public void done(List<ParseObject> objects, ParseException e) {
                                        if (e == null && objects != null) {
                                            if (objects.size() > 0) {
                                                for (ParseObject object : objects) {
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
                            downloadAgain = false;
                        }
                        break;
                    case 3:
                        downloadAgain = true;
                        for (final RecipeFeed recipe : recipeFeedArrayList) {
                            String recipeId = recipe.getRecipeId();

                            ParseQuery<ParseObject> timeQuery = new ParseQuery<ParseObject>("RecipesTest");
                            timeQuery.whereEqualTo("objectId", recipeId);

                            timeQuery.getFirstInBackground(new GetCallback<ParseObject>() {
                                @Override
                                public void done(ParseObject object, ParseException e) {
                                    if (e == null && object != null) {
                                        Date objectBorn = object.getCreatedAt();

                                        String objectAge = TimeRelated.getAge(objectBorn);

                                        Log.i("Processed object age string", " " + objectAge);
                                        if (!objectAge.substring(objectAge.length() - 2).equals("sa") && !objectAge.substring(objectAge.length() - 2).equals("dk") && !objectAge.substring(objectAge.length() - 2).equals("sn")) {
                                            recipeFeedArrayList.remove(recipe);
                                            adapterFeed.notifyDataSetChanged();
                                        }
                                    } else {
                                        Log.i("Time filter query", "error");
                                    }
                                }
                            });
                        }

                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                Log.i("Nothing selected", "exec");
            }
        });

        final AdapterFeed.RecyclerViewOnItemLongClickListener longClickListener = new AdapterFeed.RecyclerViewOnItemLongClickListener() {
            @Override
            public boolean onItemLongClicked(int position) {

                final int pos = position;

                Log.i("Long pressed", "position " + position);

                RecipeFeed temp = recipeFeedArrayList.get(position);

                String recipeOwner = temp.getUsername();
                Log.i("recipe id outer", "test " + temp.getRecipeId());

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
                                                                if (e == null) {
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


        final RecyclerViewClickListener listener = new RecyclerViewClickListener() {
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


        updateFeed();


        final EditText searchInput = rootView.findViewById(R.id.searchInput);

        searchInput.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                final int DRAWABLE_LEFT = 0;
                final int DRAWABLE_TOP = 1;
                final int DRAWABLE_RIGHT = 2;
                final int DRAWABLE_BOTTOM = 3;

                if (event.getAction() == MotionEvent.ACTION_UP) {
                    if (event.getRawX() >= (searchInput.getRight() - searchInput.getCompoundDrawables()[DRAWABLE_RIGHT].getBounds().width())) {
                        // your action here


                        Log.i("inside drawable event", "true");
                        selectedCategory = "";
                        dropdownSpinner.setVisibility(View.VISIBLE);
                        recipeFeedArrayList.clear();

                        recyclerView.setAdapter(adapterFeed);

                        searchString = searchInput.getText().toString();
                        searchInput.setText("");

                        ParseQuery<ParseObject> searchQuery = new ParseQuery<ParseObject>("RecipesTest");
                        searchQuery.whereContains("ingJson", searchString);

                        searchQuery.findInBackground(new FindCallback<ParseObject>() {
                            @Override
                            public void done(List<ParseObject> objects, ParseException e) {
                                if (e == null && objects != null) {
                                    if (objects.size() > 0) {
                                        for (ParseObject object : objects) {
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
                        return true;
                    }
                }

                return false;
            }
        });


        LocalBroadcastManager.getInstance(getActivity()).registerReceiver(messageReceiver, new IntentFilter("message"));

        return rootView;
    }


    // It filters by the category name
    public BroadcastReceiver messageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            recipeFeedArrayList.clear();
            String clickedOn = intent.getStringExtra("clickedOn");
            selectedCategory = clickedOn;
            Log.i("inside frag", "test " + clickedOn);

            dropdownSpinner.setVisibility(View.VISIBLE);


            recyclerView.setAdapter(adapterFeed);

            ParseQuery<ParseObject> categoryQuery = new ParseQuery<ParseObject>("RecipesTest");
            categoryQuery.whereEqualTo("category", clickedOn);

            categoryQuery.findInBackground(new FindCallback<ParseObject>() {
                @Override
                public void done(List<ParseObject> objects, ParseException e) {
                    if (e == null && objects != null) {
                        if (objects.size() > 0) {
                            for (ParseObject object : objects) {
                                final String recipeId = object.getObjectId();

                                boolean cont = false;

                                for (RecipeFeed item : recipeFeedArrayList) {
                                    if (recipeId.equals(item.getRecipeId())) {
                                        cont = true;
                                    }
                                }


                                if (cont) {
                                    continue;
                                }

                                ParseFile file = (ParseFile) object.get("image");
                                final String recipeName = object.getString("headline");
                                Log.i("recipe name", recipeName);
                                final String username = object.getString("username");
                                Log.i("username", username);


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


    };


    @Override
    public void onPause() {
        // Unregister since the activity is not visible
        LocalBroadcastManager.getInstance(getActivity()).unregisterReceiver(messageReceiver);
        super.onPause();
    }


    public void updateFeed() {
        categoryModelArrayList.clear();

        ArrayList<String> categoryNames = new ArrayList<>();
        ArrayList<Integer> ids = new ArrayList<>();

        categoryNames.add("Aperatif");
        ids.add(R.drawable.sand_2);
        categoryNames.add("Çorba");
        ids.add(R.drawable.soup_1);
        categoryNames.add("Diyet");
        ids.add(R.drawable.diet_3);
        categoryNames.add("Et");
        ids.add(R.drawable.chicken_1);
        categoryNames.add("Hamur işi");
        ids.add(R.drawable.patty_1);
        categoryNames.add("İçecek");
        ids.add(R.drawable.cold_drink_1);
        categoryNames.add("Kahvaltılık");
        ids.add(R.drawable.breakfast_2);
        categoryNames.add("Salata");
        ids.add(R.drawable.salad_1);
        categoryNames.add("Sandviç");
        ids.add(R.drawable.sand_3);
        categoryNames.add("Sebze");
        ids.add(R.drawable.eat_1);
        categoryNames.add("Tatlı");
        ids.add(R.drawable.sutlu_1);
        categoryNames.add("Vegan");
        ids.add(R.drawable.vegan_1);
        categoryNames.add("Vejetaryen");
        ids.add(R.drawable.vege_1);
        categoryNames.add("Diğer");
        ids.add(R.drawable.spagetti_1);


        ArrayList<String> tempNames = new ArrayList<>();
        ArrayList<Integer> tempIds = new ArrayList<>();

        for (int i = 0; i < categoryNames.size(); i++) {
            tempNames.add(categoryNames.get(i));
            tempIds.add(ids.get(i));


            if (tempNames.size() == 3 || i == categoryNames.size() - 1) {

                Gson gson = new Gson();
                Gson gson1 = new Gson();

                String json = gson.toJson(tempNames);
                String json1 = gson1.toJson(tempIds);

                CategoryModel temp = new CategoryModel(json, json1);


                categoryModelArrayList.add(temp);
                Log.i("Passed object", "test " + tempNames);

                tempNames.clear();
                tempIds.clear();

                categoryAdapterFeed.notifyDataSetChanged();
            }


        }
    }
}
