package com.example.introduction.ebookreader;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class AuthorAdapter  extends RecyclerView.Adapter<AuthorAdapter.ViewHolder>{
    public ArrayList<infoModel> infoArrayList;
    Context mcontext;
    public AuthorAdapter(ArrayList<infoModel> infoArrayList,Context mcontext){
        this.infoArrayList=infoArrayList;
        this.mcontext=mcontext;
    }

    @NonNull
    @Override
    public AuthorAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.author_recyclerview, parent, false);
        return new AuthorAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AuthorAdapter.ViewHolder holder, int position) {
        infoModel info =infoArrayList.get(position);
        if (info.getUri()!=null) {
            Glide.with(mcontext).load(info.getUri()).into(holder.authorIV);
        }
        else {
            holder.authorIV.setImageResource(info.getImage());
        }

        holder.author_name.setText(info.getName());

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
        return infoArrayList.size();
    }
    public static class ViewHolder  extends RecyclerView.ViewHolder{

        CircleImageView authorIV;
        TextView author_name;
        public ViewHolder(View itemView) {
            super(itemView);
         authorIV=itemView.findViewById(R.id.IVauthor);
         author_name=itemView.findViewById(R.id.author_name);
        }


    }
}
