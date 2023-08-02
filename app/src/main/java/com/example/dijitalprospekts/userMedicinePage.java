package com.example.dijitalprospekts;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class userMedicinePage extends AppCompatActivity {

    private RecyclerView recyclerView;
    private IlacAdapter adapter;
    ImageView buttonBack;
    BottomNavigationView navigationView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_medicine_page);
        getWindow().setStatusBarColor(ContextCompat.getColor(userMedicinePage.this,R.color.color1));

        buttonBack = findViewById(R.id.beforePage);
        recyclerView = findViewById(R.id.listRecyclerView);
        adapter = new IlacAdapter(Ilac.getData(this), this);

        navigationView = findViewById(R.id.bottomNavigationView);
        navigationView.setSelectedItemId(R.id.bottomBarUser);

        navigationView.setOnItemSelectedListener(item -> {
            if (item.getItemId() == R.id.bottomBarUser) {
                return true;
            } else if (item.getItemId() == R.id.bottomBarSearch) {
                startActivity(new Intent(getApplicationContext(), searchPage.class));
                overridePendingTransition(0,0);
                return true;
            } else if (item.getItemId() == R.id.bottomBarSettings) {
                startActivity(new Intent(getApplicationContext(), settingsPage.class));
                overridePendingTransition(0,0);
                return true;
            }
            return false;
        });

        buttonBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(userMedicinePage.this, homePage.class);
                startActivity(i);
            }
        });

        recyclerView.setHasFixedSize(true);
        LinearLayoutManager manager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(manager);
        recyclerView.setAdapter(adapter);

        adapter.setOnItemClickListener(new IlacAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Ilac ilac) {
                String ilacAdi = ilac.getIlacName();

                Intent intent = new Intent(userMedicinePage.this, informationButtons.class);
                intent.putExtra("ilacAdi", ilacAdi);
                startActivity(intent);
            }
        });
    }
}