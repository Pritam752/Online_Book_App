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
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class login_Fragment extends Fragment {
    TextInputLayout Elayout,Playout;
    TextInputEditText Einput,Pinput;
    Button btn;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v=inflater.inflate(R.layout.fragment_login_, container, false);


        Elayout=v.findViewById(R.id.layout_emails);
        Playout=v.findViewById(R.id.layout_pass);
        Einput=v.findViewById(R.id.ed_email);
        Pinput =v.findViewById(R.id.ed_pass);
        btn=v.findViewById(R.id.btn_submit);
        btn.setOnClickListener(view -> {
            Elayout.setError("");
            Playout.setError("");
            String email = String.valueOf(Einput.getText());
            String pass = String.valueOf(Pinput.getText());

            if (email.isEmpty())
                Elayout.setError("Email need to be provided");
            else if (pass.isEmpty())
                Playout.setError("Password must be at least of 8 characters");
            else {
                FirebaseAuth auth = FirebaseAuth.getInstance();// instance creation for firebase authentication
                auth.signInWithEmailAndPassword(email, pass)
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            localSession localSession =new localSession(getActivity());
                            localSession.holdSession();
                            localSession.addData(localSession.U_EMAIL,email);
                            localSession.addData(localSession.U_ID,auth.getUid());  //user unique id
                            Toast.makeText(getActivity(),"Login successful",Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(getContext(),MainActivity.class));
                        } else {
                            Toast.makeText(getActivity(), "Login failed " + task.getException().getMessage(), Toast.LENGTH_LONG).show();
                        }

                    }

                });


            }

        });
        return v;
    }
}