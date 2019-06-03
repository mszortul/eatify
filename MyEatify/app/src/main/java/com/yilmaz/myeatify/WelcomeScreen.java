package com.yilmaz.myeatify;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

public class WelcomeScreen extends AppCompatActivity {


    ImageView im_1, im_2, im_3, im_4, im_5;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome_screen);

        im_1 = (ImageView) findViewById(R.id.im_1);
        im_1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openHomePage();
            }
        });


        im_2 = (ImageView) findViewById(R.id.im_2);
        im_2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openSearchPage();
            }
        });



        im_3 = (ImageView) findViewById(R.id.im_3);
        im_3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openAddPage();
            }
        });




        im_4 = (ImageView) findViewById(R.id.im_4);
        im_4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openLikePage();
            }
        });




        im_5 = (ImageView) findViewById(R.id.im_5);
        im_5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openPersonPage();
            }
        });



    }


    public void openHomePage(){
        Intent intent = new Intent(this, HomePage.class);
        startActivity(intent);
    }


    public void openSearchPage(){
        Intent intent = new Intent(this, SearchPage.class);
        startActivity(intent);
    }


    public void openAddPage(){
        Intent intent = new Intent(this, AddPage.class);
        startActivity(intent);
    }


    public void openLikePage(){
        Intent intent = new Intent(this, LikePage.class);
        startActivity(intent);
    }


    public void openPersonPage(){
        Intent intent = new Intent(this, PersonPage.class);
        startActivity(intent);
    }
}
