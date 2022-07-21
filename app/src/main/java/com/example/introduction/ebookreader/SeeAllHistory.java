package com.example.introduction.ebookreader;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import android.content.SharedPreferences;
import android.os.Bundle;

import android.widget.Toast;

import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class SeeAllHistory extends AppCompatActivity {

    RecyclerView historySeeAllRecyclerview;
    ArrayList<infoModel> SeeAllHistoryList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_see_all_history);

        historySeeAllRecyclerview=findViewById(R.id.historySeeAllRecyclerview);


        setData();
        if(SeeAllHistoryList!=null) {
            SeeALLHistoryRecyclerView();
        }
        else {
            Toast.makeText(SeeAllHistory.this, "No History Found!!!", Toast.LENGTH_SHORT).show();
        }
    }

    private void setData() {

            SharedPreferences sharedPreferences = getSharedPreferences("shared preferences", MODE_PRIVATE);
            Gson gson = new Gson();
            String json = sharedPreferences.getString("task list", null);
            Type type = new TypeToken<ArrayList<infoModel>>() {}.getType();
            SeeAllHistoryList = gson.fromJson(json, type);

            if (SeeAllHistoryList == null) {
                SeeAllHistoryList= new ArrayList<>();
            }
        }


    private void SeeALLHistoryRecyclerView() {
        LinearLayoutManager historyManager = new LinearLayoutManager(this);
        historyManager.setOrientation(RecyclerView.VERTICAL);
        historySeeAllRecyclerview.setLayoutManager(historyManager);
        historyManager.setReverseLayout(true);
        historyManager.setStackFromEnd(true);
        historyAdapter adapter = new historyAdapter(SeeAllHistoryList, SeeAllHistory.this);
        historySeeAllRecyclerview.setAdapter(adapter);
    }



    }
