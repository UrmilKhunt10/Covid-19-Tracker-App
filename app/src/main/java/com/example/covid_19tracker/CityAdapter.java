package com.example.covid_19tracker;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class CityAdapter extends RecyclerView.Adapter<CityAdapter.ViewHolder>{
    Context context;
    ArrayList<Model> list;
    public CityAdapter(Context context, ArrayList<Model> list){
        this.context = context;
        this.list = list;
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.city_layout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CityAdapter.ViewHolder holder, int position) {
        holder.cityName.setText(list.get(position).getCityName());
        holder.active.setText(String.valueOf(list.get(position).getActive()));
        holder.confirmed.setText(String.valueOf(list.get(position).getConfirmed()));
        holder.deceased.setText(String.valueOf(list.get(position).getDeceased()));
        holder.recovered.setText(String.valueOf(list.get(position).getRecovered()));
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView cityName, active, confirmed, deceased, recovered;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            cityName = itemView.findViewById(R.id.cityName);
            active = itemView.findViewById(R.id.active);
            confirmed = itemView.findViewById(R.id.confirmed);
            deceased = itemView.findViewById(R.id.deceased);
            recovered = itemView.findViewById(R.id.recovered);
        }
    }
}