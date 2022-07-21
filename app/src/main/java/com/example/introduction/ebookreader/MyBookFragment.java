package com.example.introduction.ebookreader;

import static com.example.introduction.ebookreader.MyBookList.arrayList;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.DialogFragment;

import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class MyBookFragment extends DialogFragment {
    TextInputEditText book_Title;

    ActivityResultLauncher<Intent> resultLauncher;


    StorageReference storageReference;
    FirebaseFirestore firebaseStorage;
    FirebaseAuth auth;
    Uri sUri;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view=inflater.inflate(R.layout.fragment_mybook, container, false);
        LinearLayout Select_Pdf= view.findViewById(R.id.select_pdf);
         book_Title = view.findViewById(R.id.edit_BookTitle);
        Button Btn_upload = view.findViewById(R.id.Upload_pdf);

        firebaseStorage= FirebaseFirestore.getInstance();
        storageReference= FirebaseStorage.getInstance().getReference();
        auth = FirebaseAuth.getInstance();// instance creation for firebase authentication


        //Initialize result launcher
        resultLauncher=registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        //Initialize result data
                        Intent data= result.getData();
                        //check condition
                        if (data != null){
                            //when data is not empty
                            //get pdf uri
                            sUri= data.getData();
                            //set uri on text view
                           // pdfUrl.setText(Html.fromHtml("<big><b>PDF Uri</b></big><br>"+sUri));
                            //get pdf path
                            String sPath= sUri.getPath();
                            //set pdf path on text view
                            // pdfPath.setText(Html.fromHtml("<big><b>PDF Path</b></big><br>"+sPath));
                            Btn_upload.setOnClickListener(view -> {
                                String name=String.valueOf(book_Title.getText());
                                arrayList.add(new PdfInfo(name));
                                Objects.requireNonNull(getDialog()).dismiss();
                                UploadPdfFirebase(data.getData());
                            });

                        }
                    }
                }
        );
        //set on Click listener on button
        Select_Pdf.setOnClickListener(view1 -> {
            //check condition
            if (ActivityCompat.checkSelfPermission(requireActivity(), Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
                //when permission is not granted
                //request permission
                ActivityCompat.requestPermissions(requireActivity(),new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},1);
            }else {
                //when permission is granted
                //create method
                selectPDF();

            }

        });



        return view;
    }

    private void selectPDF() {
        //Initialize intent
        Intent intent=new Intent(Intent.ACTION_GET_CONTENT);
        //set type
        intent.setType("application/pdf");
        //launch intent
        resultLauncher.launch(intent);
    }

    private void UploadPdfFirebase(Uri data) {
        final ProgressDialog progressDialog=new ProgressDialog(getContext());
        progressDialog.setTitle("Uploading...");
        progressDialog.show();
        String userId=auth.getCurrentUser().getUid();
        Map<String,String> newUser = new HashMap<>();

        StorageReference reference=storageReference.child(userId+"uploads/"+System.currentTimeMillis()+"pdf");
        reference.putFile(data).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Task<Uri> uri= taskSnapshot.getStorage().getDownloadUrl();
                while (!uri.isComplete());
                Uri url= uri.getResult();
                newUser.put("pdfName",String.valueOf(book_Title.getText()));
                newUser.put("pdfUri",url.toString());
                PdfInfo pdf= new PdfInfo(String.valueOf(book_Title.getText()),url.toString());
                firebaseStorage.collection(userId+"UploadPDF").add(newUser)
                        .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                            @Override
                            public void onSuccess(DocumentReference documentReference) {
                                progressDialog.dismiss();
                              //  Toast.makeText(getContext()," Uploaded SuccessFull!!",Toast.LENGTH_LONG).show();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                progressDialog.dismiss();
                              //  Toast.makeText(getContext(),"Uploaded firebaseDatabase Fail!!",Toast.LENGTH_LONG).show();
                            }
                        });
            }
        }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onProgress(@NonNull UploadTask.TaskSnapshot taskSnapshot) {

                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                progressDialog.dismiss();
                Toast.makeText(getContext()," Uploaded Storage Fail!!"+e,Toast.LENGTH_LONG).show();
            }
        });
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        //check condition
        if (requestCode ==1 && grantResults.length >0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
            //when permission is granted
            //call method
            selectPDF();
        }
        else {
            //when permission is denied
            //display toast
            Toast.makeText(getActivity(),"Permission Denied.",Toast.LENGTH_LONG).show();
        }
    }
}