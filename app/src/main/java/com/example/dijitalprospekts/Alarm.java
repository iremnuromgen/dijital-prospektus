package com.example.dijitalprospekts;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;

import java.util.ArrayList;

public class Alarm {
    private String alarmSaati;
    private String alarmNotu;

    public Alarm() {}

    public Alarm(String alarmSaati, String alarmNotu) {
        this.alarmSaati = alarmSaati;
        this.alarmNotu = alarmNotu;
    }

    public String getAlarmSaati() {
        return alarmSaati;
    }

    public void setAlarmSaati(String alarmSaati) {
        this.alarmSaati = alarmSaati;
    }

    public String getAlarmNotu() {
        return alarmNotu;
    }

    public void setAlarmNotu(String alarmNotu) {
        this.alarmNotu = alarmNotu;
    }

    static public ArrayList<Alarm> getData(Context context)
    {
        ArrayList<Alarm> alarmList = new ArrayList<>();

        ArrayList<String> alarmSaatiList = new ArrayList<>();
        ArrayList<String> alarmNotuList = new ArrayList<>();

        try
        {
            SQLiteDatabase database = context.openOrCreateDatabase("Alarmlar", Context.MODE_PRIVATE, null);
            Cursor cursor = database.rawQuery("SELECT * FROM alarmlar", null);

            int alarmSaatiIndex = cursor.getColumnIndex("alarmSaat");
            int alarmNotuIndex = cursor.getColumnIndex("alarmNot");

            while (cursor.moveToNext())
            {
                alarmSaatiList.add(cursor.getString(alarmSaatiIndex));
                alarmNotuList.add(cursor.getString(alarmNotuIndex));
            }

            cursor.close();

            for (int i = 0; i < alarmSaatiList.size(); i++)
            {
                Alarm alarm = new Alarm();
                alarm.setAlarmSaati(alarmSaatiList.get(i));
                alarm.setAlarmNotu(alarmNotuList.get(i));

                alarmList.add(alarm);
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        return alarmList;
    }

    public void deleteAlarmFromDatabase(Context context) {
        try {
            SQLiteDatabase database = context.openOrCreateDatabase("Alarmlar", Context.MODE_PRIVATE, null);
            String deleteQuery = "DELETE FROM alarmlar WHERE alarmSaat = ? AND alarmNot = ?";
            SQLiteStatement statement = database.compileStatement(deleteQuery);
            statement.bindString(1, this.alarmSaati);
            statement.bindString(2, this.alarmNotu);
            statement.executeUpdateDelete();
            database.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
