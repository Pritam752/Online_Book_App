package com.example.introduction.ebookreader;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class historyAdapter extends RecyclerView.Adapter<historyAdapter.ViewHolder> {
    ArrayList<infoModel> historyList;
    Context mcontext;
    public historyAdapter(ArrayList<infoModel> historyList, Context mcontext){
        this.historyList=historyList;
        this.mcontext = mcontext;
    }
    @NonNull
    @Override
    public historyAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.history_recyclerview, parent, false);
        return new historyAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull historyAdapter.ViewHolder holder, int position) {
        infoModel Info = historyList.get(position);
        holder.history.setText(Info.getSearchHistory());
        // below line is use to add on click listener for our item of recycler view.
        String historyQuery= historyList.get(position).getSearchHistory();
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = new Intent(mcontext,ListActivity.class);
                i.putExtra("history",historyQuery);
                mcontext.startActivity(i);

            }
        });
        holder.arrow.setOnClickListener(view -> {

        });


    }

    @Override
    public int getItemCount() {
        return historyList.size();
    }
    public static class ViewHolder  extends RecyclerView.ViewHolder{
        TextView history;
        ImageView arrow;
        public ViewHolder(View itemView) {
            super(itemView);
            history=itemView.findViewById(R.id.history);
            arrow=itemView.findViewById(R.id.historyArrow);
        }


    }
}
