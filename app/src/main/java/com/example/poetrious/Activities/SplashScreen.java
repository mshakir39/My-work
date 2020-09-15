package com.example.poetrious.Activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;

import androidx.appcompat.app.AppCompatActivity;

import com.blogspot.atifsoftwares.animatoolib.Animatoo;
import com.example.poetrious.R;

public class SplashScreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_splash_screen);



        SharedPreferences sppp = getSharedPreferences("Exist", 0);
        SharedPreferences sp = getSharedPreferences("checkbox", 0);
        SharedPreferences sp1 = getSharedPreferences("check", 0);
        final boolean cb1 = sp.getBoolean("isLogin", false);
        final boolean usr = sppp.getBoolean("user", false);
        final boolean cb2 = sp1.getBoolean("Datasave", false);
        new Handler().postDelayed(new Runnable(){
            @Override
            public void run() {

                if(cb1&&cb2)
                {


                    startActivity(new Intent(SplashScreen.this, MainActivity.class));
                    Animatoo.animateFade(SplashScreen.this);
                    finish();
                }
                else if (cb1&&usr) {

                    startActivity(new Intent(SplashScreen.this, MainActivity.class));
                    Animatoo.animateFade(SplashScreen.this);
                    finish();
                }
                else if(cb1)
                {
                    startActivity(new Intent(SplashScreen.this, Add_Profile.class));
                    Animatoo.animateFade(SplashScreen.this);
                    finish();
                }

                else
                {

                    startActivity(new Intent(SplashScreen.this, Login.class));
                    Animatoo.animateFade(SplashScreen.this);
                    finish();
                }


            }
        }, 3000);
    }
}
