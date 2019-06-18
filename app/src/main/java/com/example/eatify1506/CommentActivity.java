package com.example.eatify1506;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextClock;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.parse.DeleteCallback;
import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

public class CommentActivity extends AppCompatActivity {

    LinearLayout commentLayout;
    boolean commentMade = false;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comment);


        Intent in = getIntent();
        final String parentRecipeId = in.getStringExtra("recipeId");

        setTitle(parentRecipeId);

        commentLayout = findViewById(R.id.commentLayout);


        final EditText commentInput = findViewById(R.id.commentEditText);


        ParseQuery<ParseObject> commentQuery = new ParseQuery<ParseObject>("Comments");
        commentQuery.whereEqualTo("parentRecipeId", parentRecipeId);
        commentQuery.orderByAscending("createdAt");


        commentQuery.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, ParseException e) {
                if (e == null) {
                    if (objects.size() > 0) {
                        Log.i("Number of comments", "test " + objects.size());

                        for (ParseObject object : objects) {
                            String owner = object.getString("username");
                            String content = object.getString("content");
                            String commentId = object.getObjectId();
                            Date date = object.getCreatedAt();
                            String ago = TimeRelated.getAge(date);
                            addComment(owner, content, commentId, ago);
                        }

                    }
                }
            }
        });

        commentInput.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    hideKeyboard(v);
                }
            }
        });


        commentInput.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                final int DRAWABLE_LEFT = 0;
                final int DRAWABLE_TOP = 1;
                final int DRAWABLE_RIGHT = 2;
                final int DRAWABLE_BOTTOM = 3;

                if (event.getAction() == MotionEvent.ACTION_UP) {
                    if (event.getRawX() >= (commentInput.getRight() - commentInput.getCompoundDrawables()[DRAWABLE_RIGHT].getBounds().width())) {
                        // your action here

                        String content = commentInput.getText().toString();
                        addComment(ParseUser.getCurrentUser().getUsername(), content, "", "0s");

                        commentInput.setText("");
                        commentInput.clearFocus();
                        hideSoftKeyboard(CommentActivity.this);
                        // arrayAdapter.notifyDataSetChanged();

                        ParseObject commentEntry = new ParseObject("Comments");

                        commentEntry.put("username", ParseUser.getCurrentUser().getUsername());
                        commentEntry.put("content", content);
                        commentEntry.put("parentRecipeId", parentRecipeId);

                        commentEntry.saveInBackground(new SaveCallback() {
                            @Override
                            public void done(ParseException e) {
                                if (e == null) {
                                    Log.i("Comment saved to server", "success");
                                }
                            }
                        });

                        return true;
                    }
                }

                return false;
            }
        });


    }

    public static void hideSoftKeyboard(Activity activity) {
        InputMethodManager inputMethodManager =
                (InputMethodManager) activity.getSystemService(
                        Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(
                activity.getCurrentFocus().getWindowToken(), 0);
    }


    public void hideKeyboard(View v) {
        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(v.getWindowToken(), 0);

    }

    public void addComment(final String uname, final String content, final String commentId, String ago) {

        final LinearLayout rowLinearLayout = new LinearLayout(CommentActivity.this);
        LinearLayout.LayoutParams paramsRow = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT, 100);
        rowLinearLayout.setOrientation(LinearLayout.VERTICAL);
        rowLinearLayout.setBackgroundColor(getColor(R.color.rowBackground));
        paramsRow.setMargins(60, 20, 60, 60);
        rowLinearLayout.setLayoutParams(paramsRow);
        rowLinearLayout.setTag(commentId);

        LinearLayout username_ago = new LinearLayout(CommentActivity.this);
        LinearLayout.LayoutParams username_ago_params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT, 20);
        username_ago.setOrientation(LinearLayout.HORIZONTAL);
        username_ago_params.setMargins(40, 40, 40, 10);
        username_ago.setLayoutParams(username_ago_params);


        TextView agoTextView = new TextView(CommentActivity.this);
        LinearLayout.LayoutParams agoParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        agoParams.gravity = Gravity.CENTER_VERTICAL;
        agoTextView.setCompoundDrawablesRelativeWithIntrinsicBounds(R.drawable.ic_circle_6_light, 0, 0, 0);
        agoTextView.setText(" " + ago);
        agoTextView.setTextColor(getColor(R.color.redLight));
        agoTextView.setTextSize(12);
        agoTextView.setLayoutParams(agoParams);

        TextView usernameTextView = new TextView(CommentActivity.this);
        LinearLayout.LayoutParams paramsUsername = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        usernameTextView.setText(uname);
        usernameTextView.setTextColor(getColor(R.color.redDark));
        usernameTextView.setTextSize(16);
        paramsUsername.setMargins(0, 0, 15, 0);
        paramsUsername.gravity = Gravity.CENTER_VERTICAL | Gravity.START;

        usernameTextView.setLayoutParams(paramsUsername);

        TextView commentTextView = new TextView(CommentActivity.this);
        LinearLayout.LayoutParams paramsComment = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, 70);
        commentTextView.setTextSize(14);
        commentTextView.setText(content);
        paramsComment.setMargins(40, 10, 40, 40);
        paramsUsername.gravity = Gravity.CENTER_VERTICAL | Gravity.START;

        commentTextView.setLayoutParams(paramsComment);


        final ImageView reportImageView = new ImageView(CommentActivity.this);
        LinearLayout.LayoutParams paramsReport = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, 10);
        if (uname.equals(ParseUser.getCurrentUser().getUsername())) {
            reportImageView.setImageResource(R.drawable.ic_delete_forever);
        } else {
            reportImageView.setImageResource(R.drawable.ic_flag_red);
        }
        paramsReport.gravity = Gravity.CENTER_VERTICAL | Gravity.RIGHT;
        paramsReport.setMargins(0, 0, 20, 20);

        reportImageView.setLayoutParams(paramsReport);
        //rowLinearLayout.addView(usernameTextView);
        username_ago.addView(usernameTextView);
        username_ago.addView(agoTextView);
        rowLinearLayout.addView(username_ago);
        rowLinearLayout.addView(commentTextView);
        rowLinearLayout.addView(reportImageView);

        commentLayout.addView(rowLinearLayout);

        if (!uname.equals(ParseUser.getCurrentUser().getUsername())) {
            reportImageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    ParseQuery<ParseObject> alreadyReportedQuery = new ParseQuery<ParseObject>("Reports");
                    alreadyReportedQuery.whereEqualTo("commentId", commentId);
                    alreadyReportedQuery.whereEqualTo("reportOwner", ParseUser.getCurrentUser().getUsername());

                    alreadyReportedQuery.findInBackground(new FindCallback<ParseObject>() {
                        @Override
                        public void done(List<ParseObject> objects, ParseException e) {
                            if (e == null) {
                                if (objects.size() > 0) {
                                    Toast.makeText(CommentActivity.this, "İçeriği daha önceden rapor ettiniz.", Toast.LENGTH_SHORT).show();
                                } else {
                                    AlertDialog.Builder builder = new AlertDialog.Builder(CommentActivity.this);
                                    builder.setTitle("İçeriği rapor etmek istediğinize emin misiniz?");

                                    // Specify the type of input expected; this, for example, sets the input as a password, and will mask the text

                                    // Set up the buttons
                                    builder.setPositiveButton("Evet", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            ParseObject reportObject = new ParseObject("Reports");
                                            reportObject.put("commentId", commentId);
                                            reportObject.put("reportOwner", ParseUser.getCurrentUser().getUsername());
                                            reportObject.put("reportedUsername", uname);
                                            reportObject.put("reportedContent", content);

                                            reportObject.saveInBackground(new SaveCallback() {
                                                @Override
                                                public void done(ParseException e) {
                                                    if (e == null) {
                                                        Toast.makeText(CommentActivity.this, "İçerik rapor edildi.", Toast.LENGTH_SHORT).show();
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
        } else {

            reportImageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(CommentActivity.this);
                    builder.setTitle("Yorumunuzu silmek istediğinize emin misiniz?");

                    // Specify the type of input expected; this, for example, sets the input as a password, and will mask the text

                    // Set up the buttons
                    builder.setPositiveButton("Evet", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            commentLayout.removeView(rowLinearLayout);
                            ParseQuery<ParseObject> deleteOwnCommentQuery = new ParseQuery<ParseObject>("Comments");
                            deleteOwnCommentQuery.whereEqualTo("objectId", commentId);

                            deleteOwnCommentQuery.getFirstInBackground(new GetCallback<ParseObject>() {
                                @Override
                                public void done(ParseObject object, ParseException e) {
                                    if (e == null) {
                                        object.deleteInBackground(new DeleteCallback() {
                                            @Override
                                            public void done(ParseException e) {
                                                if (e == null) {
                                                    Log.i("User deleted own comment", "success");
                                                }
                                            }
                                        });
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
            });

        }
    }
}
