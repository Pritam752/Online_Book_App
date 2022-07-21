package com.example.introduction.ebookreader;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class populerApater extends  RecyclerView.Adapter<populerApater.ViewHolder>{
    private ArrayList<BookModel> bookModelArrayList;
    private Context mcontext;

    // creating constructor for array list and context.
    public populerApater(ArrayList<BookModel> bookInfoArrayList, Context mcontext) {
        this.bookModelArrayList = bookInfoArrayList;
        this.mcontext = mcontext;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // inflating our layout for item of recycler view item.
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.popular_recyclerview, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        // inside on bind view holder method we are setting  data to each UI component.
        BookModel bookInfo = bookModelArrayList.get(position);
        holder.title.setText(bookInfo.getTitle());
        holder.author.setText(bookInfo.getPublisher());
        Glide.with(mcontext).load(bookInfo.getThumbnail()).into(holder.bookimage);
        //Picasso.get().load(imageurls.get(position)).into(holder.bookimage);
       holder.itemView.setOnClickListener(view -> {
            Intent openLinksIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(bookInfo.getPreviewLink()));
           mcontext.startActivity(openLinksIntent);
        });


    }

    @Override
    public int getItemCount() {
        // inside get item count method we are returning the size of our array list.
        return bookModelArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        // initialize text view and image views.
        TextView title,author;
        ImageView bookimage;

        public ViewHolder( View itemView) {
            super(itemView);
            title=itemView.findViewById(R.id.populer_title);
            author=itemView.findViewById(R.id.populer_author);
            bookimage=itemView.findViewById(R.id.IVpopuler);
        }
    }
}
