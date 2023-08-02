package com.example.dijitalprospekts;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;

public class myForgotPassword extends AppCompatActivity {

    Button btnSend, btnBack;
    EditText editMail;
    FirebaseAuth fAuth;
    ProgressBar progressBar;
    String strMail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_forgot_password);
        getWindow().setStatusBarColor(ContextCompat.getColor(myForgotPassword.this,R.color.color5));

        btnSend =findViewById(R.id.buttonSend);
        btnBack =findViewById(R.id.buttonBack);
        editMail =findViewById(R.id.editTextMail);
        progressBar =findViewById(R.id.progressBar);

        fAuth =FirebaseAuth.getInstance();

        //Kod gönderme butonu
        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                strMail = editMail.getText().toString().trim();
                if(!TextUtils.isEmpty(strMail))
                {
                    ResetPassword();
                }
                else
                {
                    editMail.setError("E-Mail Boş Olamaz!");
                }
            }
        });

        //Giriş hesabına geri dönme butonu

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(myForgotPassword.this, loginPage.class);
                startActivity(intent);
                finish();
            }
        });

    }

    private void ResetPassword(){
        progressBar.setVisibility(View.VISIBLE);
        btnSend.setVisibility(View.INVISIBLE);

        fAuth.sendPasswordResetEmail(strMail).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                Toast.makeText(myForgotPassword.this, "Şifre Sıfırlama Bağlantısı Mail Hesabınıza Gönderildi.", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(myForgotPassword.this, loginPage.class);
                startActivity(intent);
                finish();
            }
        }) .addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(myForgotPassword.this, "Hata Mesajı : " + e.getMessage(), Toast.LENGTH_SHORT).show();
                progressBar.setVisibility(View.INVISIBLE);
                btnSend.setVisibility(View.VISIBLE);
            }
        });
    }
}