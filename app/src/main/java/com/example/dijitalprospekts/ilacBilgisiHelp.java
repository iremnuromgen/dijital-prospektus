package com.example.dijitalprospekts;

import android.content.Context;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class ilacBilgisiHelp {

    private Context context;
    public ilacBilgisiHelp(Context context) {
        this.context = context;
    }

    public ilacBilgileri getilacBilgileri(String ilacAdi) {
        try {
            InputStream inputStream = context.getAssets().open("ilacProspektus.json");
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
            StringBuilder stringBuilder = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                stringBuilder.append(line);
            }
            String jsonString = stringBuilder.toString();

            JSONObject jsonObject = new JSONObject(jsonString);
            JSONArray ilaclarArray = jsonObject.getJSONArray("prospektus");

            for (int i = 0; i < ilaclarArray.length(); i++) {
                JSONObject ilacObj = ilaclarArray.getJSONObject(i);
                String ad = ilacObj.getString("ilacAdi");
                if (ad.equalsIgnoreCase(ilacAdi)) {
                    String uyari = ilacObj.getString("uyari");
                    String nedir = ilacObj.getString("nedir");
                    String neIcinKullanilir = ilacObj.getString("neIcinKullanilir");
                    String dikkatEdilmesiGerekenler = ilacObj.getString("dikkatEdilmesiGerekenler");
                    String nasilKullanilir = ilacObj.getString("nasilKullanilir");
                    String yanEtkileri = ilacObj.getString("yanEtkileri");
                    String saklanmasi = ilacObj.getString("saklanmasi");
                    return new ilacBilgileri(uyari, nedir, neIcinKullanilir, dikkatEdilmesiGerekenler, nasilKullanilir, yanEtkileri, saklanmasi);
                }
            }
        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }

        return null;
    }
}
