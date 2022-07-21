package com.example.introduction.ebookreader;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class PosterAdapter extends RecyclerView.Adapter<PosterAdapter.ViewHolder> {
    public ArrayList<infoModel> posterArrayList;
    Context mcontext;
    public PosterAdapter(ArrayList<infoModel> PosterArrayList,Context mcontext){
        this.posterArrayList=PosterArrayList;
        this.mcontext=mcontext;
    }

    @NonNull
    @Override
    public PosterAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.poster_recyclerview, parent, false);
        return new PosterAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PosterAdapter.ViewHolder holder, int position) {
        infoModel info =posterArrayList.get(position);

        if (info.getUri()!=null) {
            Glide.with(mcontext).load(info.getUri()).into(holder.posterIV);
        }
        else {
            holder.posterIV.setImageResource(info.getImage());
        }


        // below line is use to add on click listener for our item of recycler view.
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = new Intent(mcontext,ListActivity.class);
                i.putExtra("Info",info.getName());
                mcontext.startActivity(i);

            }
        });

    }

    @Override
    public int getItemCount() {
        return posterArrayList.size();
    }
    public static class ViewHolder  extends RecyclerView.ViewHolder{

        ImageView posterIV;
        public ViewHolder(View itemView) {
            super(itemView);
            posterIV=itemView.findViewById(R.id.IVposter);
        }


    }
}
