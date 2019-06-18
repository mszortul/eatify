package com.example.eatify1506;

import android.app.Application;

import com.parse.Parse;

public final class StarterApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        Parse.initialize(new Parse.Configuration.Builder(this)
                .applicationId("fb3e7e3681d9de6c61575c566b2808e02e70da9f")
                .clientKey("cf06d57eb8b9a329e9d83c612128a12cf7747116")
                .server("http://40.89.156.67:80/parse")
                .build()
        );

    }
}
