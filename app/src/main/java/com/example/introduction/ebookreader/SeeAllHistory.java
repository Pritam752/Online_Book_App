package com.example.introduction.ebookreader;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;


import android.content.SharedPreferences;
import android.os.Bundle;

import android.widget.ImageView;
import android.widget.Toast;

import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class SeeAllHistory extends AppCompatActivity {
    String title,publisher, publishedDate,language, description, thumbnail, previewLink, infoLink, buyLink, id,subTitle;
    int pageCount;


    ImageView back;
    RecyclerView historySeeAllRecyclerview;
    ArrayList<BookModel> SeeAllHistoryList;
    ArrayList<BookModel> bookModelArrayList=new ArrayList<>();
    private  static  final String SHARED_PREF_NAME="preferences";
    private  static  final String KEY_NAME_HISTORY="History list";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_see_all_history);

        historySeeAllRecyclerview = findViewById(R.id.historySeeAllRecyclerview);
        back=findViewById(R.id.backButton);
        back.setOnClickListener(view -> {
            onBackPressed();
        });

       loadData();

        for (int i = 0; i < SeeAllHistoryList.size(); i++) {

            BookModel info = SeeAllHistoryList.get(i);
            if (info != null) {
                title = info.getTitle();
                publisher = info.getPublisher();
                publishedDate = info.getPublishedDate();
                description = info.getDescription();
                language = info.getLanguage();
                pageCount = info.getPageCount();
                thumbnail = info.getThumbnail();
                previewLink = info.getPreviewLink();
                buyLink = info.getBuyLink();
                id = info.getId();
                subTitle = info.getSubtitle();
                BookModel bookInfo1 = new BookModel(id, title, subTitle, publisher, publishedDate, description, pageCount, thumbnail, language, previewLink, buyLink);
                bookModelArrayList.add(bookInfo1);
                if (bookModelArrayList != null) {
                    getRecycleView();
                } else {
                    Toast.makeText(SeeAllHistory.this, "No History Found!!!", Toast.LENGTH_SHORT).show();
                }

            }

        }
        }

    private void loadData() {
        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREF_NAME, MODE_PRIVATE);
        Gson gson = new Gson();
        String json = sharedPreferences.getString(KEY_NAME_HISTORY, null);
        Type type = new TypeToken<ArrayList<BookModel>>() {}.getType();
        SeeAllHistoryList = gson.fromJson(json, type);


    }


        private void getRecycleView () {
            SwipeRefreshLayout refreshLayout = findViewById(R.id.refresh);
            LinearLayoutManager layoutManager = new LinearLayoutManager(this);
            layoutManager.setOrientation(RecyclerView.VERTICAL);
            historySeeAllRecyclerview.setLayoutManager(layoutManager);
            layoutManager.setReverseLayout(true);
            layoutManager.setStackFromEnd(true);
            SeeAllAdapter adapter = new SeeAllAdapter(bookModelArrayList, SeeAllHistory.this);
            historySeeAllRecyclerview.setAdapter(adapter);

        }


    }
