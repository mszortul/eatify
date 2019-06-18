package com.example.eatify1506;
import android.graphics.Bitmap;
import android.widget.TableLayout;
import android.widget.Toolbar;

import com.google.android.material.tabs.TabLayout;

public class RecipeFeed {

    String recipeName, username, userImg, recipeId;
    Bitmap recipeImg;



    public RecipeFeed(String recipeName, String username, Bitmap recipeImg, String userImg, String recipeId) {
        this.recipeName = recipeName;
        this.username = username;
        this.recipeImg = recipeImg;
        this.userImg = userImg;
        this.recipeId = recipeId;
    }



    public String getRecipeName() {
        return recipeName;
    }

    public void setRecipeName(String recipeName) {
        this.recipeName = recipeName;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Bitmap getRecipeImg() {
        return recipeImg;
    }

    public void setRecipeImg(Bitmap recipeImg) {
        this.recipeImg = recipeImg;
    }

    public String getUserImg() {
        return userImg;
    }

    public void setUserImg(String userImg) {
        this.userImg = userImg;
    }

    public String getRecipeId() {
        return recipeId;
    }

    public void setRecipeId(String recipeId) {
        this.recipeId = recipeId;
    }
}
