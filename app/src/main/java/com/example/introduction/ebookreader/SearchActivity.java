package com.example.introduction.ebookreader;


import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.GridLayout;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.common.reflect.TypeToken;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import pl.droidsonroids.gif.GifImageView;

public class SearchActivity extends AppCompatActivity {
    private RequestQueue RequestQueue;
    ImageButton micButton,backButton;
    TextView seeAll;
    EditText search;
    String query,micQuery,historyData;
    ArrayList<BookModel> bookModelArrayList,categoryArrayList;
    RecyclerView Book_category,history_recycleView;
    Intent intent;
    GifImageView noResult;

    StorageReference storageRef;
    FirebaseFirestore fStore;
    FirebaseAuth auth;

    JSONObject object= new JSONObject();
    SharedPreferences preferences;
    SharedPreferences.Editor editor;
    ArrayList<infoModel> historyList;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        findId();
        backButton.setOnClickListener(view -> {
            startActivity(new Intent(SearchActivity.this,MainActivity.class));
        });
        //history();

        categoryArrayList=new ArrayList<>();
        historyList =new ArrayList<>();

        setUserInfo();
      GridLayoutManager gridLayoutManager= new GridLayoutManager(this,2,GridLayoutManager.VERTICAL,false);
       Book_category.setLayoutManager(gridLayoutManager);
        Book_category.setAdapter(new categoryAdapter(categoryArrayList,SearchActivity.this));
       // LinearLayoutManager layoutManager= new LinearLayoutManager(this,RecyclerView.VERTICAL,false);
      //  Book_category.setLayoutManager(layoutManager);
       // categoryAdapter adapter= new categoryAdapter(categoryArrayList,SearchActivity.this);
     //   Book_category.setAdapter(adapter);

        loadData();
        if (historyList.isEmpty()){
            history_recycleView.setVisibility(View.GONE);
            noResult.setVisibility(View.VISIBLE);
        }
        else {
            history_recycleView.setVisibility(View.VISIBLE);
            noResult.setVisibility(View.GONE);
        }

        search.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int i, KeyEvent keyEvent) {
                if (keyEvent.getAction() == KeyEvent.ACTION_DOWN && i == KeyEvent.KEYCODE_ENTER) {
                    query=search.getText().toString();
                    query=query.replace(" ","%20");
                    if (query.isEmpty()) {
                        Toast.makeText(SearchActivity.this,"Please enter your book name",Toast.LENGTH_LONG).show();
                    }
                    else {
                        intent = new Intent(SearchActivity.this, ListActivity.class);
                        // passing  input data in next intent
                        intent.putExtra("search", query);
                        startActivity(intent);
                        setInsertData();
                        saveData();
                    }

                    return true;
                }
                return false;
            }
        });


        HistoryRecyclerView();

        seeAll.setOnClickListener(view -> {
            removeData();
        });




    }

    private void removeData() {
        SharedPreferences settings = SearchActivity.this.getSharedPreferences("shared preferences", MODE_PRIVATE);
        settings.edit().clear().commit();
        Toast.makeText(this, "Recent Searches removed", Toast.LENGTH_SHORT).show();
        HistoryRecyclerView();
        history_recycleView.setVisibility(View.GONE);
        noResult.setVisibility(View.VISIBLE);
    }

    private void saveData() {
       preferences = getSharedPreferences("shared preferences", MODE_PRIVATE);
        editor = preferences.edit();
        Gson gson = new Gson();
        String json = gson.toJson(historyList);
        editor.putString("task list", json);
        editor.apply();
    }

    private void HistoryRecyclerView() {
        LinearLayoutManager historyManager = new LinearLayoutManager(this);
        historyManager.setOrientation(RecyclerView.VERTICAL);
        history_recycleView.setLayoutManager(historyManager);
        historyManager.setReverseLayout(true);
        historyManager.setStackFromEnd(true);
        historyAdapter adapter = new historyAdapter(historyList, SearchActivity.this);
        history_recycleView.setAdapter(adapter);
    }

    private void setInsertData() {
        historyList.add(new infoModel(search.getText().toString()));
    }

    private void loadData() {
        SharedPreferences sharedPreferences = getSharedPreferences("shared preferences", MODE_PRIVATE);
        Gson gson = new Gson();
        String json = sharedPreferences.getString("task list", null);
        Type type = new TypeToken<ArrayList<infoModel>>() {}.getType();
        historyList = gson.fromJson(json, type);

        if (historyList == null) {
            historyList = new ArrayList<>();
        }
    }

    private void setUserInfo() {
        categoryArrayList.add(new BookModel("Horror"));
        categoryArrayList.add(new BookModel("Mystery"));
        categoryArrayList.add(new BookModel("Cooking"));
        categoryArrayList.add(new BookModel("Travel"));
        categoryArrayList.add(new BookModel("Comics"));
        categoryArrayList.add(new BookModel("Graphic nobels"));
        categoryArrayList.add(new BookModel("Fiction"));
        categoryArrayList.add(new BookModel("Books of interviews"));
        categoryArrayList.add(new BookModel("Books of lectures"));
        categoryArrayList.add(new BookModel("Hacking"));

    }



    public void btnSpeech(View view) {
        Intent intent= new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT,
                "HI SPEAK SOMETHING");
        try {
            startActivityForResult(intent,1);
        }catch (ActivityNotFoundException e){
            Toast.makeText(this, e.getMessage()
                    , Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode){
            case 1:
                if (resultCode==RESULT_OK && null!=data){

                    ArrayList<String> result =
                            data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                    search.setText(result.get(0));
                    micQuery=search.getText().toString();
                    //query=search.getText().toString();
                    //query=query.replace(" ","%20");
                    micQuery=micQuery.replace(" ","%20");
                   Intent intent1= new Intent(SearchActivity.this, ListActivity.class);
                    Toast.makeText(SearchActivity.this,micQuery,Toast.LENGTH_SHORT).show();
                    intent1.putExtra("micSearch",micQuery);
                    startActivity(intent1);
                }
                break;
        }



    }

    private void findId() {
        micButton=findViewById(R.id.micButton);
        backButton=findViewById(R.id.backButton);
        search= findViewById(R.id.Search);
        Book_category=findViewById(R.id.categoryRecyclerview);
        history_recycleView=findViewById(R.id.historyRecyclerview);
        seeAll=findViewById(R.id.seeAll);
        noResult=findViewById(R.id.noResult);

    }




}