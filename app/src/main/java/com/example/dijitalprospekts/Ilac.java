package com.example.dijitalprospekts;


import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;

import java.util.ArrayList;

public class Ilac {
    private String ilacName;

    public Ilac() {}

    public Ilac(String ilacName) {
        this.ilacName = ilacName;
    }

    public String getIlacName() {
        return ilacName;
    }

    public void setIlacName(String ilacName) {
        this.ilacName = ilacName;
    }

    static public ArrayList<Ilac> getData(Context context)
    {
        ArrayList<Ilac> ilacArrayList = new ArrayList<>();

        ArrayList<String> ilacNameList = new ArrayList<>();

        try
        {
            SQLiteDatabase database = context.openOrCreateDatabase("Favorites", Context.MODE_PRIVATE, null);
            Cursor cursor = database.rawQuery("SELECT * FROM favorites", null);

            int ilacNameIndex = cursor.getColumnIndex("ilacName");

            while (cursor.moveToNext())
            {
                ilacNameList.add(cursor.getString(ilacNameIndex));
            }

            cursor.close();

            for (int i = 0; i < ilacNameList.size(); i++)
            {
                Ilac ilac = new Ilac();
                ilac.setIlacName(ilacNameList.get(i));

                ilacArrayList.add(ilac);
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        return ilacArrayList;
    }

    public void deleteIlacFromDatabase(Context context) {
        try {
            SQLiteDatabase database = context.openOrCreateDatabase("Favorites", Context.MODE_PRIVATE, null);
            String deleteQuery = "DELETE FROM favorites WHERE ilacName = ?";
            SQLiteStatement statement = database.compileStatement(deleteQuery);
            statement.bindString(1, this.ilacName);
            statement.executeUpdateDelete();
            database.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
