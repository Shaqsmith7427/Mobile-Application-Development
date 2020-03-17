package com.example.hw_03;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.ViewHolder> {



    public static Interface ainterface;
    Context ctx;

    ArrayList<Daily> dayList;
    private static final String TAG = "demo";



    public RecyclerAdapter(Daily dayList, Context CityWeather) {
        this.dayList = dayList;
        this.ctx = CityWeather;
    }


    @NonNull
    @Override
    public RecyclerAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ConstraintLayout rv_layout = (ConstraintLayout) LayoutInflater.from(parent.getContext()).inflate(R.layout.daycast, parent, false);
        ViewHolder viewHolder = new ViewHolder(rv_layout);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerAdapter.ViewHolder holder, int position) {
        ainterface = (Interface) ctx;
        String image = "http://developer.accuweather.com/sites/default/files/"+dayList.get(position).getDayIcon()+ "-s.png";
        Picasso.get().load(image).into(holder.iv_icon);


        holder.tv_date.setText(dayList.get(position).getDate());
    }

    @Override
    public int getItemCount() {
        return 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        TextView tv_date;
        ImageView iv_icon;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            iv_icon = itemView.findViewById(R.id.iv_dayd2);
            tv_date = itemView.findViewById(R.id.tv_dayDate);


        }
    }

    public interface Interface{
        void deleteItem(int position);
    }


}
