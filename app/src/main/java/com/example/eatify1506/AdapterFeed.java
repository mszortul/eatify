package com.example.eatify1506;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.inputmethodservice.Keyboard;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.GetDataCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.sql.Time;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class AdapterFeed extends RecyclerView.Adapter<RecyclerView.ViewHolder> {


    Context context;
    ArrayList<RecipeFeed> recipeFeedArrayList;
    RecyclerViewClickListener recyclerViewClickListener;
    RecyclerViewOnItemLongClickListener recyclerViewOnItemLongClickListener;

    public interface RecyclerViewOnItemLongClickListener {
        public boolean onItemLongClicked(int position);
    }

    AdapterFeed(Context context, ArrayList<RecipeFeed> recipeFeedArrayList, RecyclerViewClickListener listener, RecyclerViewOnItemLongClickListener listenerLong) {

        this.context = context;
        this.recipeFeedArrayList = recipeFeedArrayList;
        this.recyclerViewClickListener = listener;
        this.recyclerViewOnItemLongClickListener = listenerLong;
    }


    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_feed, parent, false);
        RowViewHolder rowViewHolder = new RowViewHolder(view, recyclerViewClickListener, recyclerViewOnItemLongClickListener);

        return rowViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, final int position) {
        final RecipeFeed recipeFeed = recipeFeedArrayList.get(position);

        if (holder instanceof RowViewHolder) {
            final RowViewHolder rowViewHolder = (RowViewHolder) holder;
            rowViewHolder.username.setText(recipeFeed.getUsername());
            rowViewHolder.recipeName.setText(recipeFeed.getRecipeName());
            rowViewHolder.recipeImg.setImageBitmap(recipeFeed.getRecipeImg());

            String recipeId = recipeFeed.getRecipeId();

            ParseQuery<ParseObject> commentNumberQuery = new ParseQuery<ParseObject>("Comments");
            commentNumberQuery.whereEqualTo("parentRecipeId", recipeId);

            commentNumberQuery.findInBackground(new FindCallback<ParseObject>() {
                @Override
                public void done(List<ParseObject> objects, ParseException e) {
                    if (e == null && objects != null) {
                        int commentNum = objects.size();

                        rowViewHolder.commentNumber.setText(" " + commentNum);
                    } else {
                        rowViewHolder.commentNumber.setText(" 0");
                    }
                }
            });


            ParseQuery<ParseObject> bookmarkNumberQuery = new ParseQuery<ParseObject>("RecipesTest");
            bookmarkNumberQuery.whereEqualTo("objectId", recipeId);

            bookmarkNumberQuery.getFirstInBackground(new GetCallback<ParseObject>() {
                @Override
                public void done(ParseObject object, ParseException e) {
                    if (e == null) {
                        Date born = object.getCreatedAt();

                        rowViewHolder.recipeTime.setText(" " + TimeRelated.getAge(born));

                        int bookmarkNum = object.getInt("bookmarked");

                        rowViewHolder.bookmarkNumber.setText("" + bookmarkNum);
                    } else {
                        rowViewHolder.bookmarkNumber.setText("0");
                    }
                }
            });

            // TODO: Time server query is missing, use TimeRelated.getAge()




            if (recipeFeed.getUserImg() == "default") {
                rowViewHolder.userImg.setImageResource(R.drawable.test_name);
            } else {
                ParseQuery<ParseObject> query = new ParseQuery<ParseObject>("UserInfo");
                query.whereEqualTo("username", recipeFeed.getUsername());

                query.findInBackground(new FindCallback<ParseObject>() {
                    @Override
                    public void done(List<ParseObject> objects, ParseException e) {
                        if (e == null) {
                            if (objects.size() > 0) {
                                Log.i("Inside adapter feed", "user image query result length is " + objects.size());
                                for (ParseObject object : objects) {
                                    ParseFile file = (ParseFile) object.get("image");
                                    if (file != null) {
                                        file.getDataInBackground(new GetDataCallback() {
                                            @Override
                                            public void done(byte[] data, ParseException e) {
                                                if (e == null && data != null) {
                                                    Bitmap bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);
                                                    rowViewHolder.userImg.setImageBitmap(bitmap);
                                                }
                                            }
                                        });
                                    } else {
                                        rowViewHolder.userImg.setImageResource(R.drawable.profile_img_download);
                                    }
                                }
                            }
                        }
                    }
                });
            }

            rowViewHolder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    recyclerViewOnItemLongClickListener.onItemLongClicked(position);
                    return true;
                }
            });


        }

    }

    @Override
    public int getItemCount() {
        return recipeFeedArrayList.size();
    }

    public class RowViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView username, recipeName, recipeTime, commentNumber, bookmarkNumber;
        ImageView recipeImg;
        CircleImageView userImg;
        private RecyclerViewClickListener mListener;
        private RecyclerViewOnItemLongClickListener longListener;

        public RowViewHolder(@NonNull View itemView, RecyclerViewClickListener listener, RecyclerViewOnItemLongClickListener lListener) {
            super(itemView);

            longListener = lListener;
            mListener = listener;
            username = (TextView) itemView.findViewById(R.id.username);
            recipeName = (TextView) itemView.findViewById(R.id.recipeName);
            userImg = (CircleImageView) itemView.findViewById(R.id.userImg);
            recipeImg = (ImageView) itemView.findViewById(R.id.recipeImg);
            recipeTime = (TextView) itemView.findViewById(R.id.recipeTimeRowFeed);
            commentNumber = (TextView) itemView.findViewById(R.id.commentNumberRowFeed);
            bookmarkNumber = (TextView) itemView.findViewById(R.id.bookmarkNumberRowFeed);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            mListener.onClick(view, getAdapterPosition());
        }
    }
}
