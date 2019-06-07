package com.yilmaz.myeatify;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Locale;

public class RecipeBookActivity extends AppCompatActivity {


    private TextView tv_title, tv_description;
    private ImageView img;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_book);


        tv_title=(TextView)findViewById(R.id.tv_recipe_name);
        tv_description=(TextView)findViewById(R.id.tv_description);
        img=(ImageView)findViewById(R.id.image_book_thumbnail);


        //receive the data
        Intent intent=getIntent();

        String Title =intent.getExtras().getString("Recipe Name");
        String Description =intent.getExtras().getString("Description");
        String Category=intent.getExtras().getString("Category");
        int Image=intent.getExtras().getInt("Thumbnail");



        tv_title.setText(Title);
        tv_description.setText(Description);
        img.setImageResource(Image);



    }
}
