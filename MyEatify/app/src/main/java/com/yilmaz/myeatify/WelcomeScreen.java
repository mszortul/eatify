package com.yilmaz.myeatify;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.List;

public class WelcomeScreen extends AppCompatActivity {


    List<BookRecipes> listRecipes;

    ImageView im_1, im_2, im_3, im_4, im_5;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome_screen);

        listRecipes = new ArrayList<>();
        listRecipes.add(new BookRecipes("CHICKEN", "CATEGORIES", "DESCRIPTION", R.drawable.same_3));
        listRecipes.add(new BookRecipes("MEAT", "CATEGORIES", "DESCRIPTION", R.drawable.samet_2));
        listRecipes.add(new BookRecipes("SALAD", "CATEGORIES", "DESCRIPTION", R.drawable.eat_1));

        listRecipes.add(new BookRecipes("VEGAN", "CATEGORIES", "DESCRIPTION", R.drawable.vegan_1));
        listRecipes.add(new BookRecipes("5' oCLOCK TEA", "CATEGORIES", "DESCRIPTION", R.drawable.cookie__2));
        listRecipes.add(new BookRecipes("MAGNOLIA", "CATEGORIES", "DESCRIPTION", R.drawable.magno_2));
        listRecipes.add(new BookRecipes("CHOCOLATE CAKE", "CATEGORIES", "DESCRIPTION", R.drawable.cake_2));
        listRecipes.add(new BookRecipes("CHICKEN", "CATEGORIES", "DESCRIPTION", R.drawable.dine_2));
        listRecipes.add(new BookRecipes("PANCAKE", "CATEGORIES", "DESCRIPTION", R.drawable.breakfast_1));
        listRecipes.add(new BookRecipes("FISH", "CATEGORIES", "DESCRIPTION", R.drawable.chicken_1));
        listRecipes.add(new BookRecipes("CHOCOLATE MAGNOLIA", "CATEGORIES", "DESCRIPTION", R.drawable.magno_3));
        listRecipes.add(new BookRecipes("FISH", "CATEGORIES", "DESCRIPTION", R.drawable.fish_1));
        listRecipes.add(new BookRecipes("SALAD", "CATEGORIES", "DESCRIPTION", R.drawable.salad_2));
        listRecipes.add(new BookRecipes("PIZZA", "CATEGORIES", "DESCRIPTION", R.drawable.pizza_3));
        listRecipes.add(new BookRecipes("HOT DRINK", "CATEGORIES", "DESCRIPTION", R.drawable.hot_drink_3));
        listRecipes.add(new BookRecipes("PASTA", "CATEGORIES", "DESCRIPTION", R.drawable.pasta_3));
        listRecipes.add(new BookRecipes("PANCAKE", "CATEGORIES", "DESCRIPTION", R.drawable.pancake_3));
        listRecipes.add(new BookRecipes("HOT DRINK", "CATEGORIES", "DESCRIPTION", R.drawable.hot_drink_4));
        listRecipes.add(new BookRecipes("SALAD", "CATEGORIES", "DESCRIPTION", R.drawable.salad_4));
        listRecipes.add(new BookRecipes("PASTA", "CATEGORIES", "DESCRIPTION", R.drawable.pasta_4));

        listRecipes.add(new BookRecipes("VEGAN", "CATEGORIES", "DESCRIPTION", R.drawable.vegan_2));
        listRecipes.add(new BookRecipes("PANCAKE", "CATEGORIES", "DESCRIPTION", R.drawable.pancake_4));
        listRecipes.add(new BookRecipes("SPAGETTI", "CATEGORIES", "DESCRIPTION", R.drawable.spagetti_1));
        listRecipes.add(new BookRecipes("COLD DRINK", "CATEGORIES", "DESCRIPTION", R.drawable.cold_drink_3));
        listRecipes.add(new BookRecipes("WAFFLE", "CATEGORIES", "DESCRIPTION", R.drawable.waffle_1));
        listRecipes.add(new BookRecipes("SANDWICH", "CATEGORIES", "DESCRIPTION", R.drawable.sand_3));
        listRecipes.add(new BookRecipes("BREAKFAST", "CATEGORIES", "DESCRIPTION", R.drawable.breakfast_2));
        listRecipes.add(new BookRecipes("CAKE", "CATEGORIES", "DESCRIPTION", R.drawable.cake_3));
        listRecipes.add(new BookRecipes("FISH", "CATEGORIES", "DESCRIPTION", R.drawable.fish_2));
        listRecipes.add(new BookRecipes("5' oCLOCK TEA", "CATEGORIES", "DESCRIPTION", R.drawable.patty_2));
        listRecipes.add(new BookRecipes("CHICKEN", "CATEGORIES", "DESCRIPTION", R.drawable.chicken_2));
        listRecipes.add(new BookRecipes("MILKY DESSERT", "CATEGORIES", "DESCRIPTION", R.drawable.sutlu_2));
        listRecipes.add(new BookRecipes("BREAKFAST", "CATEGORIES", "DESCRIPTION", R.drawable.breakfast_3));
        listRecipes.add(new BookRecipes("PANCAKE", "CATEGORIES", "DESCRIPTION", R.drawable.pancake_1));
        listRecipes.add(new BookRecipes("PIZZA", "CATEGORIES", "DESCRIPTION", R.drawable.pizza_1));
        listRecipes.add(new BookRecipes("CHICKEN", "CATEGORIES", "DESCRIPTION", R.drawable.chicken_2));
        listRecipes.add(new BookRecipes("PASTA", "CATEGORIES", "DESCRIPTION", R.drawable.pasta_1));
        listRecipes.add(new BookRecipes("SANDWICH", "CATEGORIES", "DESCRIPTION", R.drawable.sand_4));
        listRecipes.add(new BookRecipes("VEGAN", "CATEGORIES", "DESCRIPTION", R.drawable.vegan_4));
        listRecipes.add(new BookRecipes("COLD DRINK", "CATEGORIES", "DESCRIPTION", R.drawable.cold_drink_2));
        listRecipes.add(new BookRecipes("FISH", "CATEGORIES", "DESCRIPTION", R.drawable.dine_2));
        listRecipes.add(new BookRecipes("PANCAKE", "CATEGORIES", "DESCRIPTION", R.drawable.pancake_2));
        listRecipes.add(new BookRecipes("PIZZA", "CATEGORIES", "DESCRIPTION", R.drawable.pizza_2));
        listRecipes.add(new BookRecipes("COOKIE", "CATEGORIES", "DESCRIPTION", R.drawable.bes_4));
        listRecipes.add(new BookRecipes("BREAKFAST", "CATEGORIES", "DESCRIPTION", R.drawable.breakfast_4));
        listRecipes.add(new BookRecipes("SPAGETTI", "CATEGORIES", "DESCRIPTION", R.drawable.spagetti_2));
        listRecipes.add(new BookRecipes("HOT DRINK", "CATEGORIES", "DESCRIPTION", R.drawable.hot_drink_1));
        listRecipes.add(new BookRecipes("SALAD", "CATEGORIES", "DESCRIPTION", R.drawable.salad_1));
        listRecipes.add(new BookRecipes("CHICKEN", "CATEGORIES", "DESCRIPTION", R.drawable.dine_2));
        listRecipes.add(new BookRecipes("VEGETARIAN", "CATEGORIES", "DESCRIPTION", R.drawable.vege_1));
        listRecipes.add(new BookRecipes("VEGAN", "CATEGORIES", "DESCRIPTION", R.drawable.vegan_3));
        listRecipes.add(new BookRecipes("SANDWICH", "CATEGORIES", "DESCRIPTION", R.drawable.sand_5));
        listRecipes.add(new BookRecipes("5' oCLOCK TEA", "CATEGORIES", "DESCRIPTION", R.drawable.bes_3));
        listRecipes.add(new BookRecipes("VEGETARIAN", "CATEGORIES", "DESCRIPTION", R.drawable.veg2_2));
        listRecipes.add(new BookRecipes("COLD DRINK", "CATEGORIES", "DESCRIPTION", R.drawable.cold_drink_1));
        listRecipes.add(new BookRecipes("SANDWICH", "CATEGORIES", "DESCRIPTION", R.drawable.sand_1));
        listRecipes.add(new BookRecipes("MILK DESSERT", "CATEGORIES", "DESCRIPTION", R.drawable.sutlu_1));
        listRecipes.add(new BookRecipes("BREAKFAST", "CATEGORIES", "DESCRIPTION", R.drawable.breakfast_6));
        listRecipes.add(new BookRecipes("COLD DRINK", "CATEGORIES", "DESCRIPTION", R.drawable.cold_drink_4));
        listRecipes.add(new BookRecipes("COLD DRINK", "CATEGORIES", "DESCRIPTION", R.drawable.bes_2));
        listRecipes.add(new BookRecipes("VEGAN", "CATEGORIES", "DESCRIPTION", R.drawable.vegan_1));
        listRecipes.add(new BookRecipes("PATTY", "CATEGORIES", "DESCRIPTION", R.drawable.patty_1));
        listRecipes.add(new BookRecipes("VEGETARIAN", "CATEGORIES", "DESCRIPTION", R.drawable.vege_4));


        listRecipes.add(new BookRecipes("PATTY", "CATEGORIES", "DESCRIPTION", R.drawable.patty_3));
        listRecipes.add(new BookRecipes("5' oCLOCK TEA", "CATEGORIES", "DESCRIPTION", R.drawable.bes_4));
        listRecipes.add(new BookRecipes("COOKIE", "CATEGORIES", "DESCRIPTION", R.drawable.bes_5));



        RecyclerView myRecyclerView=(RecyclerView)findViewById(R.id.recyclerview);
        RecyclerViewAdapterBook myAdapter=new RecyclerViewAdapterBook(this, listRecipes);
        myRecyclerView.setLayoutManager(new GridLayoutManager(this, 3));
        myRecyclerView.setAdapter(myAdapter);


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
