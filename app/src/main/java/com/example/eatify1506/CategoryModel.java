package com.example.eatify1506;

import java.util.ArrayList;

public class CategoryModel {

    String categoryNames, drawableIds;

    public CategoryModel(String categoryNames, String drawableIds) {
        this.categoryNames = categoryNames;
        this.drawableIds = drawableIds;
    }

    public String getCategoryNames() {
        return categoryNames;
    }

    public void setCategoryNames(String categoryNames) {
        this.categoryNames = categoryNames;
    }

    public String getDrawableIds() {
        return drawableIds;
    }

    public void setDrawableIds(String drawableIds) {
        this.drawableIds = drawableIds;
    }
}
