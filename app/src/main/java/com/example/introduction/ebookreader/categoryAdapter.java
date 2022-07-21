package com.example.introduction.ebookreader;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;


public class categoryAdapter extends RecyclerView.Adapter<categoryAdapter.ViewHolder> {
  public ArrayList<BookModel> categoryArrayList;
    Context mcontext;
   public categoryAdapter(ArrayList<BookModel> categoryArrayList,Context mcontext){
       this.categoryArrayList=categoryArrayList;
       this.mcontext=mcontext;
   }

    @NonNull
    @Override
    public categoryAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.category_recyclerview, parent, false);
        RecyclerView.LayoutParams lp=new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        view.setLayoutParams(lp);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull categoryAdapter.ViewHolder holder, int position) {

       String category= categoryArrayList.get(position).getCategory();
       holder.BookCareGory.setText(category);
        // below line is use to add on click listener for our item of recycler view.
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = new Intent(mcontext,ListActivity.class);
                i.putExtra("Info",category);
                mcontext.startActivity(i);

            }
        });

    }

    @Override
    public int getItemCount() {
        return categoryArrayList.size();
    }

    public static class ViewHolder  extends RecyclerView.ViewHolder{
        TextView BookCareGory;
        public ViewHolder(View itemView) {
            super(itemView);
            BookCareGory=itemView.findViewById(R.id.category_book);
        }


    }
}
