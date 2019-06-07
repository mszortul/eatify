package com.yilmaz.myeatify;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

public class RecyclerViewAdapterBook extends RecyclerView.Adapter<RecyclerViewAdapterBook.myViewHolder> {


    private Context mContext;
    private List<BookRecipes> mRecipes;

    public RecyclerViewAdapterBook(Context mContext, List<BookRecipes> mRecipes) {
        this.mContext = mContext;
        this.mRecipes = mRecipes;
    }

    @Override
    public myViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;
        LayoutInflater mInflater=LayoutInflater.from(mContext);
        view = mInflater.inflate(R.layout.cardview_item_book, parent, false);
        return new myViewHolder(view);
    }

    @Override
    public void onBindViewHolder(myViewHolder holder, final int position) {

        holder.textView_book_title.setText(mRecipes.get(position).getTitle());
        holder.imageView_book_thumbnail.setImageResource(mRecipes.get(position).getThumbnail());
        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                Intent intent=new Intent(mContext, RecipeBookActivity.class);

                //pass data to the book activity
                intent.putExtra("Recipe Name", mRecipes.get(position).getTitle());
                intent.putExtra("Description", mRecipes.get(position).getDescription());
                intent.putExtra("Thumbnail", mRecipes.get(position).getThumbnail());

                //start the activity
                mContext.startActivity(intent);

            }
        });

        //set click listener



    }

    @Override
    public int getItemCount() {
        return mRecipes.size();
    }

    public static class myViewHolder extends RecyclerView.ViewHolder{

        TextView textView_book_title;
        ImageView imageView_book_thumbnail;
        CardView cardView;

        public myViewHolder(View itemView){
            super(itemView);


            textView_book_title=(TextView) itemView.findViewById(R.id.book_title);
            imageView_book_thumbnail=(ImageView) itemView.findViewById(R.id.book_image);
            cardView=(CardView) itemView.findViewById(R.id.cardview_1);
        }
    }




}
