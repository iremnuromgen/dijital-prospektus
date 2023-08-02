package com.example.dijitalprospekts;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class AlarmAdapter extends RecyclerView.Adapter<AlarmAdapter.AlarmHolder> {
    private ArrayList<Alarm> alarmList;
    private Context context;

    public AlarmAdapter(ArrayList<Alarm> alarmList, Context context) {
        this.alarmList = alarmList;
        this.context = context;
    }

    @NonNull
    @Override
    public AlarmHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.custom_item, parent, false);
        return new AlarmHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull AlarmHolder holder, int position) {
        Alarm alarm = alarmList.get(position);
        holder.setData(alarm);

    }

    @Override
    public int getItemCount() {
        return alarmList.size();
    }

    class AlarmHolder extends RecyclerView.ViewHolder
    {
        TextView txtAlarmSaati, txtAlarmNotu;
        ImageView deleteButton;

        public AlarmHolder(@NonNull View itemView) {
            super(itemView);

            txtAlarmSaati = (TextView) itemView.findViewById(R.id.time);
            txtAlarmNotu = (TextView) itemView.findViewById(R.id.name);
            deleteButton = (ImageView) itemView.findViewById(R.id.imageDelete);

            deleteButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    if(position != RecyclerView.NO_POSITION)
                    {
                        Alarm deletedAlarm = alarmList.get(position);
                        deleteAlarmFromDatabase(deletedAlarm);
                        alarmList.remove(position);
                        notifyItemRemoved(position);
                    }
                }
            });

        }

        public void setData(Alarm alarm)
        {
            this.txtAlarmSaati.setText(alarm.getAlarmSaati());
            this.txtAlarmNotu.setText(alarm.getAlarmNotu());
        }
    }

    private void deleteAlarmFromDatabase(Alarm alarm) {
        alarm.deleteAlarmFromDatabase(context);
    }
}
