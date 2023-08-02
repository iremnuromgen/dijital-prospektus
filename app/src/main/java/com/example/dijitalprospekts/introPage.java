package com.example.dijitalprospekts;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class introPage extends AppCompatActivity {

    TextView textV2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intro_page);
        getWindow().setStatusBarColor(ContextCompat.getColor(introPage.this,R.color.color1));

        textV2 =(TextView)findViewById(R.id.textView2);

        Typeface font1 = Typeface.createFromAsset(getAssets(), "font/Nalieta_dafont.otf");
        textV2.setTypeface(font1);
    }

    public void login(View v){
        Intent intent = new Intent(introPage.this,loginPage.class);
        startActivity(intent);
    }

    public void signUp(View v){
        Intent intent = new Intent(introPage.this,signupPage.class);
        startActivity(intent);
    }
}