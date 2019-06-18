package com.example.eatify1506;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

public class Add3Activity extends AppCompatActivity {

    Bitmap bitmap;
    boolean saved = false;
    boolean photoLoaded = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add3);
    }

    public void mediaPermissionCheck(View view) {
        if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
        } else {
            selectPhoto();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        // super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == 1 && grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            // Toast.makeText(Add3Activity.this, "Read media permission granted.", Toast.LENGTH_SHORT).show();
            selectPhoto();
        } else {
            Toast.makeText(Add3Activity.this, "Read media permission denied.", Toast.LENGTH_SHORT).show();
        }

    }

    public void selectPhoto() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, 1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        // super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == RESULT_OK && data != null) {
            Uri selectedImage = data.getData();

            try {
                bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), selectedImage);
                Log.i("Recipe Photo", "Receieved");

                ImageView recipeUploadImg = findViewById(R.id.recipeUploadImg);
                recipeUploadImg.setScaleType(ImageView.ScaleType.CENTER_CROP);
                recipeUploadImg.setImageBitmap(bitmap);
                photoLoaded = true;

            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }

    public void publishRecipe(View view) {

        if (photoLoaded) {

            String headline, entry, nop, ptime, ctime, suggestion, category;

            Intent in = getIntent();
            final ArrayList<String> ingList = in.getStringArrayListExtra("ingList");
            final ArrayList<String> stepList = in.getStringArrayListExtra("stepList");


            headline = in.getStringExtra("headline");
            entry = in.getStringExtra("entry");
            nop = in.getStringExtra("nop");
            ptime = in.getStringExtra("ptime");
            ctime = in.getStringExtra("ctime");
            suggestion = in.getStringExtra("suggestion");
            category = in.getStringExtra("category");

            Log.i("headline", headline);
            Log.i("entry", entry);
            Log.i("category", category);
            Log.i("nop", nop);
            Log.i("ptime", ptime);
            Log.i("ctime", ctime);
            Log.i("suggestion", suggestion);
            Log.i("stepList", stepList.toString());
            Log.i("ingList", ingList.toString());


            final ParseObject object = new ParseObject("RecipesTest");

            if (object.getObjectId() != null) {
                Log.i("object just created", object.getObjectId());
            }

            object.put("headline", headline);
            object.put("entry", entry);
            object.put("nop", nop);
            object.put("ptime", ptime);
            object.put("ctime", ctime);
            object.put("suggestion", suggestion);
            object.put("username", ParseUser.getCurrentUser().getUsername());
            object.put("bookmarked", 0);
            object.put("category", category);


            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);

            byte[] imgByteArray = stream.toByteArray();

            ParseFile imgFile = new ParseFile("image.png", imgByteArray);
            object.put("image", imgFile);

            Gson ingGson = new Gson();
            Gson stepGson = new Gson();
            Gson gson = new Gson();

            ArrayList<String> bookmarkUsers = new ArrayList<>();

            String json = gson.toJson(bookmarkUsers);
            String ingJson = ingGson.toJson(ingList);
            String stepJson = stepGson.toJson(stepList);

            object.put("ingJson", ingJson);
            object.put("stepJson", stepJson);
            object.put("bookmarkUsers", json);

            // Upload recipe data to the server.
            object.saveInBackground(new SaveCallback() {
                @Override
                public void done(ParseException e) {

                    if (e == null) {
                        Toast.makeText(Add3Activity.this, "Tarif incelenmek üzere gönderildi.", Toast.LENGTH_SHORT).show();

                        //  This is the correct id.
                        if (object.getObjectId() != null) {
                            Log.i("recipe id", object.getObjectId());
                        }
                        Log.i("SAVED", "RECIPE");


                        ParseQuery<ParseObject> recipeNumberQuery = new ParseQuery<ParseObject>("UserInfo");
                        recipeNumberQuery.whereEqualTo("username", ParseUser.getCurrentUser().getUsername());

                        recipeNumberQuery.getFirstInBackground(new GetCallback<ParseObject>() {
                            @Override
                            public void done(ParseObject object, ParseException e) {
                                if (e == null) {
                                    int recipeNumber = object.getInt("recipeNumber") + 1;
                                    object.put("recipeNumber", recipeNumber);

                                    object.saveInBackground(new SaveCallback() {
                                        @Override
                                        public void done(ParseException e) {
                                            if (e == null) {
                                                Log.i("Recipe number updated", "success");
                                            }
                                        }
                                    });
                                }
                            }
                        });

                        Intent returnIntent = new Intent();
                        setResult(Activity.RESULT_OK, returnIntent);
                        finish();


                    } else {
                        Toast.makeText(Add3Activity.this, "Bir hata oluştu. Daha sonra tekrar deneyiniz.", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        } else {
            Toast.makeText(Add3Activity.this, "Tarifinizin fotoğrafını eklemelisiniz.", Toast.LENGTH_SHORT).show();
        }

    }
}