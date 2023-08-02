package com.example.dijitalprospekts;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class userHealthyPage extends AppCompatActivity {

    ImageView buttonBack;
    BottomNavigationView navigationView;
    TextView txtHealthy;
    private DatabaseReference dReference;
    String userUID;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_healthy_page);
        getWindow().setStatusBarColor(ContextCompat.getColor(userHealthyPage.this,R.color.color1));

        navigationView = findViewById(R.id.bottomNavigationView);
        navigationView.setSelectedItemId(R.id.bottomBarUser);

        buttonBack = findViewById(R.id.beforePage);
        txtHealthy = findViewById(R.id.userHealthyTextView);
        dReference= FirebaseDatabase.getInstance().getReference();
        userUID= FirebaseAuth.getInstance().getCurrentUser().getUid();
        DatabaseReference userReference=dReference.child("Users").child(userUID);

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
                Intent i = new Intent(userHealthyPage.this, homePage.class);
                startActivity(i);
            }
        });

        userReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    String allergy = snapshot.child("kullaniciAlerji").getValue(String.class);

                    txtHealthy.setText(allergy);

                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}