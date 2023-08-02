package com.example.dijitalprospekts;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class signupPage extends AppCompatActivity {

    private LinearLayout scrPassword;
    private FirebaseAuth fAuth;
    private EditText editMail, editPassword, editPasswordTwo;
    private TextView tvErrorMail, tvErrorPassword, tvErrorPasswordTwo;
    private CardView cardOne, cardTwo, cardThree, cardFour, cardFive;
    private boolean isAtLeast8 = false, uppercaseLetter = false, lowercaseLetter = false, atLeastOneNumber = false, hasSymbol = false;
    private Button buttonRegister;
    String txtMail, txtPassword, txtPasswordTwo;
    TextView textRegister;
    private HashMap<String, Object> mData;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup_page);
        getWindow().setStatusBarColor(ContextCompat.getColor(signupPage.this,R.color.color2));

        editMail = (EditText)findViewById(R.id.textRegisterMail);
        editPassword =(EditText)findViewById(R.id.textRegisterPassword);
        editPasswordTwo=findViewById(R.id.textRegisterPasswordTwo);
        scrPassword = findViewById(R.id.securityPassword);

        tvErrorMail=findViewById(R.id.errorMail);
        tvErrorPassword=findViewById(R.id.errorPassword);
        tvErrorPasswordTwo=findViewById(R.id.errorPasswordTwo);
        textRegister =(TextView)findViewById(R.id.signIn);

        cardOne=findViewById(R.id.cardOne);
        cardTwo=findViewById(R.id.cardTwo);
        cardThree=findViewById(R.id.cardThree);
        cardFour=findViewById(R.id.cardFour);
        cardFive=findViewById(R.id.cardFive);

        buttonRegister=findViewById(R.id.buttonRegister);

        fAuth =FirebaseAuth.getInstance();

        String register = "Giriş Yap";

        SpannableString ss3 = new SpannableString(register);
        ClickableSpan cs3 = new ClickableSpan() {
            @Override
            public void onClick(@NonNull View widget) {
            }
        };

        ss3.setSpan(cs3,0,9, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        textRegister.setText(ss3);
        textRegister.setMovementMethod(LinkMovementMethod.getInstance());

        textRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(signupPage.this, loginPage.class);
                startActivity(intent);
                finish();
            }
        });

        editPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                scrPassword.setVisibility(View.VISIBLE);
            }
        });

        editPasswordTwo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                scrPassword.setVisibility(View.GONE);
            }
        });

        inputChanged();
    }

    public void signUp(View v){
        txtMail = editMail.getText().toString();
        txtPassword = editPassword.getText().toString();
        txtPasswordTwo = editPasswordTwo.getText().toString();


        if(!TextUtils.isEmpty(txtMail) && !TextUtils.isEmpty(txtPassword) && !TextUtils.isEmpty(txtPasswordTwo))
        {
            if (txtPassword.equals(txtPasswordTwo))
            {
                fAuth.createUserWithEmailAndPassword(txtMail, txtPassword)
                        .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if(task.isSuccessful())
                                {
                                    Toast.makeText(signupPage.this, "Kayıt İşlemi Başarılı", Toast.LENGTH_SHORT).show();
                                    Intent intent = new Intent(signupPage.this, saveInformation.class);
                                    intent.putExtra("kullaniciMaili", txtMail);
                                    startActivity(intent);
                                    finish();
                                }
                                else
                                {
                                    Toast.makeText(signupPage.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
            }
            else
            {
                Toast.makeText(this, "Şifreler Uyuşmuyor!", Toast.LENGTH_SHORT).show();
            }
        }
        else
        {
            if(txtMail.isEmpty())
            {
                tvErrorMail.setVisibility(View.VISIBLE);
            }
            else
            {
                tvErrorMail.setVisibility(View.GONE);
            }
            if(txtPassword.isEmpty())
            {
                tvErrorPassword.setVisibility(View.VISIBLE);
            }
            if(txtPasswordTwo.isEmpty())
            {
                tvErrorPasswordTwo.setVisibility(View.VISIBLE);
            }
        }
    }

    public void inputChanged()
    {
        editMail.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(s.length() > 0)
                {
                    tvErrorMail.setVisibility(View.GONE);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        editPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                passwordValidation();
                if (s.length() > 0)
                {
                    tvErrorPassword.setVisibility(View.GONE);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        editPasswordTwo.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() > 0)
                {
                    tvErrorPasswordTwo.setVisibility(View.GONE);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

    }

    @SuppressLint("ResourceType")
    public void passwordValidation()
    {
        String txtPassword = editPassword.getText().toString();

        //Şifrenin 8 karakterli olup olmadığının kontrolü
        if(txtPassword.length() >= 8)
        {
            isAtLeast8 = true;
            cardOne.setCardBackgroundColor(Color.parseColor(getString(R.color.colorAccent)));
        }
        else
        {
            isAtLeast8 = false;
            cardOne.setCardBackgroundColor(Color.parseColor(getString(R.color.colorDefault)));
        }

        //Şifrede en az 1 büyük harf olup olmadığının kontrolü
        if (txtPassword.matches("(.*[A-Z].*)"))
        {
            uppercaseLetter = true;
            cardTwo.setCardBackgroundColor(Color.parseColor(getString(R.color.colorAccent)));
        }
        else
        {
            uppercaseLetter = false;
            cardTwo.setCardBackgroundColor(Color.parseColor(getString(R.color.colorDefault)));
        }

        //Şifrede en az 1 küçük harf olup olmadığının kontrolü
        if (txtPassword.matches("(.*[a-z].*)"))
        {
            lowercaseLetter = true;
            cardThree.setCardBackgroundColor(Color.parseColor(getString(R.color.colorAccent)));
        }
        else
        {
            lowercaseLetter = false;
            cardThree.setCardBackgroundColor(Color.parseColor(getString(R.color.colorDefault)));
        }

        //Şifrede en az 1 rakam olup olmadığının kontrolü
        if (txtPassword.matches("(.*[0-9].*)"))
        {
            atLeastOneNumber = true;
            cardFour.setCardBackgroundColor(Color.parseColor(getString(R.color.colorAccent)));
        }
        else
        {
            atLeastOneNumber = false;
            cardFour.setCardBackgroundColor(Color.parseColor(getString(R.color.colorDefault)));
        }

        //Şifrede en az 1 adet özel karakter olup olmadığının kontrolü
        if (txtPassword.matches(".*[!@#$%^&*].*"))
        {
            hasSymbol = true;
            cardFive.setCardBackgroundColor(Color.parseColor(getString(R.color.colorAccent)));
        }
        else
        {
            hasSymbol = false;
            cardFive.setCardBackgroundColor(Color.parseColor(getString(R.color.colorDefault)));
        }
    }
}