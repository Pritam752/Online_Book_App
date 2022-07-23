package com.example.introduction.ebookreader;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.TextView;


import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;

import java.lang.reflect.Type;
import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class SeeAllAdapter extends RecyclerView.Adapter<SeeAllAdapter.HistoryViewHolder>{
    private ArrayList<BookModel> bookModelArrayList,historyItemsdeleteList;
    private Context mcontext;

    // creating constructor for array list and context.
    public SeeAllAdapter(ArrayList<BookModel> bookInfoArrayList, Context mcontext) {
        this.bookModelArrayList = bookInfoArrayList;
        this.mcontext = mcontext;
    }
    @NonNull
    @Override
    public SeeAllAdapter.HistoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // inflating our layout for item of recycler view item.
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.seeall_history_items, parent, false);
        return new SeeAllAdapter.HistoryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SeeAllAdapter.HistoryViewHolder holder, int position) {
        // inside on bind view holder method we are setting  data to each UI component.
        BookModel bookInfo = bookModelArrayList.get(position);
       // BookModel historyDelete=historyItemsdeleteList.get(position);
        holder.history_previewLink.setText(bookInfo.getPreviewLink());
        holder.history_title.setText(bookInfo.getTitle());


        holder.history_clear.setOnClickListener(view -> {
          int newPosition=holder.getAdapterPosition();
          bookModelArrayList.remove(newPosition);
          notifyItemRemoved(newPosition);
          notifyItemRangeChanged(newPosition,bookModelArrayList.size());
//            loadData();
//            SharedPreferences settings = mcontext.getSharedPreferences("preferences", Context.MODE_PRIVATE);
//            SharedPreferences.Editor editor=mcontext.getSharedPreferences("History list",Context.MODE_PRIVATE).edit();
//            //AdapterView.AdapterContextMenuInfo info=(AdapterView.AdapterContextMenuInfo)view.getMI;
//           editor.remove(b.getId());
//            editor.remove(historyDelete.getTitle());
//            editor.remove(historyDelete.getSubtitle());
//            editor.remove(historyDelete.getPublisher());
//            editor.remove(historyDelete.getPublishedDate());
//            editor.remove(historyDelete.getLanguage());
//            editor.remove(historyDelete.getDescription());
//            editor.remove(historyDelete.getThumbnail());
//            editor.remove(historyDelete.getPreviewLink());
//            editor.remove(historyDelete.getBuyLink());
//           editor.apply();
        });


        //  set image from URL for image view.
        //use glide for imageview
        Glide.with(mcontext).load(bookInfo.getThumbnail()).into(holder.history_thumbnail);

       /*  //use picasso for imageview its don't support in database
        Picasso.get()
                .load(bookInfo.getThumbnail())
                .into(holder.bookIV);*/

        // below line is use to add on click listener for our item of recycler view.
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // inside on click listener method we are calling a new activity
                // and passing all the data of that item in next intent.
                Intent i = new Intent(mcontext,DetailActivity.class);
                i.putExtra("title", bookInfo.getTitle());
                i.putExtra("subTitle",bookInfo.getSubtitle());
                i.putExtra("publisher", bookInfo.getPublisher());
                i.putExtra("publishedDate", bookInfo.getPublishedDate());
                i.putExtra("language",bookInfo.getLanguage());
                i.putExtra("description", bookInfo.getDescription());
                i.putExtra("pageCount", bookInfo.getPageCount());
                i.putExtra("thumbnail", bookInfo.getThumbnail());
                i.putExtra("previewLink", bookInfo.getPreviewLink());
                i.putExtra("buyLink", bookInfo.getBuyLink());
                i.putExtra("favouriteID",bookInfo.getId());

                // after passing that data we are starting our new intent.
                mcontext.startActivity(i);
            }
        });

    }

    @Override
    public int getItemCount() {
        return bookModelArrayList.size();
    }
    public class HistoryViewHolder extends RecyclerView.ViewHolder {
        // initialize text view and image views.
        CircleImageView history_thumbnail,history_clear;
        TextView history_title,history_previewLink;


        public HistoryViewHolder(View itemView) {
            super(itemView);
           history_clear=itemView.findViewById(R.id.history_clear);
            history_title=itemView.findViewById(R.id.history_title);
            history_thumbnail=itemView.findViewById(R.id.history_thumbnail);
            history_previewLink=itemView.findViewById(R.id.history_previewLink);


        }
    }
    private void loadData() {
        SharedPreferences sharedPreferences = mcontext.getSharedPreferences("preferences", Context.MODE_PRIVATE);
        Gson gson = new Gson();
        String json = sharedPreferences.getString("history list", null);
        Type type = new TypeToken<ArrayList<BookModel>>() {}.getType();
        historyItemsdeleteList = gson.fromJson(json, type);
    }


}
