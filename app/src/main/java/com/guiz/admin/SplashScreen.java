package com.guiz.admin;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;

public class SplashScreen extends AppCompatActivity {

    public static ContainerClass.Admin admin = new ContainerClass.Admin();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                SharedPreferences sharedPreferences = SplashScreen.this.getSharedPreferences("user", MODE_PRIVATE);
                boolean log = sharedPreferences.getBoolean("logged", false);
                if(log){
                    admin.setName(sharedPreferences.getString("name", ""));
                    admin.setUid(sharedPreferences.getString("uid", ""));
                    admin.setCno(sharedPreferences.getString("cno", ""));

                    startActivity(new Intent(SplashScreen.this, MainActivity.class));
                }else {
                    Intent intent = new Intent(SplashScreen.this, LogIn.class);
                    startActivity(intent);
                }
            }
        }, 2000);
    }
}