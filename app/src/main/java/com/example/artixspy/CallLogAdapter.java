package com.example.artixspy;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class CallLogAdapter extends RecyclerView.Adapter<CallLogAdapter.MyViewHolder> {
Context context;
ArrayList<callLogModel> callLogModelArryList;

    public CallLogAdapter(Context context, ArrayList<callLogModel> callLogModelArryList) {
        this.context = context;
        this.callLogModelArryList = callLogModelArryList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.call_log,parent,false);

        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        callLogModel currentLog = callLogModelArryList.get(position);
        holder.number.setText(currentLog.getNumber());
        holder.name.setText(currentLog.getName());
        holder.date.setText(currentLog.getDate());
        holder.time.setText(currentLog.getTime());
        holder.type.setText(currentLog.getType());
        holder.duration.setText(currentLog.getDuration());

    }

    @Override
    public int getItemCount() {
        return callLogModelArryList == null? 0 : callLogModelArryList.size();
    }

    public class MyViewHolder extends  RecyclerView.ViewHolder{
        private CardView cardView;
        private TextView name,number,type,date,time,duration;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            cardView = itemView.findViewById(R.id.cardview);
            name = itemView.findViewById(R.id.name);
            number = itemView.findViewById(R.id.number);
            type = itemView.findViewById(R.id.type);
            time = itemView.findViewById(R.id.time);
            date = itemView.findViewById(R.id.date);
            duration = itemView.findViewById(R.id.duration);


        }
    }
}
