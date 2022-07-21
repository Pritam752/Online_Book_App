package com.example.introduction.ebookreader;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.helper.widget.Carousel;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.app.Fragment;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.speech.RecognizerIntent;
import android.view.MenuItem;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;
import com.google.common.net.InternetDomainName;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import de.hdodenhof.circleimageview.CircleImageView;

public class MainActivity extends AppCompatActivity {
    NavigationView nav;
    ActionBarDrawerToggle toggle;
    DrawerLayout drawerLayout;
    Toolbar toolbar;
    ImageView mic,Search_icon;
    TextView Search_text;
    ArrayList<infoModel> AuthorArrayList,posterArrayList;
    ArrayList<BookModel> PopulerArrayList;
    RecyclerView PosterRecyclerview,AuthorRecyclerView,PopularRecyclerView;
    CircleImageView profile_image;

    StorageReference storageReference1;
    FirebaseFirestore firebaseFirestore;
    FirebaseAuth firebaseAuth;

    String micQuery;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findID();
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.open, R.string.close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        SideNavigationView();
        firebaseAuth= FirebaseAuth.getInstance();// instance creation for firebase authentication
        firebaseFirestore = FirebaseFirestore.getInstance();
        storageReference1 = FirebaseStorage.getInstance().getReference();

        String userId=firebaseAuth.getCurrentUser().getUid();


