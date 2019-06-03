package com.yilmaz.myeatify;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.jgabrielfreitas.core.BlurImageView;

public class MainActivity extends AppCompatActivity {

    BlurImageView blurImageView;
    Button btn_1, btn_2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        blurImageView = (BlurImageView)findViewById(R.id.BlurImageView);
        blurImageView.setBlur(3);

        btn_1 = (Button) findViewById(R.id.btn_1);
        btn_1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openWelcomeScreen();
            }
        });


        btn_2 = (Button) findViewById(R.id.btn_2);
        btn_2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openSignUpScreen();
            }
        });



    }


    public void openWelcomeScreen(){
        Intent intent = new Intent(this, WelcomeScreen.class);
        startActivity(intent);
    }


    public void openSignUpScreen(){
        Intent intent = new Intent(this, SignUpScreen.class);
        startActivity(intent);
    }



}
