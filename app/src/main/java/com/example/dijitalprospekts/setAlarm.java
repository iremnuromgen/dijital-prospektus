package com.example.dijitalprospekts;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.app.AlarmManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.Calendar;
import java.util.Date;

public class setAlarm extends AppCompatActivity {

    private TimePicker timePicker;
    private EditText alarmNote;
    private ImageButton kaydet, geri;
    String alarm, not;
    private int jam, menit;
    BottomNavigationView navigationView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_alarm);
        getWindow().setStatusBarColor(ContextCompat.getColor(setAlarm.this,R.color.color1));

        timePicker = findViewById(R.id.time);
        timePicker.setIs24HourView(true);
        kaydet = findViewById(R.id.alarmKaydet);
        geri = findViewById(R.id.geriDon);
        alarmNote = findViewById(R.id.note);

        navigationView = findViewById(R.id.bottomNavigationView);
        navigationView.setSelectedItemId(R.id.bottomBarUser);

        navigationView.setOnItemSelectedListener(item -> {
            if (item.getItemId() == R.id.bottomBarUser) {
                return true;
            } else if (item.getItemId() == R.id.bottomBarSearch) {
                startActivity(new Intent(getApplicationContext(), searchPage.class));
                overridePendingTransition(0,0);
                return true;
            } else if (item.getItemId() == R.id.bottomBarSettings) {
                startActivity(new Intent(getApplicationContext(), settingsPage.class));
                overridePendingTransition(0,0);
                return true;
            }
            return false;
        });

        timePicker.setOnTimeChangedListener(new TimePicker.OnTimeChangedListener() {
            @Override
            public void onTimeChanged(TimePicker view, int hourOfDay, int minute) {
                jam = hourOfDay;
                menit = minute;
            }
        });

        kaydet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                not = alarmNote.getText().toString();
                alarm = String.format("%02d:%02d", jam, menit);

                try
                {
                    SQLiteDatabase database = setAlarm.this.openOrCreateDatabase("Alarmlar", MODE_PRIVATE, null);
                    database.execSQL("CREATE TABLE IF NOT EXISTS alarmlar (id INTEGER PRIMARY KEY, alarmSaat VARCHAR, alarmNot VARCHAR)");

                    String sqlSorgusu = "INSERT INTO alarmlar (alarmSaat, alarmNot) VALUES (?, ?)";
                    SQLiteStatement statement = database.compileStatement(sqlSorgusu);
                    statement.bindString(1, alarm);
                    statement.bindString(2, not);
                    statement.execute();

                    clear();
                    Toast.makeText(setAlarm.this, "Alarm Kuruldu " + alarm, Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(setAlarm.this, userAlarmPage.class);
                    startActivity(intent);
                    setTimer();
                    notification();
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }
            }
        });

        geri.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(setAlarm.this, userAlarmPage.class);
                startActivity(i);
            }
        });
    }

    private void clear()
    {
        alarmNote.setText("");
    }

    private void notification()
    {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
        {
            CharSequence name = "Alarm Hatırlatıcı";
            String description = "İlaç Vakti";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;

            NotificationChannel channel = new NotificationChannel("Notify", name, importance);
            channel.setDescription(description);

            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

    private void setTimer()
    {
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Date date = new Date();
        Calendar cal_alarm = Calendar.getInstance();
        Calendar cal_now = Calendar.getInstance();

        cal_now.setTime(date);
        cal_alarm.setTime(date);

        cal_alarm.set(Calendar.HOUR_OF_DAY, jam);
        cal_alarm.set(Calendar.MINUTE, menit);
        cal_alarm.set(Calendar.SECOND, 0);

        if(cal_alarm.before(cal_now))
        {
            cal_alarm.add(Calendar.DATE, 1);
        }

        Intent i = new Intent(setAlarm.this, MyBroadcastReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(setAlarm.this, 0 ,i, PendingIntent.FLAG_MUTABLE);
        alarmManager.set(AlarmManager.RTC_WAKEUP, cal_alarm.getTimeInMillis(), pendingIntent);

    }
}