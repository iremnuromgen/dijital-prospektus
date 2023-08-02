package com.example.dijitalprospekts;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
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
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;

public class saveInformation extends AppCompatActivity {
    private EditText editName , editPhone , editWeight ,editAge;
    private RadioGroup genderRadioGroup;
    private Button btn, addProfile;

    private Uri imageUri;
    String[] allergyOptions = {"Alerji Seçiniz", "Yok", "Alerji 1", "Alerji 2", "Alerji 3", "Alerji 4", "Alerji 5"};
    private String myUri = "";
    private StorageTask sTask;
    private StorageReference sReference;
    private CircleImageView userProfile;
    Spinner spinnerAllergy;
    private FirebaseAuth fAuth;
    private FirebaseUser fUser;
    private FirebaseDatabase fDatabase;
    private DatabaseReference dReference;
    private String strName, strPhone, strMail, strWeight, strAge, strGender, strAllergy;
    private int intPhone, intWeight, intAge;
    private HashMap<String, Object> mData;
    private ProgressDialog pd;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_save_information);
        getWindow().setStatusBarColor(ContextCompat.getColor(saveInformation.this,R.color.color1));

        spinnerAllergy = findViewById(R.id.spinner);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.spinner_item_custom, allergyOptions){
            @Override
            public boolean isEnabled(int position) {
                return position != 0;
            }
        };
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerAllergy.setAdapter(adapter);

        editName=findViewById(R.id.userName);
        editPhone=findViewById(R.id.userPhone);
        editWeight=findViewById(R.id.userWeight);
        editAge=findViewById(R.id.userAge);
        btn=findViewById(R.id.infoSave);
        genderRadioGroup = findViewById(R.id.genderRadioGroup);

        userProfile=findViewById(R.id.userProfile); //imageview
        addProfile=(Button)findViewById(R.id.addProfile); //resim ekleme butonu

        Intent intent = getIntent();

        fAuth = FirebaseAuth.getInstance();
        fUser=fAuth.getCurrentUser();
        dReference = FirebaseDatabase.getInstance().getReference();
        sReference = FirebaseStorage.getInstance().getReference("UsersProfiles");


        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                pd = new ProgressDialog(saveInformation.this);
                pd.setMessage("Bilgileriniz Kaydediliyor...");
                pd.show();

                Spinner spinnerAllergy = findViewById(R.id.spinner);
                String selectedAllergy = spinnerAllergy.getSelectedItem().toString();

                strName = editName.getText().toString();
                strPhone = editPhone.getText().toString();
                strWeight = editWeight.getText().toString().trim();
                strAge = editAge.getText().toString().trim();

                if(intent.hasExtra("kullaniciMaili"))
                {
                    strMail=intent.getStringExtra("kullaniciMaili");
                }

                int selectedId = genderRadioGroup.getCheckedRadioButtonId();
                String gender;
                if (selectedId == R.id.femaleRadioButton) {
                    strGender = "Kadın";
                } else if (selectedId == R.id.maleRadioButton) {
                    strGender = "Erkek";
                } else {
                    strGender = null;
                }

                intPhone = Integer.parseInt(strPhone);
                intWeight = Integer.parseInt(strWeight);
                intAge = Integer.parseInt(strAge);

                fUser=fAuth.getCurrentUser();

                mData = new HashMap<>();
                mData.put("kullaniciAdi",strName);
                mData.put("kullaniciTelefon",intPhone);
                mData.put("kullaniciMaili",strMail);
                mData.put("kullaniciKilo",intWeight);
                mData.put("kullaniciYas",intAge);
                mData.put("kullaniciCinsiyet",strGender);
                mData.put("kullaniciAlerji",selectedAllergy);
                mData.put("kullaniciID",fUser.getUid());

                dReference.child("Users").child(fUser.getUid())
                        .setValue(mData)
                        .addOnCompleteListener(saveInformation.this, new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                loadProfile();

                                if(task.isSuccessful())
                                {
                                    pd.dismiss();

                                    Toast.makeText(saveInformation.this, "Bilgiler Başarıyla Kaydedildi.", Toast.LENGTH_SHORT).show();
                                    Intent intent = new Intent(saveInformation.this, loginPage.class);
                                    startActivity(intent);
                                    finish();
                                }
                            }
                        });
            }
        });
    }

    public void loadImage(View v)
    {
        CropImage.activity()
                .setAspectRatio(1, 1)
                .start(saveInformation.this);
    }

    private String imageExtention(Uri uri)
    {
        ContentResolver contentResolver = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();

        return mime.getExtensionFromMimeType(contentResolver.getType(uri));
    }

    private void loadProfile()
    {
        if(imageUri != null)
        {
            final StorageReference storageReference =sReference.child(fUser.getUid()).child("profile.jpg");
            sTask = storageReference.putFile(imageUri);
            sTask.continueWithTask(new Continuation() {
                @Override
                public Object then(@NonNull Task task) throws Exception {
                    if(!task.isSuccessful())
                    {
                        throw task.getException();
                    }
                    return storageReference.getDownloadUrl();
                }
            }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(@NonNull Task<Uri> task) {
                    if(task.isSuccessful())
                    {
                        Uri profilesUri = task.getResult();
                        myUri = profilesUri.toString();

                        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("UserProfiles");

                        String profileID = databaseReference.push().getKey();

                        HashMap<String, Object> hashMap = new HashMap<>();
                        hashMap.put("userProfileID", profileID);
                        hashMap.put("userProfileImage", myUri);
                        hashMap.put("user", FirebaseAuth.getInstance().getCurrentUser().getUid());

                        databaseReference.child(profileID).setValue(hashMap);
                    }
                    else
                    {
                        Toast.makeText(saveInformation.this, "Profil Resmi Yükleme Başarısız.", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(saveInformation.this, loginPage.class));
                        finish();
                    }
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(saveInformation.this, "" + e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }
        else
        {
            Toast.makeText(this, "Resim Seçilmedi.", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE && resultCode == RESULT_OK)
        {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            imageUri = result.getUri();

            userProfile.setImageURI(imageUri);
        }
        else
        {
            userProfile.setImageResource(R.drawable.avataruser);
            Toast.makeText(this, "Resim Seçilemedi.", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(this, saveInformation.class));
            finish();
        }
    }

}