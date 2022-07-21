package com.example.introduction.ebookreader;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class MyBookAdapter extends RecyclerView.Adapter<MyBookAdapter.ViewHolder>{
    private final Context mContext;
    private final ArrayList<PdfInfo> arrayList;
    public MyBookAdapter(Context mContext,ArrayList<PdfInfo> arrayList){
        this.mContext = mContext;
        this.arrayList = arrayList;
    }
    @NonNull
    @Override
    public MyBookAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.my_book_items,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyBookAdapter.ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        if(arrayList!=null) {
            holder.Title.setText(arrayList.get(position).getPdfName());
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent=new Intent(mContext,PdfViewActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    intent.putExtra("PdfUri",arrayList.get(position).getUri());
                    intent.putExtra("PdfName",arrayList.get(position).getPdfName());
                    mContext.startActivity(intent);
                }
            });

        }
        else {
            Toast.makeText(mContext, "Pdf link not found", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }
    public static class ViewHolder extends RecyclerView.ViewHolder{

        TextView Title;
        ImageView pdf_image;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
           Title = itemView.findViewById(R.id.TV_bookTitle);
           pdf_image=itemView.findViewById(R.id.pdf_image);
        }
    }
}
