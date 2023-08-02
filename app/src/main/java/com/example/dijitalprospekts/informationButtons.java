package com.example.dijitalprospekts;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class informationButtons extends AppCompatActivity {

    ImageView buttonBack;
    CheckBox buttonFavorite;
    TextView txtBilgi;
    Button ilacUyari, ilacNedir, ilacNeIcin, ilacDikkat, ilacNasil, ilacYanEtki, ilacSaklanmasi;
    BottomNavigationView navigationView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_information_buttons);
        getWindow().setStatusBarColor(ContextCompat.getColor(informationButtons.this,R.color.color1));

        String ilacAdi = getIntent().getStringExtra("ilacAdi");

        ilacUyari = findViewById(R.id.buttonUyari);
        ilacNedir = findViewById(R.id.buttonNedir);
        ilacNeIcin = findViewById(R.id.buttonNeIcinKullanilir);
        ilacDikkat = findViewById(R.id.buttonDikkatEdilmesiGerekenler);
        ilacNasil = findViewById(R.id.buttonNasilKullanilir);
        ilacYanEtki = findViewById(R.id.buttonYanEtkileri);
        ilacSaklanmasi = findViewById(R.id.buttonSaklamaKosullari);
        buttonBack = findViewById(R.id.beforePage);
        buttonFavorite = (CheckBox) findViewById(R.id.favorite); //favorilere ekleme

        txtBilgi = findViewById(R.id.buttonBilgi);
        txtBilgi.setText(ilacAdi + " Hakkında Hangi Bilgiyi Arıyorsunuz?");

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

        buttonBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(informationButtons.this, searchPage.class);
                startActivity(i);
            }
        });

        ilacUyari.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ilacBilgisiHelp ilacBilgisiHelp = new ilacBilgisiHelp(informationButtons.this);
                ilacBilgileri ilacBilgileri = ilacBilgisiHelp.getilacBilgileri(ilacAdi);
                String iUyari = ilacBilgileri != null ? ilacBilgileri.getUyari() : "";

                Intent intent = new Intent(informationButtons.this, bilgi1Uyari.class);
                intent.putExtra("ilacAdi", ilacAdi);
                intent.putExtra("ilacBilgisi", iUyari);
                startActivity(intent);
            }
        });

        ilacNedir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ilacBilgisiHelp ilacBilgisiHelp = new ilacBilgisiHelp(informationButtons.this);
                ilacBilgileri ilacBilgileri = ilacBilgisiHelp.getilacBilgileri(ilacAdi);
                String iNedir = ilacBilgileri != null ? ilacBilgileri.getNedir() : "";

                Intent intent = new Intent(informationButtons.this, bilgi2Nedir.class);
                intent.putExtra("ilacAdi", ilacAdi);
                intent.putExtra("ilacBilgisi", iNedir);
                startActivity(intent);

            }
        });

        ilacNeIcin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ilacBilgisiHelp ilacBilgisiHelp = new ilacBilgisiHelp(informationButtons.this);
                ilacBilgileri ilacBilgileri = ilacBilgisiHelp.getilacBilgileri(ilacAdi);
                String iNeIcin = ilacBilgileri != null ? ilacBilgileri.getNeIcinKullanilir() : "";

                Intent intent = new Intent(informationButtons.this, bilgi3NeIcin.class);
                intent.putExtra("ilacAdi", ilacAdi);
                intent.putExtra("ilacBilgisi", iNeIcin);
                startActivity(intent);
            }
        });

        ilacDikkat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ilacBilgisiHelp ilacBilgisiHelp = new ilacBilgisiHelp(informationButtons.this);
                ilacBilgileri ilacBilgileri = ilacBilgisiHelp.getilacBilgileri(ilacAdi);
                String iDikkat = ilacBilgileri != null ? ilacBilgileri.getDikkatEdilmesiGerekenler() : "";

                Intent intent = new Intent(informationButtons.this, bilgi4Dikkat.class);
                intent.putExtra("ilacAdi", ilacAdi);
                intent.putExtra("ilacBilgisi", iDikkat);
                startActivity(intent);
            }
        });

        ilacNasil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ilacBilgisiHelp ilacBilgisiHelp = new ilacBilgisiHelp(informationButtons.this);
                ilacBilgileri ilacBilgileri = ilacBilgisiHelp.getilacBilgileri(ilacAdi);
                String iNasil = ilacBilgileri != null ? ilacBilgileri.getNasilKullanilir() : "";

                Intent intent = new Intent(informationButtons.this, bilgi5Nasil.class);
                intent.putExtra("ilacAdi", ilacAdi);
                intent.putExtra("ilacBilgisi", iNasil);
                startActivity(intent);
            }
        });

        ilacYanEtki.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ilacBilgisiHelp ilacBilgisiHelp = new ilacBilgisiHelp(informationButtons.this);
                ilacBilgileri ilacBilgileri = ilacBilgisiHelp.getilacBilgileri(ilacAdi);
                String iYanEtki = ilacBilgileri != null ? ilacBilgileri.getYanEtkileri() : "";

                Intent intent = new Intent(informationButtons.this, bilgi6YanEtki.class);
                intent.putExtra("ilacAdi", ilacAdi);
                intent.putExtra("ilacBilgisi", iYanEtki);
                startActivity(intent);
            }
        });

        ilacSaklanmasi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ilacBilgisiHelp ilacBilgisiHelp = new ilacBilgisiHelp(informationButtons.this);
                ilacBilgileri ilacBilgileri = ilacBilgisiHelp.getilacBilgileri(ilacAdi);
                String iSaklanmasi = ilacBilgileri != null ? ilacBilgileri.getSaklanmasi() : "";

                Intent intent = new Intent(informationButtons.this, bilgi7Saklanmasi.class);
                intent.putExtra("ilacAdi", ilacAdi);
                intent.putExtra("ilacBilgisi", iSaklanmasi);
                startActivity(intent);
            }
        });

        buttonFavorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String ilacAdi = getIntent().getStringExtra("ilacAdi");

                try
                {
                    SQLiteDatabase database = informationButtons.this.openOrCreateDatabase("Favorites", MODE_PRIVATE, null);
                    database.execSQL("CREATE TABLE IF NOT EXISTS favorites (id INTEGER PRIMARY KEY, ilacName VARCHAR)");

                    String sqlSorgusu = "INSERT INTO favorites (ilacName) VALUES (?)";
                    SQLiteStatement statement = database.compileStatement(sqlSorgusu);
                    statement.bindString(1, ilacAdi);
                    statement.execute();

                    Toast.makeText(informationButtons.this, ilacAdi + " , İlaç Listesine Eklendi.", Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(informationButtons.this, userMedicinePage.class);
                    intent.putExtra("ilacAdi", ilacAdi);
                    startActivity(intent);
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                    Toast.makeText(informationButtons.this, "İlaç eklenirken hata oluştu : " + e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}