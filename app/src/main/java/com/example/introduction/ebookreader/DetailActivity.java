package com.example.introduction.ebookreader;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.bumptech.glide.Glide;
import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class DetailActivity extends AppCompatActivity {
    String title,publisher, publishedDate,language, description, thumbnail, previewLink, infoLink, buyLink, id,subTitle;
    int pageCount;

    TextView TVTitle,TVpublisher,TVpage,TVlanguage,TVPublishDate,TVdescription;
    CardView previewBtn,buyBtn;
    private ImageView IVbook;
    ImageButton IVback;
    ToggleButton favourite;

    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    ArrayList<BookModel> favouriteList;
    private  static  final String SHARED_PREF_NAME="preferences";
    private  static  final String KEY_NAME_FAVOURITE="favourite list";
    private  static  final String KEY_NAME_HISTORY="History list";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        findId();     // initializing our views..

        getData();      // getting the data which we have passed from our adapter class.

        setData();     // after getting the data we are setting that data to our text views and image view.


        // initializing on click listener for preview button.
        previewBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (previewLink.isEmpty()) {
                    // below toast message is displayed when preview link is not present.
                    Toast.makeText(DetailActivity.this, "Oops Sorry!!\nNo preview Link present for this book", Toast.LENGTH_SHORT).show();
                    return;
                }
                // if the link is present we are opening that link .
                Uri uri = Uri.parse(previewLink);
                Intent i = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(i);
            }
        });

        // initializing on click listener for buy button.
        buyBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (buyLink.isEmpty()) {
                    // below toast message is displaying when buy link is empty.
                    Toast.makeText(DetailActivity.this, "Oops Sorry!!\nNo buy page present for this book", Toast.LENGTH_SHORT).show();
                    return;
                }
                // if the link is present we are opening the link.
                Uri uri = Uri.parse(buyLink);
                Intent i = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(i);
            }
        });
        IVback.setOnClickListener(view -> {
            onBackPressed();
        });
//        favourite.setOnClickListener(view -> {
//            loadData(SHARED_PREF_NAME,KEY_NAME);
//            setInsertData();
//            saveData(SHARED_PREF_NAME,KEY_NAME);
//        });


//        sharedPreferences = getSharedPreferences(SHARED_PREF_NAME, MODE_PRIVATE);
//        editor=sharedPreferences.edit();
//        editor.putString(KEY_NAME,id);
//        editor.apply();

        SharedPreferences sharedPreferences = getSharedPreferences("save",MODE_PRIVATE);

        favourite.setOnClickListener(view -> {
            if (favourite.isChecked()){
                SharedPreferences.Editor editor=getSharedPreferences("save",MODE_PRIVATE).edit();
                editor.putBoolean("value",true);
                editor.apply();
               //favourite.setButtonDrawable(ic_favorite_border);
                favourite.setChecked(true);
                loadData(SHARED_PREF_NAME,KEY_NAME_FAVOURITE);
                FavouriteSetInsertData();
                saveData(SHARED_PREF_NAME,KEY_NAME_FAVOURITE);
            }
            else {
                SharedPreferences.Editor editor=getSharedPreferences("save",MODE_PRIVATE).edit();
                editor.putBoolean("value",false);
                editor.apply();
                 favourite.setChecked(false);
            }
        });
        loadData(SHARED_PREF_NAME,KEY_NAME_HISTORY);
        HistorySetInsertData();
        saveData(SHARED_PREF_NAME,KEY_NAME_HISTORY);

    }

    private void HistorySetInsertData() {
        favouriteList.add(new BookModel(id,title,subTitle, publisher, publishedDate, description, pageCount, thumbnail,language, previewLink, buyLink));
    }

    private void saveData(String SHARED_PREF_NAME,String KEY_NAME) {
        sharedPreferences = getSharedPreferences(SHARED_PREF_NAME, MODE_PRIVATE);
        editor = sharedPreferences.edit();
        Gson gson = new Gson();
        String json = gson.toJson(favouriteList);
        editor.putString(KEY_NAME, json);
        editor.apply();

    }
    private void FavouriteSetInsertData() {
        favouriteList.add(new BookModel(id,title,subTitle, publisher, publishedDate, description, pageCount, thumbnail,language, previewLink, buyLink));
    }

    private void loadData(String SHARED_PREF_NAME,String KEY_NAME) {
        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREF_NAME, MODE_PRIVATE);
        Gson gson = new Gson();
        String json = sharedPreferences.getString(KEY_NAME, null);
        Type type = new TypeToken<ArrayList<BookModel>>() {}.getType();
        favouriteList = gson.fromJson(json, type);

        if (favouriteList == null) {
            favouriteList = new ArrayList<>();
        }


    }

    private void setData() {
        TVTitle.setText(title);
        TVpublisher.setText(publisher);
        TVPublishDate.setText("" + publishedDate);
        TVdescription.setText(description);
        TVpage.setText("" +pageCount);
        Glide.with(DetailActivity.this).load(thumbnail).into(IVbook);
        //Picasso.get().load(thumbnail).into(IVbook);
        TVlanguage.setText(language);
    }

    private void getData() {
        title = getIntent().getStringExtra("title");
        publisher = getIntent().getStringExtra("publisher");
        publishedDate = getIntent().getStringExtra("publishedDate");
        description = getIntent().getStringExtra("description");
        language=getIntent().getStringExtra("language");
        pageCount = getIntent().getIntExtra("pageCount", 0);
        thumbnail = getIntent().getStringExtra("thumbnail");
        previewLink = getIntent().getStringExtra("previewLink");
        infoLink = getIntent().getStringExtra("infoLink");
        buyLink = getIntent().getStringExtra("buyLink");
        id=getIntent().getStringExtra("favouriteID");
        subTitle=getIntent().getStringExtra("subTitle");

    }

    private void findId() {
        TVTitle = findViewById(R.id.TVTitle);
        TVpublisher= findViewById(R.id.TVpublisher);
        TVpage= findViewById(R.id.TVpage);
        TVlanguage= findViewById(R.id.TVlanguage);
        TVPublishDate= findViewById(R.id.TVPublishDate);
        TVdescription= findViewById(R.id.TVdescription);
        IVbook=findViewById(R.id.IVbook);
        IVback=findViewById(R.id.IVBack);
        previewBtn = findViewById(R.id.idBtnPreview);
        buyBtn = findViewById(R.id.idBtnBuy);
        favourite=findViewById(R.id.favorite);
    }
}