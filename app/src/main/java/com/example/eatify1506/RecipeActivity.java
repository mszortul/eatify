package com.example.eatify1506;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.example.eatify1506.TimeRelated;

import android.app.ActionBar;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.parse.DeleteCallback;
import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.GetDataCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import org.w3c.dom.Text;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import de.hdodenhof.circleimageview.CircleImageView;

public class RecipeActivity extends AppCompatActivity {

    ArrayList<String> ingList = new ArrayList<>();
    ArrayList<String> stepList = new ArrayList<>();
    CircleImageView currentRecipeProfileImg;

    boolean prevBookmarked = false;
    boolean nowBookmarked = false;

    int bookmarked;
    String currentRecipeId;
    String username;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe);

        Intent in = getIntent();

        final TextView currentRecipeName = findViewById(R.id.currentRecipeName);
        final TextView currentRecipeEntry = findViewById(R.id.currentRecipeEntry);
        final TextView currentRecipeCookTime = findViewById(R.id.currentRecipeCookTime);
        final TextView currentRecipePrepTime = findViewById(R.id.currentRecipePrepTime);
        final TextView currentRecipeCreatedAt = findViewById(R.id.currentRecipeCreatedAt);
        final TextView currentRecipeNop = findViewById(R.id.currentRecipeNop);
        final TextView currentRecipeSuggestions = findViewById(R.id.currentRecipeSuggestions);
        final TextView currentRecipeUsername = findViewById(R.id.currentRecipeUsername);
        final TextView currentRecipeBookmarks = findViewById(R.id.currentRecipeBookmarks);

        final ImageView reportRecipeFlag = findViewById(R.id.reportRecipeFlag);



        final TextView commentTextView = findViewById(R.id.numberOfComments);


        final ImageView currentRecipeImg = findViewById(R.id.currentRecipeImg);
        currentRecipeProfileImg = findViewById(R.id.currentRecipeProfileImg);

        final LinearLayout ingListView = findViewById(R.id.currentRecipeIngList);
        final LinearLayout stepListView = findViewById(R.id.currentRecipeStepList);

        final FrameLayout suggestionFrameLayout = findViewById(R.id.suggestionFrameLayout);


        final String currentUsername = in.getStringExtra("username");
        currentRecipeId = in.getStringExtra("recipeId");


        if (currentUsername.equals(ParseUser.getCurrentUser().getUsername())) {
            reportRecipeFlag.setVisibility(View.GONE);
        } else {
            reportRecipeFlag.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // what to when report flag is clicked
                    ParseQuery<ParseObject> alreadyReportedQuery = new ParseQuery<ParseObject>("Reports");
                    alreadyReportedQuery.whereEqualTo("recipeId", currentRecipeId);
                    alreadyReportedQuery.whereEqualTo("reportOwner", ParseUser.getCurrentUser().getUsername());

                    alreadyReportedQuery.findInBackground(new FindCallback<ParseObject>() {
                        @Override
                        public void done(List<ParseObject> objects, ParseException e) {
                            if (e == null) {
                                if (objects.size() > 0) {
                                    Toast.makeText(RecipeActivity.this, "İçeriği daha önceden rapor ettiniz.", Toast.LENGTH_SHORT).show();
                                } else {
                                    AlertDialog.Builder builder = new AlertDialog.Builder(RecipeActivity.this);
                                    builder.setTitle("İçeriği rapor etmek istediğinize emin misiniz?");

                                    // Specify the type of input expected; this, for example, sets the input as a password, and will mask the text

                                    // Set up the buttons
                                    builder.setPositiveButton("Evet", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            ParseObject reportObject = new ParseObject("Reports");
                                            reportObject.put("recipeId", currentRecipeId);
                                            reportObject.put("reportOwner", ParseUser.getCurrentUser().getUsername());
                                            reportObject.put("reportedUsername", currentUsername);

                                            reportObject.saveInBackground(new SaveCallback() {
                                                @Override
                                                public void done(ParseException e) {
                                                    if (e == null) {
                                                        Toast.makeText(RecipeActivity.this, "İçerik rapor edildi.", Toast.LENGTH_SHORT).show();
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

        ParseQuery<ParseObject> commentQuery = new ParseQuery<ParseObject>("Comments");
        commentQuery.whereEqualTo("parentRecipeId", currentRecipeId);


        commentQuery.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, ParseException e) {
                if (e == null && objects != null) {
                    int numberOfComments = objects.size();
                    commentTextView.setText(numberOfComments + " Yorum");
                }
            }
        });

        // final Bitmap currentRecipeImageBitmap = (Bitmap) in.getExtras().get("recipeImage");

        // currentRecipeProfileImg.setImageBitmap(currentRecipeImageBitmap);

        // Log.i("CurrentRecipeId", currentRecipeId);

        ParseQuery<ParseObject> query = new ParseQuery<ParseObject>("RecipesTest");
        query.whereEqualTo("objectId", currentRecipeId);

        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, ParseException e) {

                if (e == null) {
                    if (objects.size() > 0) {

                        for (ParseObject object : objects) {

                            Date born = object.getCreatedAt();

                            currentRecipeCreatedAt.setText(" " + TimeRelated.getAge(born));


                            ParseFile recipeImgFile = (ParseFile) object.get("image");
                            String headline = object.getString("headline");
                            String entry = object.getString("entry");
                            String nop = object.getString("nop");
                            String ptime = object.getString("ptime");
                            String ctime = object.getString("ctime");
                            String suggestions = object.getString("suggestion");
                            username = object.getString("username");
                            bookmarked = object.getInt("bookmarked");

                            currentRecipeName.setText(headline);
                            currentRecipeEntry.setText(entry);
                            currentRecipeNop.setText(nop);
                            currentRecipePrepTime.setText(ptime);
                            currentRecipeCookTime.setText(ctime);
                            currentRecipeSuggestions.setText(suggestions);
                            currentRecipeUsername.setText(username);



                            if (bookmarked == 0) {
                                currentRecipeBookmarks.setText("Tarif listesine ilk ekleyen sen ol!");
                            } else {
                                currentRecipeBookmarks.setText(String.format("%d kişi tarif listesine ekledi.", bookmarked));
                            }


                            String ingJson = object.getString("ingJson");
                            String stepJson = object.getString("stepJson");

                            Gson ingGson = new Gson();
                            Gson stepGson = new Gson();

                            Type ingType = new TypeToken<ArrayList<String>>() {
                            }.getType();
                            Type stepType = new TypeToken<ArrayList<String>>() {
                            }.getType();

                            ingList = ingGson.fromJson(ingJson, ingType);
                            stepList = stepGson.fromJson(stepJson, stepType);


                            for (int i = 0; i < ingList.size(); i++) {
                                String newIngredient = "- " + ingList.get(i);

                                TextView textView = new TextView(RecipeActivity.this);
                                textView.setText(newIngredient);
                                textView.setTextColor(ContextCompat.getColor(RecipeActivity.this, R.color.light_black));
                                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                                params.setMargins(10, 0, 10, 10);

                                textView.setLayoutParams(params);
                                ingListView.addView(textView);
                            }

                            for (int i = 0; i < stepList.size(); i++) {
                                String newStep = (i + 1) + ". " + stepList.get(i);

                                TextView textView = new TextView(RecipeActivity.this);
                                textView.setText(newStep);
                                textView.setTextColor(ContextCompat.getColor(RecipeActivity.this, R.color.light_black));
                                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                                params.setMargins(10, 0, 10, 10);

                                textView.setLayoutParams(params);
                                stepListView.addView(textView);
                            }


                            if (suggestions.isEmpty()) {
                                suggestionFrameLayout.setVisibility(View.GONE);
                                currentRecipeSuggestions.setVisibility(View.GONE);
                            }


                            if (recipeImgFile != null) {
                                recipeImgFile.getDataInBackground(new GetDataCallback() {
                                    @Override
                                    public void done(byte[] data, ParseException e) {

                                        if (e == null && data != null) {

                                            Bitmap bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);
                                            currentRecipeImg.setImageBitmap(bitmap);

                                            Log.i("recipe id", currentRecipeId + " downloaded.");


                                            ParseQuery<ParseObject> userQuery = new ParseQuery<ParseObject>("UserInfo");
                                            userQuery.whereEqualTo("username", ParseUser.getCurrentUser().getUsername());

                                            userQuery.getFirstInBackground(new GetCallback<ParseObject>() {
                                                @Override
                                                public void done(ParseObject object, ParseException e) {
                                                    if (e == null) {
                                                        String bookJson = object.getString("bookmarkList");
                                                        Gson bookGson = new Gson();
                                                        Type bookType = new TypeToken<ArrayList<String>>() {
                                                        }.getType();
                                                        ArrayList<String> bookmarkList = bookGson.fromJson(bookJson, bookType);

                                                        if (bookmarkList.contains(currentRecipeId)) {
                                                            currentRecipeBookmarks.setCompoundDrawablesRelativeWithIntrinsicBounds(R.drawable.ic_bookmark_orange_24dp, 0, 0, 0);
                                                            currentRecipeBookmarks.setTag("true");
                                                            prevBookmarked = true;
                                                            Log.i("bookmark already in the list", "test");
                                                        } else {
                                                            Log.i("bookmark not in the list", "test");
                                                        }
                                                    }
                                                }
                                            });


                                        }

                                    }
                                });
                            }

                            ParseQuery<ParseObject> userImageQuery = new ParseQuery<ParseObject>("UserInfo");

                            Log.i("recipe activity user image username", username);
                            userImageQuery.whereEqualTo("username", username);

                            userImageQuery.getFirstInBackground(new GetCallback<ParseObject>() {
                                @Override
                                public void done(ParseObject object, ParseException e) {
                                    if (e == null) {
                                        ParseFile file = (ParseFile) object.get("image");
                                        Log.i("image file found", "success");

                                        if (file != null) {
                                            Log.i("File is not null", "true");
                                            file.getDataInBackground(new GetDataCallback() {
                                                @Override
                                                public void done(byte[] data, ParseException e) {
                                                    if (e == null && data != null) {
                                                        Bitmap bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);
                                                        currentRecipeProfileImg.setImageBitmap(bitmap);

                                                        Log.i("User image retrieved", "success");
                                                    }
                                                }
                                            });
                                        }


                                    }
                                }
                            });


                        }

                    } else {
                        Log.i("Error", "objects.size() is 0");
                    }
                } else {
                    Log.i("Error", "e is not null");
                }

            }
        });

    }


    // Assume tag is true and image is orange if recipe is already bookmarked. (All done in onCreate)
    public void bookmarkClicked(View view) {
        TextView bookmarkTextView = (TextView) view;

        String isBookmarked = (String) bookmarkTextView.getTag();

        if (isBookmarked.equals("false")) {
            bookmarkTextView.setCompoundDrawablesRelativeWithIntrinsicBounds(R.drawable.ic_bookmark_orange_24dp, 0, 0, 0);
            bookmarkTextView.setTag("true");
            bookmarked += 1;
            bookmarkTextView.setText(String.format("%d kişi tarif listesine ekledi.", bookmarked));
        } else {
            bookmarkTextView.setCompoundDrawablesRelativeWithIntrinsicBounds(R.drawable.ic_bookmark_border_black_24dp, 0, 0, 0);
            bookmarkTextView.setTag("false");
            bookmarked -= 1;
            if (bookmarked == 0) {
                bookmarkTextView.setText("Tarif listesine ilk ekleyen sen ol!");
            } else {
                bookmarkTextView.setText(String.format("%d kişi tarif listesine ekledi.", bookmarked));
            }
        }
    }


    // We update bookmark related stuff here.
    // It does the trick, but kinda slow because all the database queries and stuff.
    @Override
    protected void onDestroy() {
        super.onDestroy();

        TextView bookmarkTextView = findViewById(R.id.currentRecipeBookmarks);

        String isBookmarked = (String) bookmarkTextView.getTag();

        if (isBookmarked == "true") {
            nowBookmarked = true;
        } else {
            nowBookmarked = false;
        }

        Log.i("prevBookmarked", String.format("test %b", prevBookmarked));
        Log.i("nowBookmarked", String.format("test %b", nowBookmarked));

        if ((prevBookmarked && !nowBookmarked) || !prevBookmarked && nowBookmarked) {
            ParseQuery<ParseObject> query = new ParseQuery<ParseObject>("RecipesTest");
            query.whereEqualTo("objectId", currentRecipeId);

            query.getFirstInBackground(new GetCallback<ParseObject>() {
                @Override
                public void done(ParseObject object, ParseException e) {
                    if (e == null) {
                        object.put("bookmarked", bookmarked);

                        object.saveInBackground(new SaveCallback() {
                            @Override
                            public void done(ParseException e) {
                                if (e == null) {
                                    Log.i("Bookmarked number", "updated");
                                }
                            }
                        });
                    }
                }
            });

            // if bookmark removed, remove it from users bookmark list too.
            if (prevBookmarked && !nowBookmarked) {
                Log.i("inner if condition prev 1 now 0", "executed");
                ParseQuery<ParseObject> userQuery = new ParseQuery<ParseObject>("UserInfo");
                userQuery.whereEqualTo("username", ParseUser.getCurrentUser().getUsername());

                // This process for the user who removed it from users own bookmarks.
                userQuery.getFirstInBackground(new GetCallback<ParseObject>() {
                    @Override
                    public void done(ParseObject object, ParseException e) {
                        if (e == null) {
                            String bookJson = object.getString("bookmarkList");
                            Gson bookGson = new Gson();
                            Type bookType = new TypeToken<ArrayList<String>>() {
                            }.getType();
                            ArrayList<String> bookmarkList = bookGson.fromJson(bookJson, bookType);

                            Log.i("Bookmark list len(before remove)", "test  " + bookmarkList.size());
                            bookmarkList.remove(currentRecipeId);
                            Log.i("Bookmark list len(before remove)", "test  " + bookmarkList.size());

                            Gson saveGson = new Gson();
                            String saveJson = saveGson.toJson(bookmarkList);

                            object.put("bookmarkList", saveJson);

                            object.saveInBackground(new SaveCallback() {
                                @Override
                                public void done(ParseException e) {
                                    if (e == null) {
                                        Log.i("Recipe id removed from users list", "success");
                                        Log.i("Deleted recipe id is", currentRecipeId);
                                    }
                                }
                            });

                        }
                    }
                });

                // This is for recipe itself. We will remove current users name from recipes list.
                ParseQuery<ParseObject> recipeQuery = new ParseQuery<ParseObject>("RecipesTest");
                recipeQuery.whereEqualTo("objectId", currentRecipeId);

                recipeQuery.getFirstInBackground(new GetCallback<ParseObject>() {
                    @Override
                    public void done(ParseObject object, ParseException e) {
                        if (e == null) {
                            String json = object.getString("bookmarkUsers");
                            Gson gson = new Gson();
                            Type type = new TypeToken<ArrayList<String>>() {
                            }.getType();
                            ArrayList<String> bookmarkUsers = gson.fromJson(json, type);

                            bookmarkUsers.remove(ParseUser.getCurrentUser().getUsername());

                            Gson gson1 = new Gson();
                            String json1 = gson1.toJson(bookmarkUsers);
                            object.put("bookmarkUsers", json1);
                            object.saveInBackground(new SaveCallback() {
                                @Override
                                public void done(ParseException e) {
                                    if (e == null) {
                                        Log.i("Current username removed from recipes list", "success");
                                    }
                                }
                            });
                        }
                    }
                });

            } else {
                Log.i("inner if condition prev 0 now 1", "executed");
                // Bookmark wasnt in the users list, it should be added now
                ParseQuery<ParseObject> userQuery = new ParseQuery<ParseObject>("UserInfo");
                userQuery.whereEqualTo("username", ParseUser.getCurrentUser().getUsername());

                // This adds recipe id to users bookmarks
                userQuery.getFirstInBackground(new GetCallback<ParseObject>() {
                    @Override
                    public void done(ParseObject object, ParseException e) {
                        if (e == null) {
                            String bookJson = object.getString("bookmarkList");
                            Gson bookGson = new Gson();
                            Type bookType = new TypeToken<ArrayList<String>>() {
                            }.getType();
                            ArrayList<String> bookmarkList = bookGson.fromJson(bookJson, bookType);

                            Log.i("Bookmark list len(before add)", "test  " + bookmarkList.size());
                            bookmarkList.add(currentRecipeId);
                            Log.i("Bookmark list len(after add)", "test  " + bookmarkList.size());

                            Gson saveGson = new Gson();
                            String saveJson = saveGson.toJson(bookmarkList);

                            object.put("bookmarkList", saveJson);

                            object.saveInBackground(new SaveCallback() {
                                @Override
                                public void done(ParseException e) {
                                    if (e == null) {
                                        Log.i("Recipe id added to users list", "success");
                                        Log.i("Added recipe id is", currentRecipeId);
                                    }
                                }
                            });

                        }
                    }
                });

                // This adds username to recipes list
                ParseQuery<ParseObject> recipeQuery = new ParseQuery<ParseObject>("RecipesTest");
                recipeQuery.whereEqualTo("objectId", currentRecipeId);

                recipeQuery.getFirstInBackground(new GetCallback<ParseObject>() {
                    @Override
                    public void done(ParseObject object, ParseException e) {
                        if (e == null) {
                            String json = object.getString("bookmarkUsers");
                            Gson gson = new Gson();
                            Type type = new TypeToken<ArrayList<String>>() {
                            }.getType();
                            ArrayList<String> bookmarkUsers = gson.fromJson(json, type);

                            bookmarkUsers.add(ParseUser.getCurrentUser().getUsername());

                            Gson gson1 = new Gson();
                            String json1 = gson1.toJson(bookmarkUsers);
                            object.put("bookmarkUsers", json1);
                            object.saveInBackground(new SaveCallback() {
                                @Override
                                public void done(ParseException e) {
                                    if (e == null) {
                                        Log.i("Current username added to recipes list", "success");
                                    }
                                }
                            });
                        }
                    }
                });
            }

        }

    }


    public void profileClicked(View view) {
        Intent intent = new Intent(getApplicationContext(), ProfileActivity.class);
        intent.putExtra("username", username);
        startActivity(intent);
    }

    public void openComments(View view) {
        Intent intent = new Intent(RecipeActivity.this, CommentActivity.class);
        intent.putExtra("recipeId", currentRecipeId);
        startActivity(intent);
    }

}