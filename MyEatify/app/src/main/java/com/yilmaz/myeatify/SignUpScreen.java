package com.yilmaz.myeatify;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.jgabrielfreitas.core.BlurImageView;

public class SignUpScreen extends AppCompatActivity {


    BlurImageView blurImageView_1;
    Button btn_11;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up_screen);


        blurImageView_1 = (BlurImageView)findViewById(R.id.BlurImageView_1);
        blurImageView_1.setBlur(100);
        //blurImageView_1.setBitmapScale(10);



        btn_11 = (Button) findViewById(R.id.btn_11);
        btn_11.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signedUp();
            }
        });

    }


    public void signedUp() {
        Intent intent = new Intent(SignUpScreen.this, MainActivity.class);
        startActivity(intent);
    }

}
