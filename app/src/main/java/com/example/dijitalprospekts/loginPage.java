package com.example.dijitalprospekts;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class loginPage extends AppCompatActivity {

    private Button buttonGoogle;
    private EditText editMail, editPassword;
    String txtMail, txtPassword;
    private FirebaseAuth fAuth;
    private FirebaseUser fUser;
    private FirebaseDatabase fDatabase;
    private DatabaseReference dReference;
    GoogleSignInClient mGoogleSignInClient;
    TextView forgotPassword, nMember;
    int RC_SIGN_IN = 20;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_page);
        getWindow().setStatusBarColor(ContextCompat.getColor(loginPage.this,R.color.color1));

        forgotPassword = (TextView)findViewById(R.id.iForgotMyPassword);
        nMember = (TextView)findViewById(R.id.notMember);
        editMail =(EditText)findViewById(R.id.textMail);
        editPassword =(EditText)findViewById(R.id.textPassword);
        buttonGoogle =(Button)findViewById(R.id.withGoogle);

        fAuth =FirebaseAuth.getInstance();
        fDatabase =FirebaseDatabase.getInstance();
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail().build();

        mGoogleSignInClient = GoogleSignIn.getClient(this,gso);


        buttonGoogle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                googleSignIn();
            }
        });

        String textPassword = "Şifremi Unuttum";
        String textMember = "Üye Ol";

        SpannableString ss1 = new SpannableString(textPassword);
        ClickableSpan cs1 = new ClickableSpan() {
            @Override
            public void onClick(@NonNull View widget) {
            }
        };

        SpannableString ss2 = new SpannableString(textMember);
        ClickableSpan cs2 = new ClickableSpan() {
            @Override
            public void onClick(@NonNull View widget) {
            }
        };

        ss1.setSpan(cs1,0,15, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        ss2.setSpan(cs2,0,6, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        forgotPassword.setText(ss1);
        forgotPassword.setMovementMethod(LinkMovementMethod.getInstance());

        nMember.setText(ss2);
        nMember.setMovementMethod(LinkMovementMethod.getInstance());

        //Şifre sıfırlama sayfasına geçiş
        forgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(loginPage.this, myForgotPassword.class);
                startActivity(intent);
                finish();
            }
        });

        //Kayıt olma sayfasına gidiş
        nMember.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(loginPage.this, signupPage.class);
                startActivity(intent);
                finish();
            }
        });

    }

    private void googleSignIn() {

        Intent intent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(intent, RC_SIGN_IN);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == RC_SIGN_IN)
        {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);

            try{

                GoogleSignInAccount account = task.getResult(ApiException.class);
                firbaseAuth(account.getIdToken());

            }
            catch (Exception e){

                Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void firbaseAuth(String idToken) {

        AuthCredential credential = GoogleAuthProvider.getCredential(idToken,null);
        fAuth.signInWithCredential(credential)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        if(task.isSuccessful())
                        {
                            FirebaseUser user = fAuth.getCurrentUser();
                            HashMap<String,Object> map = new HashMap<>();
                            map.put("id",user.getUid());
                            map.put("name",user.getDisplayName());
                            map.put("profile",user.getPhotoUrl().toString());

                            fDatabase.getReference().child("users").child(user.getUid()).setValue(map);

                            Intent intent = new Intent(loginPage.this, homePage.class);
                            startActivity(intent);
                        }
                        else
                        {
                            Toast.makeText(loginPage.this, "Bir şeyler yanlış gitti...", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    public void signIn(View v){
        txtMail =editMail.getText().toString();
        txtPassword =editPassword.getText().toString();

        if(!TextUtils.isEmpty(txtMail) && !TextUtils.isEmpty(txtPassword))
        {
            fAuth.signInWithEmailAndPassword(txtMail, txtPassword)
                    .addOnSuccessListener(this, new OnSuccessListener<AuthResult>() {
                        @Override
                        public void onSuccess(AuthResult authResult) {
                            fUser = fAuth.getCurrentUser();
                            dReference=FirebaseDatabase.getInstance().getReference("Users").child(fUser.getUid());
                            dReference.addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    for(DataSnapshot snp : snapshot.getChildren())
                                    {
                                        System.out.println(snp.getKey() + "=" + snp.getValue());
                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {
                                    Toast.makeText(loginPage.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            });
                            Toast.makeText(loginPage.this, "Giriş Başarılı.", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(loginPage.this, homePage.class);
                            startActivity(intent);
                        }
                    }).addOnFailureListener(this, new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(loginPage.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
        }
        else
        {
            Toast.makeText(this, "Lütfen E-Mail ve Şifre Girin.", Toast.LENGTH_SHORT).show();
        }
    }
}