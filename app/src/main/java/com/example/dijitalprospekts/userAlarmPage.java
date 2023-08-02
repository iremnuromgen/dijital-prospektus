package com.example.dijitalprospekts;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.Calendar;

public class userAlarmPage extends AppCompatActivity{

    private RecyclerView recyclerView;
    private ImageView noAlarmImage, backButton;
    private AlarmAdapter adapter;
    Button sAlarm;
    BottomNavigationView navigationView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_alarm_page);
        getWindow().setStatusBarColor(ContextCompat.getColor(userAlarmPage.this,R.color.color1));

        backButton = findViewById(R.id.beforePage);
        sAlarm = findViewById(R.id.setAlarm); //button
        recyclerView = (RecyclerView) findViewById(R.id.alarmsRecyclerView);
        adapter = new AlarmAdapter(Alarm.getData(this), this);
        noAlarmImage = (ImageView) findViewById(R.id.noalarm);

        recyclerView.setHasFixedSize(true);
        LinearLayoutManager manager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(manager);
        recyclerView.setAdapter(adapter);

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

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(userAlarmPage.this, homePage.class);
                startActivity(i);
            }
        });

        sAlarm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(userAlarmPage.this, setAlarm.class);
                startActivity(intent);
            }
        });

        if(adapter.getItemCount() == 0)
        {
            noAlarmImage.setVisibility(View.VISIBLE);
        } else {
            noAlarmImage.setVisibility(View.GONE);
        }
    }
}