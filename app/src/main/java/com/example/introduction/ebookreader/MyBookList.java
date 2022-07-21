package com.example.introduction.ebookreader;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import de.hdodenhof.circleimageview.CircleImageView;

public class MyBookList extends AppCompatActivity {
    CardView addButton;
    RecyclerView recyclerView;
    public static MyBookFragment dilog;
    public static ArrayList<PdfInfo> arrayList = new ArrayList<>();
    //Set<PdfInfo> set = new LinkedHashSet<PdfInfo>();
    NavigationView nav;
    ActionBarDrawerToggle toggle;
    DrawerLayout drawerLayout;
    private ProgressBar progressBar;
    Toolbar toolbar;
    CircleImageView profile_image;

    StorageReference storageReference1;
    FirebaseFirestore firebaseFirestore;
    FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_book_list);

        recyclerView = findViewById(R.id.recycleView);
        addButton = findViewById(R.id.addButton);
        nav=(NavigationView) findViewById(R.id.navigation_view);
        drawerLayout =(DrawerLayout) findViewById(R.id.drawerLayout);
        toolbar = (Toolbar) findViewById(R.id.book_toolbar);
        profile_image=findViewById(R.id.profile_image);
        setSupportActionBar(toolbar);
        progressBar = findViewById(R.id.progressBar);
        progressBar.setVisibility(View.VISIBLE);

        arrayList.clear();



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
                Glide.with(MyBookList.this).load(uri).into(profile_image);
            }
        });
        profile_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent= new Intent(MyBookList.this,ProfileActivity.class);
                startActivity(intent);

            }
        });


        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dilog = new MyBookFragment();
                dilog.show(getSupportFragmentManager(),"Dialog");
                setRecycleView();


            }
        });

        UserUploadPdf();


    }

    private void UserUploadPdf() {


        String userId=firebaseAuth.getCurrentUser().getUid();


       firebaseFirestore.collection(userId+"UploadPDF").get()
               .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                   @Override
                   public void onComplete(@NonNull Task<QuerySnapshot> task) {
                       QuerySnapshot qs= task.getResult();
                       List<DocumentSnapshot> dsList =qs.getDocuments();
                       for (DocumentSnapshot ds: dsList){
                           PdfInfo pdf= new PdfInfo(
                                   ds.getString("pdfName"),
                                   ds.getString("pdfUri")

                           );

                           arrayList.add(pdf);
                       }
                       progressBar.setVisibility(View.GONE);
                       setRecycleView();
                   }

               }).addOnFailureListener(new OnFailureListener() {
                   @Override
                   public void onFailure(@NonNull Exception e) {
                       Toast.makeText(MyBookList.this, "No data Found", Toast.LENGTH_SHORT).show();
                   }
               });
    }

    private void setRecycleView() {
        //Toast.makeText(MyBookList.this, arrayList, Toast.LENGTH_LONG).show();
//        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext(),
//                RecyclerView.VERTICAL,false));
//        MyBookAdapter  listAdapter = new MyBookAdapter(getApplicationContext(),arrayList);
//        recyclerView.setAdapter(listAdapter);
        LinearLayoutManager posterManager = new LinearLayoutManager(this);
        posterManager.setOrientation(RecyclerView.VERTICAL);
        recyclerView.setLayoutManager(posterManager);
        MyBookAdapter listAdapter = new MyBookAdapter(getApplicationContext(),arrayList);
        recyclerView.setAdapter(listAdapter);

    }

    private void SideNavigationView() {
        nav.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                item.setChecked(true);
                if (item.getItemId() == R.id.home_menu) {
                    Toast.makeText(getApplicationContext(), "Home panel is open", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(MyBookList.this,MainActivity.class));
                    drawerLayout.closeDrawer(GravityCompat.START);
                }

                if (item.getItemId() == R.id.profile_menu) {
                    Toast.makeText(getApplicationContext(), "Profile panel is open", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(MyBookList.this,ProfileActivity.class));
                    drawerLayout.closeDrawer(GravityCompat.START);
                }
                if (item.getItemId() == R.id.MyBook_menu) {
                    Toast.makeText(getApplicationContext(), "MyBook panel is open", Toast.LENGTH_SHORT).show();
                   //mailmepritam startActivity(new Intent(MyBookList.this,MyBookList.class));
                    drawerLayout.closeDrawer(GravityCompat.START);
                }
                if (item.getItemId() == R.id.report_menu) {
                    Toast.makeText(getApplicationContext(), "report panel is open", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(MyBookList.this,ReportActivity.class));
                    drawerLayout.closeDrawer(GravityCompat.START);
                }
                if (item.getItemId() == R.id.rate_menu) {
                    Toast.makeText(getApplicationContext(), "rate panel is open", Toast.LENGTH_SHORT).show();
                    drawerLayout.closeDrawer(GravityCompat.START);
                }
                if (item.getItemId() == R.id.privacy_menu) {
                    Toast.makeText(getApplicationContext(), "privacy panel is open", Toast.LENGTH_SHORT).show();
                    drawerLayout.closeDrawer(GravityCompat.START);
                }
                return true;
            }
        });
    }
}