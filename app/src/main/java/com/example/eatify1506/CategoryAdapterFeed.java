package com.example.eatify1506;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.lang.reflect.Type;
import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class CategoryAdapterFeed extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    Context context;
    ArrayList<CategoryModel> categoryModelArrayList;
    String clickedOn;

    public CategoryAdapterFeed(Context context, ArrayList<CategoryModel> categoryModelArrayList) {
        this.context = context;
        this.categoryModelArrayList = categoryModelArrayList;

    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.category_row, parent, false);
        CategoryRowViewHolder rowViewHolder = new CategoryRowViewHolder(view);

        return rowViewHolder;
    }

    private Bitmap compressBitmap(int resource_id) {

        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inSampleSize = 4;
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), resource_id, options);
        bitmap.compress(Bitmap.CompressFormat.JPEG, 50, stream);

        byte[] byteArray = stream.toByteArray();

        return BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);
    }


    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        final CategoryModel categoryModel = categoryModelArrayList.get(position);

        Log.i("names", "test" + categoryModel.categoryNames);

        if (holder instanceof CategoryRowViewHolder) {
            Log.i("onBind", "instanceof inside");
            final CategoryRowViewHolder rowViewHolder = (CategoryRowViewHolder) holder;

            Gson gson = new Gson();
            Gson gson1 = new Gson();

            String json = categoryModel.getCategoryNames();
            String json1 = categoryModel.getDrawableIds();

            Type type = new TypeToken<ArrayList<String>>() {
            }.getType();
            Type type1 = new TypeToken<ArrayList<Integer>>() {
            }.getType();

            final ArrayList<String> names = gson.fromJson(json, type);
            final ArrayList<Integer> ids = gson1.fromJson(json1, type1);

            Log.i("after conversion", "test " + names);

            rowViewHolder.layout1.setOnClickListener(new View.OnClickListener() {
                // TODO: make query for category name same for layout 2 and 3
                @Override
                public void onClick(View v) {
                    String categoryName = names.get(0);
                    Log.i("clicked on", " " + categoryName);
                    clickedOn = categoryName;

                    Intent intent = new Intent("message");
                    intent.putExtra("clickedOn", clickedOn);
                    LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
                }
            });

            rowViewHolder.layout2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String categoryName = names.get(1);
                    Log.i("clicked on", " " + categoryName);
                    clickedOn = categoryName;
                    Intent intent = new Intent("message");
                    intent.putExtra("clickedOn", clickedOn);
                    LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
                }
            });
            rowViewHolder.layout3.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String categoryName = names.get(2);
                    Log.i("clicked on", " " + categoryName);
                    clickedOn = categoryName;
                    Intent intent = new Intent("message");
                    intent.putExtra("clickedOn", clickedOn);
                    LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
                }
            });

            int numberOfItems = names.size();

            Bitmap comp1, comp2, comp3;

            LinearLayout.LayoutParams params;

            switch (numberOfItems) {
                case 0:
                    Log.i("case", "0");
                    rowViewHolder.layout1.setVisibility(View.GONE);
                    rowViewHolder.layout2.setVisibility(View.GONE);
                    rowViewHolder.layout3.setVisibility(View.GONE);
                    break;
                case 1:
                    Log.i("case", "1");
                    rowViewHolder.text1.setText(names.get(0));

                    rowViewHolder.image1.setImageBitmap(compressBitmap(ids.get(0)));

                    params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, 60);
                    rowViewHolder.layout1.setLayoutParams(params);

                    rowViewHolder.layout2.setVisibility(View.GONE);
                    rowViewHolder.layout3.setVisibility(View.GONE);

                    break;
                case 2:
                    Log.i("case", "2");

                    rowViewHolder.text1.setText(names.get(0));
                    rowViewHolder.text2.setText(names.get(1));

                    rowViewHolder.image1.setImageBitmap(compressBitmap(ids.get(0)));
                    rowViewHolder.image2.setImageBitmap(compressBitmap(ids.get(1)));

                    params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, 30);


                    rowViewHolder.layout1.setLayoutParams(params);
                    rowViewHolder.layout2.setLayoutParams(params);
                    rowViewHolder.layout1.setOrientation(LinearLayout.VERTICAL);
                    rowViewHolder.layout2.setOrientation(LinearLayout.VERTICAL);


                    //Picasso.get().load(ids.get(0)).into(rowViewHolder.image1);

                    //rowViewHolder.image1.setImageResource(ids.get(0));
                    //Picasso.get().load(ids.get(1)).into(rowViewHolder.image2);
                    //rowViewHolder.image2.setImageResource(ids.get(1));


                    rowViewHolder.layout3.setVisibility(View.GONE);

                    break;
                case 3:
                    Log.i("case", "3");
                    rowViewHolder.text1.setText(names.get(0));
                    rowViewHolder.text2.setText(names.get(1));
                    rowViewHolder.text3.setText(names.get(2));

                    rowViewHolder.image1.setImageBitmap(compressBitmap(ids.get(0)));
                    rowViewHolder.image2.setImageBitmap(compressBitmap(ids.get(1)));
                    rowViewHolder.image3.setImageBitmap(compressBitmap(ids.get(2)));

                    //Picasso.get().load(ids.get(0)).into(rowViewHolder.image1);
                    //rowViewHolder.image1.setImageResource(ids.get(0));
                    //Picasso.get().load(ids.get(1)).into(rowViewHolder.image2);
                    //rowViewHolder.image2.setImageResource(ids.get(1));
                    //Picasso.get().load(ids.get(2)).into(rowViewHolder.image3);
                    //rowViewHolder.image3.setImageResource(ids.get(2));

                    break;

            }
        }

    }

    @Override
    public int getItemCount() {
        return categoryModelArrayList.size();
    }

    public class CategoryRowViewHolder extends RecyclerView.ViewHolder {

        TextView text1, text2, text3;
        CircleImageView image1, image2, image3;
        LinearLayout layout1, layout2, layout3;

        public CategoryRowViewHolder(@NonNull View itemView) {
            super(itemView);
            layout1 = (LinearLayout) itemView.findViewById(R.id.category1);
            layout2 = (LinearLayout) itemView.findViewById(R.id.category2);
            layout3 = (LinearLayout) itemView.findViewById(R.id.category3);
            text1 = (TextView) itemView.findViewById(R.id.category1_text);
            text2 = (TextView) itemView.findViewById(R.id.category2_text);
            text3 = (TextView) itemView.findViewById(R.id.category3_text);
            image1 = (CircleImageView) itemView.findViewById(R.id.category1_image);
            image2 = (CircleImageView) itemView.findViewById(R.id.category2_image);
            image3 = (CircleImageView) itemView.findViewById(R.id.category3_image);

        }

    }
}
