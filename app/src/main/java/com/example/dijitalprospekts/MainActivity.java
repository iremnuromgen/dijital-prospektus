package com.example.dijitalprospekts;

import static com.example.dijitalprospekts.R.id.digiProspektus;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    ImageView digitalProspektus;
    private static int nextPage = 5000;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getWindow().setStatusBarColor(ContextCompat.getColor(MainActivity.this,R.color.color3));

        digitalProspektus =(ImageView) findViewById(R.id.digiProspektus);

        Animation animation = AnimationUtils.loadAnimation(this,R.anim.animation);
        digitalProspektus.startAnimation(animation);

        //Geçiş
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(MainActivity.this, introPage.class);
                startActivity(intent);
                finish();
            }
        },nextPage);
    }
}