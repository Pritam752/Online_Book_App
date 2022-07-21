package com.example.introduction.ebookreader;

import static java.security.AccessController.getContext;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class ReportActivity extends AppCompatActivity {
    TextView textSubject;
    EditText etMessage;
    CardView addButton;
    TextView tvAttachment;
    Button btSend;
    ImageButton backButton;

    String email;
    String subject;
    String message;
    Uri URI = null;
    private static final int PICK_FROM_GALLERY = 101;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report);
        textSubject=findViewById(R.id.textSubject);
        etMessage=findViewById(R.id.etMessage);
        addButton=findViewById(R.id.addButton);
        tvAttachment=findViewById(R.id.tvAttachment);
        btSend=findViewById(R.id.btSend);
        backButton=findViewById(R.id.backButton);



        backButton.setOnClickListener(view1 -> {
            startActivity(new Intent(ReportActivity.this,MainActivity.class));
        });

        btSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendEmail();
            }
        });

        //attachment button listener
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openFolder();
            }
        });


    }
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_FROM_GALLERY && resultCode == RESULT_OK) {
            URI = data.getData();
            tvAttachment.setText(URI.getLastPathSegment());
            tvAttachment.setVisibility(View.VISIBLE);
        }
    }

    private void openFolder() {

        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        intent.putExtra("return-data", true);
        startActivityForResult(Intent.createChooser(intent, "Complete action using"), PICK_FROM_GALLERY);
    }

    private void sendEmail() {
        try {
            email = "trxdevil001@gmail.com";
            subject = textSubject.getText().toString();
            message = etMessage.getText().toString();
            final Intent emailIntent = new Intent(android.content.Intent.ACTION_SEND);
            emailIntent.setType("plain/text");
            emailIntent.putExtra(android.content.Intent.EXTRA_EMAIL, new String[]{email});
            emailIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, subject);
            if (URI != null) {
                emailIntent.putExtra(Intent.EXTRA_STREAM, URI);
            }
            emailIntent.putExtra(android.content.Intent.EXTRA_TEXT, message);
            this.startActivity(Intent.createChooser(emailIntent, "Sending email..."));
        } catch (Throwable t) {
            Toast.makeText(this, "Request failed try again: "+ t.toString(), Toast.LENGTH_LONG).show();
        }

    }
}