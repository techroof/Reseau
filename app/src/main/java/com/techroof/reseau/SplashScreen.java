package com.techroof.reseau;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.Window;
import android.view.WindowManager;

import com.techroof.reseau.IntroSlider.IntroSliderActivity;
import com.techroof.reseau.Supporter.ProductDetailsActivity;
import com.techroof.reseau.Supporter.SupporterHomeActivity;

import io.paperdb.Paper;

public class SplashScreen extends AppCompatActivity {

    private final int splashLength=2000;
    private String introSlider;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        Paper.init(this);

        introSlider=Paper.book().read("introSlider");

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Window w = getWindow();
            w.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
                    WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        }

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                if (introSlider==null){

                    Intent intro=new Intent(SplashScreen.this, IntroSliderActivity.class);
                    startActivity(intro);
                    finish();

                }else{
                    Intent main=new Intent(SplashScreen.this, StartActivity.class);
                    startActivity(main);
                    finish();
                }
            }
        },splashLength);
    }
}