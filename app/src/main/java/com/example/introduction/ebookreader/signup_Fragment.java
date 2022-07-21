package com.example.introduction.ebookreader;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class signup_Fragment extends Fragment {
    TextInputLayout namelayout,emaillayout,passlayout;
    TextInputEditText nameinput,emailinput,passinput;
    Button registraion;


    FirebaseFirestore fStore;
    String userID;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view=inflater.inflate(R.layout.fragment_signup_, container, false);

        namelayout=view.findViewById(R.id.layout_name);
        emaillayout=view.findViewById(R.id.layout_email);
        passlayout=view.findViewById(R.id.layout_pwd);
        emailinput=view.findViewById(R.id.edit_email);
        passinput=view.findViewById(R.id.edit_pwd);
        nameinput=view.findViewById(R.id.edit_name);
        registraion=view.findViewById(R.id.btn_registr);



        registraion.setOnClickListener(v -> {
            emaillayout.setError("");
            passlayout.setError("");
            String email = String.valueOf(emailinput.getText());
            String pass = String.valueOf(passinput.getText());
            String name= String.valueOf(nameinput.getText());




            if (name.isEmpty())
                namelayout.setError("Enter your full Name");
            else if (email.isEmpty())
                emaillayout.setError("Email need to be provided");
            else if (pass.isEmpty())
                passlayout.setError("Password must be at least of 8 characters");
            else {
                FirebaseAuth auth = FirebaseAuth.getInstance();// instance creation for firebase authentication
                fStore = FirebaseFirestore.getInstance();
                auth.createUserWithEmailAndPassword(email, pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            //Toast.makeText(getActivity(), "User created", Toast.LENGTH_LONG).show();
                            userID=auth.getCurrentUser().getUid();
                            DocumentReference documentReference=fStore.collection("Users").document(userID);
                            Map<String,Object> user= new HashMap<>();
                            user.put("fName",name);
                            user.put("Email",email);
                            documentReference.set(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void unused) {
                                    Toast.makeText(getActivity(), "User Profile created", Toast.LENGTH_LONG).show();
                                }
                            });

                            startActivity(new Intent(getContext(), MainActivity.class));
                        } else {
                            Toast.makeText(getActivity(), "Error :" + task.getException().getMessage(), Toast.LENGTH_LONG).show();
                        }


                    }
                });
            }

        });

        return view;
    }
}