package com.example.introduction.ebookreader;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatToggleButton;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;

import java.lang.reflect.Type;
import java.util.ArrayList;

import io.opencensus.resource.Resource;


public class BookAdapter extends RecyclerView.Adapter<BookAdapter.BookViewHolder> {
    // creating variables for arraylist and context.
    private ArrayList<BookModel> bookModelArrayList;
    private Context mcontext;

    // creating constructor for array list and context.
    public BookAdapter(ArrayList<BookModel> bookInfoArrayList, Context mcontext) {
        this.bookModelArrayList = bookInfoArrayList;
        this.mcontext = mcontext;
    }

    @NonNull
    @Override
    public BookViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // inflating our layout for item of recycler view item.
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.book_items, parent, false);
        return new BookViewHolder(view);
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    @Override
    public void onBindViewHolder(@NonNull BookViewHolder holder, int position) {

        // inside on bind view holder method we are setting  data to each UI component.
        BookModel bookInfo = bookModelArrayList.get(position);
        holder.nameTV.setText(bookInfo.getTitle());
        holder.publisherTV.setText(bookInfo.getPublisher());
        holder.pageCountTV.setText ("No of Pages : " + bookInfo.getPageCount());
        holder.dateTV.setText(bookInfo.getPublishedDate());
        holder.language.setText(bookInfo.getLanguage());


        //  set image from URL for image view.
        //use glide for imageview
        Glide.with(mcontext).load(bookInfo.getThumbnail()).into(holder.bookIV);

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
//        SharedPreferences sharedPreferences = mcontext.getSharedPreferences("save",Context.MODE_PRIVATE);
//
//        holder.favourite.setOnClickListener(view -> {
//            if (holder.favourite.isChecked()){
//                SharedPreferences.Editor editor=mcontext.getSharedPreferences("save",Context.MODE_PRIVATE).edit();
//                editor.putBoolean("value",true);
//                editor.apply();
//                holder.favourite.setButtonDrawable(ic_favorite_border);
//                holder.favourite.setChecked(true);
//            }
//            else {
//                SharedPreferences.Editor editor=mcontext.getSharedPreferences("save",Context.MODE_PRIVATE).edit();
//                editor.putBoolean("value",false);
//                editor.apply();
//                 holder.favourite.setChecked(false);
//            }
//        });
//        SharedPreferences sharedPrefer = mcontext.getSharedPreferences("save", Context.MODE_PRIVATE);
//        boolean favouritePref=sharedPrefer.getBoolean("value",true);
//        if (favouritePref==true){
//            holder.favourite.setChecked(true);
//        }
//        else {
//            holder.favourite.setChecked(false);
//        }

    }






    @Override
    public int getItemCount() {
        // inside get item count method we are returning the size of our array list.
        return bookModelArrayList.size();
    }

    public class BookViewHolder extends RecyclerView.ViewHolder {
        // initialize text view and image views.
        TextView nameTV, publisherTV, pageCountTV, dateTV,language;
        ImageView bookIV;
        ToggleButton favourite;


        public BookViewHolder(View itemView) {
            super(itemView);
            nameTV = itemView.findViewById(R.id.idTVBookTitle);
            publisherTV = itemView.findViewById(R.id.idTVpublisher);
            pageCountTV = itemView.findViewById(R.id.idTVPageCount);
            dateTV = itemView.findViewById(R.id.idTVDate);
            bookIV = itemView.findViewById(R.id.idIVbook);
            language=itemView.findViewById(R.id.lang);
            favourite=itemView.findViewById(R.id.favorite);


        }
    }

}
