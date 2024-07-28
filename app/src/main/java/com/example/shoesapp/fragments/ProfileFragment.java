package com.example.shoesapp.fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.shoesapp.LoginActivity;
import com.example.shoesapp.R;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

public class ProfileFragment extends Fragment {
    TextView txtUserName, txtUserEmail;
    MaterialButton btnLogout;
    FirebaseAuth mAuth;
    FirebaseFirestore firestore;
    String userId;

    public ProfileFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        mAuth = FirebaseAuth.getInstance();
        firestore = FirebaseFirestore.getInstance();

        txtUserName = view.findViewById(R.id.profileUsername);
        txtUserEmail = view.findViewById(R.id.profileEmail);
        btnLogout = view.findViewById(R.id.userLogout);

        if (mAuth.getCurrentUser() != null) {
            userId = mAuth.getCurrentUser().getUid();

            DocumentReference documentReference = firestore.collection("Users").document(userId);
            documentReference.addSnapshotListener(new EventListener<DocumentSnapshot>() {
                @Override
                public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                    if (error != null) {
                        // Handle the error
                        return;
                    }
                    if (value != null && value.exists()) {
                        txtUserEmail.setText(value.getString("Email"));
                        txtUserName.setText(value.getString("UserName"));
                    }
                }
            });
        }

        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logout();
            }
        });

        return view;
    }

    public void logout() {
        mAuth.signOut();
        if (getContext() != null) {
            startActivity(new Intent(getContext(), LoginActivity.class));
        }
    }
}