        StorageReference storageReference=storageReference1.child("Users/"+userId+"/profile.jpg");
        storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Glide.with(MainActivity.this).load(uri).into(profile_image);
            }
        });
        profile_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent= new Intent(MainActivity.this,ProfileActivity.class);
                startActivity(intent);

            }
        });
        Search_icon.setOnClickListener(view -> {
            startActivity(new Intent(MainActivity.this, SearchActivity.class));

        });
        Search_text.setOnClickListener(view -> {
            startActivity(new Intent(MainActivity.this, SearchActivity.class));

        });


        AuthorArrayList = new ArrayList<>();
        setAuthorsInfo();


        posterArrayList = new ArrayList<>();
        setpostersInfo();


        popularGetData();





    }
    public void btnMic(View view) {
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
                    Search_text.setText(result.get(0));
                    micQuery=Search_text.getText().toString();
                    //query=search.getText().toString();
                    //query=query.replace(" ","%20");
                    micQuery=micQuery.replace(" ","%20");
                    Intent intent1= new Intent(MainActivity.this, ListActivity.class);
                    Toast.makeText(MainActivity.this,micQuery,Toast.LENGTH_SHORT).show();
                    intent1.putExtra("micSearch",micQuery);
                    startActivity(intent1);
                }
                break;
        }



    }

    private void popularGetData() {
        FirebaseFirestore fs= FirebaseFirestore.getInstance();
        fs.collection("populerGetData").get()
                .addOnFailureListener(e -> {
                    // also displaying error message get any error from database.
                    Toast.makeText(MainActivity.this, "No data found" + e, Toast.LENGTH_SHORT).show();
                })
                .addOnCompleteListener(task -> {
                    QuerySnapshot qs= task.getResult();
                    List<DocumentSnapshot> dsList =qs.getDocuments();
                    PopulerArrayList = new ArrayList<>();
                    for (DocumentSnapshot ds: dsList){
                        BookModel bookInfo = new BookModel(
                                ds.getString("titles"),
                                ds.getString("authors"),
                                ds.getString("image"),
                                ds.getString("urls")
                        );

                        PopulerArrayList.add(bookInfo);
                    }
                    //recycler view for popularAdapter
                    populerRecyclerView();
                });


    }

    private void populerRecyclerView() {
        LinearLayoutManager layoutManager= new LinearLayoutManager(this,RecyclerView.HORIZONTAL,false);
        PopularRecyclerView.setLayoutManager(layoutManager);
        populerApater adapter= new populerApater(PopulerArrayList,MainActivity.this);
        PopularRecyclerView.setAdapter(adapter);
    }


    private void setpostersInfo() {
        posterArrayList=new ArrayList<>();
        FirebaseFirestore firebaseFirestore1= FirebaseFirestore.getInstance();
        firebaseFirestore1.collection("POSTERSGETDATA").get()
                .addOnFailureListener(e -> {
                    // also displaying error message get any error from database.
                    Toast.makeText(MainActivity.this, "No data found" + e, Toast.LENGTH_SHORT).show();
                })
                .addOnCompleteListener(task -> {
                    QuerySnapshot posterQs= task.getResult();
                    assert posterQs != null;
                    List<DocumentSnapshot> posterDsList =posterQs.getDocuments();
                    for (DocumentSnapshot ds: posterDsList){
                        infoModel posterInfo = new infoModel(
                                ds.getString("Title"),
                                ds.getString("image")
                        );

                        posterArrayList.add(posterInfo);

                    }
                    //recycler view for popularAdapter
                    posterRecyclerView();

                });



    }

    private void posterRecyclerView() {
        LinearLayoutManager posterManager = new LinearLayoutManager(this);
        posterManager.setOrientation(RecyclerView.HORIZONTAL);
        PosterRecyclerview.setLayoutManager(posterManager);
        PosterAdapter posterAdapter = new PosterAdapter(posterArrayList, MainActivity.this);
        PosterRecyclerview.setAdapter(posterAdapter);


        final int interval_time=5000;
        Handler handler=new Handler();
        Runnable runnable=new Runnable() {
             int count=0;
            @Override
            public void run() {
                if (count<posterArrayList.size()){
                    PosterRecyclerview.scrollToPosition(count++);
                    handler.postDelayed(this,interval_time);
                    if (count==posterArrayList.size()){
                        count=0;
                    }
                }
            }
        };
        handler.postDelayed(runnable,interval_time);
    }

    private void setAuthorsInfo() {
        AuthorArrayList  =new ArrayList<>();
        AuthorArrayList.add(new infoModel("R.K Narayan",R.drawable.rknarayan));
        AuthorArrayList.add(new infoModel("Rabindranath Tagore",R.drawable.tagore));
        FirebaseFirestore firebaseFirestore2= FirebaseFirestore.getInstance();
        firebaseFirestore2.collection("AuthorsGetData").get()
                .addOnFailureListener(e -> {
                    // also displaying error message get any error from database.
                    Toast.makeText(MainActivity.this, "No data found" + e, Toast.LENGTH_SHORT).show();
                })
                .addOnCompleteListener(task -> {
                    QuerySnapshot authorQs= task.getResult();
                    assert authorQs != null;
                    List<DocumentSnapshot> authorDsList =authorQs.getDocuments();
                    for (DocumentSnapshot ds: authorDsList){
                        infoModel posterInfo = new infoModel(
                                ds.getString("author"),
                                ds.getString("image")
                        );

                       AuthorArrayList.add(posterInfo);

                    }
                    //recycler view for popularAdapter
                    AuthorRecyclerView();

                });

    }

    private void AuthorRecyclerView() {
        LinearLayoutManager authorManager = new LinearLayoutManager(this);
        authorManager.setOrientation(RecyclerView.HORIZONTAL);
        AuthorRecyclerView.setLayoutManager(authorManager);
        AuthorAdapter adapter = new AuthorAdapter(AuthorArrayList, MainActivity.this);
        AuthorRecyclerView.setAdapter(adapter);
    }

    private void findID() {
        nav=(NavigationView) findViewById(R.id.nav_view);
        drawerLayout =(DrawerLayout) findViewById(R.id.drawer_layout);
        Search_icon =findViewById(R.id.Search_icon);
        Search_text=findViewById(R.id.Search_text);
        mic=findViewById(R.id.mic);
        AuthorRecyclerView=findViewById(R.id.AuthorRecyclerView);
        PopularRecyclerView=findViewById(R.id.PopularRecyclerView);
        PosterRecyclerview=findViewById(R.id.PosterRecyclerview);
        profile_image=findViewById(R.id.profile_image);
    }

    private void SideNavigationView() {
        nav.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                item.setChecked(true);
                if (item.getItemId() == R.id.home_menu) {
                    Toast.makeText(getApplicationContext(), "Home panel is open", Toast.LENGTH_SHORT).show();
                    drawerLayout.closeDrawer(GravityCompat.START);
                }

                if (item.getItemId() == R.id.profile_menu) {
                    Toast.makeText(getApplicationContext(), "Profile panel is open", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(MainActivity.this,ProfileActivity.class));
                    drawerLayout.closeDrawer(GravityCompat.START);
                }
                if (item.getItemId() == R.id.MyBook_menu) {
                    Toast.makeText(getApplicationContext(), "MyBook panel is open", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(MainActivity.this,MyBookList.class));
                    drawerLayout.closeDrawer(GravityCompat.START);
                }
                if (item.getItemId() == R.id.report_menu) {
                    Toast.makeText(getApplicationContext(), "report panel is open", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(MainActivity.this,ReportActivity.class));
                    drawerLayout.closeDrawer(GravityCompat.START);
                }
                if (item.getItemId() == R.id.rate_menu) {
                    //Toast.makeText(getApplicationContext(), "rate panel is open", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(MainActivity.this,FavouriteActivity.class));
                    drawerLayout.closeDrawer(GravityCompat.START);
                }
                if (item.getItemId() == R.id.privacy_menu) {
                    //Toast.makeText(getApplicationContext(), "privacy panel is open", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(MainActivity.this,SeeAllHistory.class));
                    drawerLayout.closeDrawer(GravityCompat.START);
                }
                return true;
            }
        });

    }
    


}