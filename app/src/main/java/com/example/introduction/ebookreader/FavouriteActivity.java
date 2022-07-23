package com.example.introduction.ebookreader;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class FavouriteActivity extends AppCompatActivity {
    private com.android.volley.RequestQueue RequestQueue;
    String title,publisher, publishedDate,language, description, thumbnail, previewLink, infoLink, buyLink, id,subTitle;
    int pageCount;

    String url;
    ArrayList<BookModel> favouriteList;
    ArrayList<String>  list=new ArrayList<>();
    ArrayList<BookModel> bookModelArrayList=new ArrayList<>();
    RecyclerView favouriteView;

    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;

    private  static  final String SHARED_PREF_NAME="myPref";
    private  static  final String KEY_NAME="id";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favourite);
        favouriteView=findViewById(R.id.favouriteRecycleView);

        loadData();

        for (int i=0;i<favouriteList.size();i++){

                BookModel info = favouriteList.get(i);
            if (info!=null) {
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
                BookModel bookInfo = new BookModel(id, title, subTitle, publisher, publishedDate, description, pageCount, thumbnail, language, previewLink, buyLink);
                bookModelArrayList.add(bookInfo);
                getRecycleView();

            }

        }
        //Toast.makeText(FavouriteActivity.this, "" +list, Toast.LENGTH_SHORT).show();
//        sharedPreferences = getSharedPreferences(SHARED_PREF_NAME, MODE_PRIVATE);
//        String id=sharedPreferences.getString(KEY_NAME,null);
//        Toast.makeText(FavouriteActivity.this, "" + id, Toast.LENGTH_SHORT).show();
//        getJson(id);
    }



    private void loadData() {
        SharedPreferences sharedPreferences = getSharedPreferences("preferences", MODE_PRIVATE);
        Gson gson = new Gson();
        String json = sharedPreferences.getString("favourite list", null);
        Type type = new TypeToken<ArrayList<BookModel>>() {}.getType();
        favouriteList = gson.fromJson(json, type);

        if (favouriteList == null) {
            favouriteList = new ArrayList<>();
        }

    }


    private void getRecycleView() {
        SwipeRefreshLayout refreshLayout = findViewById(R.id.refresh);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(RecyclerView.VERTICAL);
        favouriteView.setLayoutManager(layoutManager);
        layoutManager.setReverseLayout(true);
        layoutManager.setStackFromEnd(true);
        BookAdapter adapter = new BookAdapter(bookModelArrayList, FavouriteActivity.this);
        favouriteView.setAdapter(adapter);

    }

}