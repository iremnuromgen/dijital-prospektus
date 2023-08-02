package com.example.dijitalprospekts;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class searchPage extends AppCompatActivity {

    ImageView ilacResim;
    EditText mName;
    Button mImage, mSearch;
    TextView tvErrorMedicine;
    String strName;
    BottomNavigationView navigationView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_page);
        getWindow().setStatusBarColor(ContextCompat.getColor(searchPage.this,R.color.color1));

        mName = findViewById(R.id.medicineName);
        mImage = findViewById(R.id.medicineImage);
        tvErrorMedicine = findViewById(R.id.errorMedicine);

        navigationView = findViewById(R.id.bottomNavigationView);
        navigationView.setSelectedItemId(R.id.bottomBarSearch);

        navigationView.setOnItemSelectedListener(item -> {
            if (item.getItemId() == R.id.bottomBarUser)
            {
                startActivity(new Intent(getApplicationContext(), homePage.class));
                overridePendingTransition(0,0);
                return true;
            }
            else if (item.getItemId() == R.id.bottomBarSearch)
            {
                return true;
            }
            else if (item.getItemId() == R.id.bottomBarSettings)
            {
                startActivity(new Intent(getApplicationContext(), settingsPage.class));
                overridePendingTransition(0,0);
                return true;
            }
            return false;
        });

        mImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(searchPage.this, cameraorgallery.class);
                startActivity(i);
            }
        });

        inputChanged();
    }

    public void search(View view)
    {
        strName = mName.getText().toString();

        if(!TextUtils.isEmpty(strName))
        {
            String ilacAdi = mName.getText().toString();

            Intent intent = new Intent(searchPage.this, informationButtons.class);
            intent.putExtra("ilacAdi", ilacAdi);
            startActivity(intent);
        }
        else
        {
            if(strName.isEmpty())
            {
                tvErrorMedicine.setVisibility(View.VISIBLE);
            }
            else
            {
                tvErrorMedicine.setVisibility(View.GONE);
            }
        }

    }

    public void inputChanged()
    {
        mName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(s.length() > 0)
                {
                    tvErrorMedicine.setVisibility(View.GONE);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

    }

}