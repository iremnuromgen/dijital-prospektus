package com.example.dijitalprospekts;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class IlacAdapter extends RecyclerView.Adapter<IlacAdapter.IlacHolder> {

    private ArrayList<Ilac> ilacArrayList;
    private Context context;
    private OnItemClickListener listener;

    public IlacAdapter(ArrayList<Ilac> ilacArrayList, Context context) {
        this.ilacArrayList = ilacArrayList;
        this.context = context;
    }

    @NonNull
    @Override
    public IlacHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.custom_list, parent, false);
        return new IlacHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull IlacHolder holder, int position) {
        Ilac ilac = ilacArrayList.get(position);
        holder.setData(ilac);
    }

    @Override
    public int getItemCount()
    {
        return ilacArrayList.size();
    }

    class IlacHolder extends RecyclerView.ViewHolder
    {
        TextView txtIlacName;
        CheckBox fEmpty;
        public IlacHolder(@NonNull View itemView) {
            super(itemView);

            txtIlacName = (TextView) itemView.findViewById(R.id.listLineName);
            fEmpty = (CheckBox) itemView.findViewById(R.id.favoriteEmpty);

            fEmpty.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    if(position != RecyclerView.NO_POSITION)
                    {
                        Ilac deletedAlarm = ilacArrayList.get(position);
                        deleteIlacFromDatabase(deletedAlarm);
                        ilacArrayList.remove(position);
                        notifyItemRemoved(position);
                    }
                }
            });

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();

                    if(listener != null && position != RecyclerView.NO_POSITION)
                    {
                        listener.onItemClick(ilacArrayList.get(position));
                    }
                }
            });
        }

        public void setData(Ilac ilac)
        {
            this.txtIlacName.setText(ilac.getIlacName());

        }
    }
    private void deleteIlacFromDatabase(Ilac ilac)
    {
        ilac.deleteIlacFromDatabase(context);
    }

    public interface OnItemClickListener
    {
        void onItemClick(Ilac ilac);
    }

    public void setOnItemClickListener(OnItemClickListener listener)
    {
        this.listener = listener;
    }
}

