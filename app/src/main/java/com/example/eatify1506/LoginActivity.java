package com.example.eatify1506;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.google.gson.Gson;
import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.parse.SignUpCallback;

import java.util.ArrayList;
import java.util.Objects;

public class LoginActivity extends AppCompatActivity implements View.OnKeyListener, View.OnClickListener {


    Button midButton, bottomButton;
    EditText username_et, email_et, password_et, confirm_et;
    String username, email, password, confirm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //Log.i("Current User", ParseUser.getCurrentUser().getUsername());


        if (ParseUser.getCurrentUser() != null) {
            Log.i("Inside", "block");
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(intent);
        }


        // initialize view variables
        midButton = findViewById(R.id.midButton);
        bottomButton = findViewById(R.id.bottomButton);
        username_et = findViewById(R.id.usernameEditText);
        email_et = findViewById(R.id.emailEditText);
        password_et = findViewById(R.id.passwordEditText);
        confirm_et = findViewById(R.id.passwordConfirmEditText);


        // set the onClickListener method for background layout
        RelativeLayout backgroundLayout = findViewById(R.id.backgroundLayout);
        backgroundLayout.setOnClickListener(this);

        // set the onKeyListener method for  password edit text
        password_et.setOnKeyListener(this);
    }

    public void switchContext(View view) {
        if (bottomButton.getText().toString().equals("KAYDOL")) {
            // change views to sign up
            bottomButton.setText("GİRİŞ");
            midButton.setText("KAYDOL");
            email_et.setVisibility(View.VISIBLE);
            confirm_et.setVisibility(View.VISIBLE);
        } else {
            // change views to log in
            bottomButton.setText("KAYDOL");
            midButton.setText("GİRİŞ");
            email_et.setVisibility(View.GONE);
            confirm_et.setVisibility(View.GONE);
        }
    }

    public void buttonClick(View view) {

        if (midButton.getText().toString().equals("GİRİŞ")) {
            // log in procedure
            username = username_et.getText().toString();
            password = password_et.getText().toString();

            ParseUser.logInInBackground(username, password, new LogInCallback() {
                @Override
                public void done(ParseUser user, ParseException e) {
                    if (user != null) {
                        Log.i("Login", "Successful");
                        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                        startActivity(intent);
                    } else {
                        Toast.makeText(LoginActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            });

        } else {
            // sign up procedure
            Log.i("Button Clicked", "Sign Up");


            username = username_et.getText().toString();
            email = email_et.getText().toString();
            password = password_et.getText().toString();
            confirm = confirm_et.getText().toString();

            if (password.equals(confirm)) {
                ParseUser newUser = new ParseUser();
                newUser.setUsername(username);
                newUser.setPassword(password);
                newUser.setEmail(email);

                newUser.signUpInBackground(new SignUpCallback() {
                    @Override
                    public void done(ParseException e) {
                        if (e == null) {
                            Log.i("Sign Up", "Successful");

                            // set initial variables for new user
                            ParseObject object = new ParseObject("UserInfo");

                            ArrayList<String> bookmarkList = new ArrayList<>();
                            ArrayList<String> followerList = new ArrayList<>();
                            ArrayList<String> followingList = new ArrayList<>();

                            object.put("followerNumber", 0);
                            object.put("followingNumber", 0);
                            object.put("recipeNumber", 0);
                            object.put("username", ParseUser.getCurrentUser().getUsername());

                            Gson gson = new Gson();
                            Gson gson1 = new Gson();
                            Gson gson2 = new Gson();

                            String json = gson.toJson(bookmarkList);
                            String json1 = gson1.toJson(followerList);
                            String json2 = gson2.toJson(followingList);

                            object.put("bookmarkList", json);
                            object.put("followerList", json1);
                            object.put("followingList", json2);

                            object.saveInBackground(new SaveCallback() {
                                @Override
                                public void done(ParseException e) {
                                    if (e == null) {
                                        Log.i("user info object", "saved.");
                                    }
                                }
                            });

                            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                            startActivity(intent);
                        } else {
                            Log.i("Sign Up", "Failure / " + e.toString());
                            Toast.makeText(LoginActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            } else {
                Toast.makeText(LoginActivity.this, "Şifrenizi kontrol ediniz.", Toast.LENGTH_SHORT).show();
            }
        }
    }

    // Hide keyboard if background is clicked
    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.backgroundLayout || v.getId() == R.id.linear1 || v.getId() == R.id.linear2 || v.getId() == R.id.linear3) {
            InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(Objects.requireNonNull(getCurrentFocus()).getWindowToken(), 0);
        }
    }


    @Override
    public boolean onKey(View v, int keyCode, KeyEvent event) {

        if (keyCode == KeyEvent.KEYCODE_ENTER && event.getAction() == KeyEvent.ACTION_DOWN) {
            buttonClick(findViewById(R.id.midButton));
        }
        return false;
    }
}
