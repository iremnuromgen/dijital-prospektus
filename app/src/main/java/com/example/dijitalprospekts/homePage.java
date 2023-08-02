package com.example.dijitalprospekts;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.net.Uri;
import android.net.wifi.hotspot2.pps.HomeSp;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.security.PrivateKey;

import de.hdodenhof.circleimageview.CircleImageView;

public class homePage extends AppCompatActivity implements View.OnClickListener{

    BottomNavigationView navigationView;
    private DatabaseReference dReference;
    private CircleImageView userProfile;
    private FirebaseUser fUser;
    private StorageReference storageReference;
    String userUID;
    private TextView txtWelcome;
    private CardView userInformation, userHealthy, userMedicine, userAlarm;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);
        getWindow().setStatusBarColor(ContextCompat.getColor(homePage.this,R.color.color1));

        navigationView = findViewById(R.id.bottomNavigationView);
        navigationView.setSelectedItemId(R.id.bottomBarUser);

        txtWelcome=findViewById(R.id.welcomeUser);
        userProfile=findViewById(R.id.userProfile);

        userInformation = findViewById(R.id.userInformationCard);
        userHealthy = findViewById(R.id.userHealthyCard);
        userMedicine = findViewById(R.id.userMedicineListCard);
        userAlarm = findViewById(R.id.userAlarmCard);

        fUser=FirebaseAuth.getInstance().getCurrentUser();
        dReference= FirebaseDatabase.getInstance().getReference();
        userUID= FirebaseAuth.getInstance().getCurrentUser().getUid();
        storageReference = FirebaseStorage.getInstance().getReference();

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

        userInformation.setOnClickListener(this);
        userHealthy.setOnClickListener(this);
        userMedicine.setOnClickListener(this);
        userAlarm.setOnClickListener(this);

        userReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    String name = snapshot.child("kullaniciAdi").getValue(String.class);

                    txtWelcome.setText("Hoş geldin " + name);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        if(fUser != null)
        {
            String userID =fUser.getUid();
            StorageReference sReference = storageReference.child("UsersProfiles").child(userID).child("profile.jpg");

            sReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                @Override
                public void onSuccess(Uri uri) {
                    Glide.with(homePage.this)
                            .load(uri)
                            .placeholder(R.drawable.avataruser)
                            .into(userProfile);
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    userProfile.setImageResource(R.drawable.avataruser);
                }
            });
        }
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.userInformationCard) {
            Intent i = new Intent(this, userInformationPage.class);
            startActivity(i);
        } else if (v.getId() == R.id.userHealthyCard) {
            Intent i = new Intent(this, userHealthyPage.class);
            startActivity(i);
        } else if (v.getId() == R.id.userMedicineListCard) {
            Intent i = new Intent(this, userMedicinePage.class);
            startActivity(i);
        } else if (v.getId() == R.id.userAlarmCard) {
            Intent i = new Intent(this, userAlarmPage.class);
            startActivity(i);
        }
    }
}