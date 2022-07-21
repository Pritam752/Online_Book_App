package com.example.introduction.ebookreader;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.InputStream;

import de.hdodenhof.circleimageview.CircleImageView;

public class ProfileActivity extends AppCompatActivity {
    BottomNavigationView bottomNavigationView;
    DrawerLayout drawerLayout;
    CircleImageView picture;
    FloatingActionButton btnEdit;
    TextView profileName,profileEmail;
    Button btn_logout;





    private static final  int REQUEST_CODE_STORAGE_PERMISSION=1;
    private static final  int REQUEST_CODE_SELECT_IMAGE=2;

    StorageReference storageRef;
    FirebaseFirestore fStore;
    FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        bottomNavigationView=findViewById(R.id.bottom_navigation);
        picture=findViewById(R.id.profile_pic);
        btnEdit=findViewById(R.id.dp_edit);
        profileName=findViewById(R.id.name);
        profileEmail=findViewById(R.id.email);
        btn_logout=findViewById(R.id.logout);

        // home activity bottom navigation method

        auth = FirebaseAuth.getInstance();// instance creation for firebase authentication
        fStore = FirebaseFirestore.getInstance();
        storageRef = FirebaseStorage.getInstance().getReference();

        String userId=auth.getCurrentUser().getUid();
        Toast.makeText(this, userId, Toast.LENGTH_SHORT).show();


        DocumentReference documentReference=fStore.collection("Users").document(userId);
        documentReference.addSnapshotListener(this, new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                profileName.setText(value.getString("fName"));
                profileEmail.setText(value.getString("Email"));
            }
        });


        //BUTTON CLICK
        btnEdit.setOnClickListener(view -> {
            if (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
                ActivityCompat.requestPermissions(ProfileActivity.this,new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},REQUEST_CODE_STORAGE_PERMISSION);
            }else {
                selectImage();
            }

        });
        btn_logout.setOnClickListener(view ->{
            takeAction();
        });
        StorageReference storageReference=storageRef.child("Users/"+auth.getCurrentUser().getUid()+"/profile.jpg");
        storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Glide.with(ProfileActivity.this).load(uri).into(picture);
            }
        });

    }
    private void takeAction() {
        AlertDialog.Builder alert = new AlertDialog.Builder(ProfileActivity.this);
        alert.setMessage("Are you sure to exit?");
        alert.setTitle("Confirm Logout");
        alert.setNegativeButton("no",(dialog, i) ->{
            dialog.dismiss();
        });
        alert.setPositiveButton("yes",(dialog,i) ->{
            localSession session =new localSession(ProfileActivity.this);
            session.deleteSpace();
            ProfileActivity.this.finishAffinity();
        });
        alert.show();
    }



    private void selectImage() {
        Intent intent =new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        if (intent.resolveActivity(getPackageManager()) != null){
            startActivityForResult(intent,REQUEST_CODE_SELECT_IMAGE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode== REQUEST_CODE_STORAGE_PERMISSION && grantResults.length>0){
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED){
                selectImage();
            }
            else {
                Toast.makeText(ProfileActivity.this,"PERMISSION DENIED!",Toast.LENGTH_LONG).show();
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode ==REQUEST_CODE_SELECT_IMAGE && resultCode==RESULT_OK){
            if (data != null){
                Uri selectedImageUri = data.getData();
                if (selectedImageUri  != null){
                    try {
                        InputStream inputStream =getContentResolver().openInputStream(selectedImageUri);
                        Bitmap bitmap= BitmapFactory.decodeStream(inputStream);
                        //picture.setImageBitmap(bitmap);
                        UploadImageToFirebase(selectedImageUri);
                    } catch (Exception e) {

                        Toast.makeText(ProfileActivity.this,e.getMessage(),Toast.LENGTH_LONG).show();
                    }
                }
            }
        }
    }

    private void UploadImageToFirebase(Uri selectedImageUri) {

        StorageReference fileRef= storageRef.child("Users/"+auth.getCurrentUser().getUid()+"/profile.jpg");
        fileRef.putFile(selectedImageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Toast.makeText(ProfileActivity.this,"Image Uploaded.",Toast.LENGTH_LONG).show();
                fileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        Glide.with(ProfileActivity.this).load(uri).into(picture);
                    }
                });
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(ProfileActivity.this," Uploaded Fail!!"+e,Toast.LENGTH_LONG).show();
            }
        });


    }
}