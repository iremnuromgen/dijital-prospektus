package com.example.dijitalprospekts;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class userInformationPage extends AppCompatActivity {

    ImageView buttonBack;
    BottomNavigationView navigationView;
    TextView txtViewName, txtViewPhone, txtViewMail, txtViewWeight, txtViewAge, txtViewGender;
    private DatabaseReference dReference;
    String userUID;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_information_page);
        getWindow().setStatusBarColor(ContextCompat.getColor(userInformationPage.this,R.color.color1));

        buttonBack = findViewById(R.id.beforePage);
        txtViewName=findViewById(R.id.userNameTextView);
        txtViewPhone=findViewById(R.id.userPhoneTextView);
        txtViewMail=findViewById(R.id.userMailTextView);
        txtViewWeight=findViewById(R.id.userWeightTextView);
        txtViewAge=findViewById(R.id.userAgeTextView);
        txtViewGender=findViewById(R.id.userGenderTextView);

        navigationView = findViewById(R.id.bottomNavigationView);
        navigationView.setSelectedItemId(R.id.bottomBarUser);

        dReference=FirebaseDatabase.getInstance().getReference();
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
                Intent i = new Intent(userInformationPage.this, homePage.class);
                startActivity(i);
            }
        });

        userReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    String name = snapshot.child("kullaniciAdi").getValue(String.class);
                    Integer phone = snapshot.child("kullaniciTelefon").getValue(Integer.class);
                    String mail = snapshot.child("kullaniciMaili").getValue(String.class);
                    Integer weight = snapshot.child("kullaniciKilo").getValue(Integer.class);
                    Integer age = snapshot.child("kullaniciYas").getValue(Integer.class);
                    String gender = snapshot.child("kullaniciCinsiyet").getValue(String.class);

                    txtViewName.setText(name);
                    txtViewPhone.setText(String.valueOf(phone));
                    txtViewMail.setText(mail);
                    txtViewWeight.setText(String.valueOf(weight));
                    txtViewAge.setText(String.valueOf(age));
                    txtViewGender.setText(gender);

                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}
