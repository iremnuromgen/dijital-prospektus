package com.example.dijitalprospekts;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class settingsPage extends AppCompatActivity {

    BottomNavigationView navigationView;
    LinearLayout editProfileLayout, logoutLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings_page);
        getWindow().setStatusBarColor(ContextCompat.getColor(settingsPage.this,R.color.color1));

        editProfileLayout = findViewById(R.id.editButton);
        logoutLayout = findViewById(R.id.logoutButton);

        navigationView = findViewById(R.id.bottomNavigationView);
        navigationView.setSelectedItemId(R.id.bottomBarSettings);

        navigationView.setOnItemSelectedListener(item -> {
            if (item.getItemId() == R.id.bottomBarUser)
            {
                startActivity(new Intent(getApplicationContext(), homePage.class));
                overridePendingTransition(0,0);
                finish();
                return true;
            }
            else if (item.getItemId() == R.id.bottomBarSearch)
            {
                startActivity(new Intent(getApplicationContext(), searchPage.class));
                overridePendingTransition(0,0);
                finish();
                return true;
            }
            else if (item.getItemId() == R.id.bottomBarSettings)
            {
                return true;
            }
            return false;
        });

        editProfileLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(settingsPage.this, editUserInformation.class);
                startActivity(intent);
            }
        });

        logoutLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder alert = new AlertDialog.Builder(settingsPage.this);
                alert.setTitle("Dijital Prospektüs");
                alert.setMessage("Uygulamadan Çıkmak İstediğinize Emin Misiniz?");
                alert.setCancelable(false); //kapanmayı engelliyorum
                alert.setIcon(R.mipmap.ic_launcher);
                alert.setPositiveButton("Hayır", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

                alert.setNegativeButton("Evet", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(settingsPage.this, loginPage.class);
                        startActivity(intent);
                        finish();
                    }
                });

                alert.show();
            }
        });
    }
}