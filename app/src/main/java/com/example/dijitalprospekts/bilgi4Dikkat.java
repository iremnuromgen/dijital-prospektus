package com.example.dijitalprospekts;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class bilgi4Dikkat extends AppCompatActivity {

    TextView txtBaslik, txtBilgi;
    BottomNavigationView navigationView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bilgi4_dikkat);
        getWindow().setStatusBarColor(ContextCompat.getColor(bilgi4Dikkat.this,R.color.color1));

        String ilacBilgisi = getIntent().getStringExtra("ilacBilgisi");
        String ilacAdi = getIntent().getStringExtra("ilacAdi");

        txtBaslik = findViewById(R.id.baslikDikkat);
        txtBilgi = findViewById(R.id.bilgiDikkat);

        txtBaslik.setText(ilacAdi + " İçin Dikkat Edilmesi Gerekenler");
        txtBilgi.setText(ilacBilgisi != null ? ilacBilgisi : "Bilgi bulunamadı");

        navigationView = findViewById(R.id.bottomNavigationView);
        navigationView.setSelectedItemId(R.id.bottomBarSearch);

        navigationView.setOnItemSelectedListener(item -> {
            if (item.getItemId() == R.id.bottomBarUser)
            {
                startActivity(new Intent(getApplicationContext(), homePage.class));
                overridePendingTransition(0,0);
                return true;
            }
            else if (item.getItemId() == R.id.bottomBarSearch)
            {
                return true;
            }
            else if (item.getItemId() == R.id.bottomBarSettings)
            {
                startActivity(new Intent(getApplicationContext(), settingsPage.class));
                overridePendingTransition(0,0);
                return true;
            }
            return false;
        });
    }
}