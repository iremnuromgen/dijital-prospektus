package com.example.dijitalprospekts;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
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
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;
import com.theartofdev.edmodo.cropper.CropImage;

import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;

public class editUserInformation extends AppCompatActivity {

    BottomNavigationView navigationView;
    private ImageView backButton;
    private Uri imageUri;
    private String myUri = "";
    private StorageTask sTask;
    private StorageReference sReference;;
    EditText editName, editMail, editPhone, editWeight, editAge;
    RadioGroup editGender;
    RadioButton radioMale, radioFemale;
    private CircleImageView editProfile;
    String newGender;
    Button update;
    private DatabaseReference dReference;
    private FirebaseUser fUser;
    private StorageReference storageReference;
    private HashMap<String,Object> mData;
    String userUID;
    private int newPhone, newWeight, newAge;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_user_information);
        getWindow().setStatusBarColor(ContextCompat.getColor(editUserInformation.this,R.color.color1));

        editProfile = findViewById(R.id.settingsProfile);
        editName = findViewById(R.id.editUserName);
        editMail = findViewById(R.id.editUserMail);
        editPhone = findViewById(R.id.editUserPhone);
        editWeight = findViewById(R.id.editUserWeight);
        editAge = findViewById(R.id.editUserAge);
        editGender = findViewById(R.id.editGenderRadioGroup);
        radioMale=findViewById(R.id.maleRadioButton);
        radioFemale=findViewById(R.id.femaleRadioButton);
        update = findViewById(R.id.updateButton);
        backButton = findViewById(R.id.beforePage);

        dReference= FirebaseDatabase.getInstance().getReference();
        userUID= FirebaseAuth.getInstance().getCurrentUser().getUid();
        fUser=FirebaseAuth.getInstance().getCurrentUser();
        storageReference = FirebaseStorage.getInstance().getReference();

        DatabaseReference userReference=dReference.child("Users").child(userUID);

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

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(editUserInformation.this, settingsPage.class);
                startActivity(i);
            }
        });

        userReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    String name = snapshot.child("kullaniciAdi").getValue(String.class);
                    String mail = snapshot.child("kullaniciMaili").getValue(String.class);
                    Integer phone = snapshot.child("kullaniciTelefon").getValue(Integer.class);
                    Integer weight = snapshot.child("kullaniciKilo").getValue(Integer.class);
                    Integer age = snapshot.child("kullaniciYas").getValue(Integer.class);
                    String gender = snapshot.child("kullaniciCinsiyet").getValue(String.class);

                    editName.setText(name);
                    editMail.setText(mail);
                    editPhone.setText(String.valueOf(phone));
                    editWeight.setText(String.valueOf(weight));
                    editAge.setText(String.valueOf(age));

                    if (gender.equals("Erkek")) {
                        radioMale.setChecked(true);
                    } else if (gender.equals("Kadın")) {
                        radioFemale.setChecked(true);
                    }

                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        sReference = storageReference.child("UsersProfiles").child(userUID).child("profile.jpg");

        sReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Glide.with(editUserInformation.this)
                        .load(uri)
                        .placeholder(R.drawable.avataruser)
                        .into(editProfile);
            }
        });

        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                loadProfile();
                String newName = editName.getText().toString();
                String strPhone = editPhone.getText().toString();
                String newMail = editMail.getText().toString();
                String strWeight = editWeight.getText().toString().trim();
                String strAge = editAge.getText().toString().trim();

                int selectedId = editGender.getCheckedRadioButtonId();
                if (selectedId == R.id.femaleRadioButton) {
                    newGender = "Kadın";
                } else if (selectedId == R.id.maleRadioButton) {
                    newGender = "Erkek";
                } else {
                    newGender = null;
                }

                newPhone = Integer.parseInt(strPhone);
                newWeight = Integer.parseInt(strWeight);
                newAge = Integer.parseInt(strAge);

                userReference.child("kullaniciAdi").setValue(newName);
                userReference.child("kullaniciTelefon").setValue(newPhone);
                userReference.child("kullaniciMaili").setValue(newMail);
                userReference.child("kullaniciKilo").setValue(newWeight);
                userReference.child("kullaniciYas").setValue(newAge);
                userReference.child("kullaniciCinsiyet").setValue(newGender);

                Toast.makeText(editUserInformation.this, "Değişiklikleriniz Kaydedildi.", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(editUserInformation.this, loginPage.class);
                startActivity(intent);
                finish();
            }
        });
    }

    public void loadImage(View v)
    {
        CropImage.activity()
                .setAspectRatio(1, 1)
                .start(editUserInformation.this);
    }

    private void loadProfile() {
        if (imageUri != null) {
            final StorageReference storageRef = storageReference.child("UsersProfiles").child(userUID).child("profile.jpg");
            sTask = storageRef.putFile(imageUri);
            sTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                @Override
                public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                    if (!task.isSuccessful()) {
                        throw task.getException();
                    }
                    return storageRef.getDownloadUrl();
                }
            }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(@NonNull Task<Uri> task) {
                    if (task.isSuccessful()) {
                        Uri profilesUri = task.getResult();
                        myUri = profilesUri.toString();

                        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("UsersProfiles").child(userUID);

                        databaseReference.child("userProfileImage").setValue(myUri);

                        Toast.makeText(editUserInformation.this, "Profil Resmi Güncellendi.", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(editUserInformation.this, "Profil Resmi Yükleme Başarısız.", Toast.LENGTH_SHORT).show();
                    }
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(editUserInformation.this, "" + e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            Toast.makeText(editUserInformation.this, "Lütfen bir profil fotoğrafı seçin.", Toast.LENGTH_SHORT).show();
        }
    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE && resultCode == RESULT_OK)
        {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            imageUri = result.getUri();

            editProfile.setImageURI(imageUri);
        }
        else
        {
            editProfile.setImageResource(R.drawable.avataruser);
            Toast.makeText(this, "Resim Seçilemedi.", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(this, editUserInformation.class));
            finish();
        }
    }
}