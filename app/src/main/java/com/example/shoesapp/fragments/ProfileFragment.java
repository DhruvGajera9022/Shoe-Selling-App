package com.example.shoesapp.fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.shoesapp.LoginActivity;
import com.example.shoesapp.R;
import com.example.shoesapp.profileactivities.EditProfileActivity;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

public class ProfileFragment extends Fragment {
    TextView txtEditProfile, txtLocation, txtLanguage, txtSettings, txtAboutUs, txtPrivacyPolicy;
    ImageButton btnEditProfile, btnLocation, btnLanguage, btnSettings, btnAboutUs, btnPrivacyPolicy;
    MaterialButton btnLogout;
    FirebaseAuth mAuth;
    FirebaseFirestore firestore;
    String userId;
    FragmentManager manager;

    public ProfileFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        txtEditProfile = view.findViewById(R.id.profileEditProfile);
        txtLocation = view.findViewById(R.id.profileLocation);
        txtLanguage = view.findViewById(R.id.profileSelectLanguage);
        txtSettings = view.findViewById(R.id.profileSettings);
        txtAboutUs = view.findViewById(R.id.profileAboutUs);
        txtPrivacyPolicy = view.findViewById(R.id.profilePrivacyPolicy);

        btnEditProfile = view.findViewById(R.id.profileEditProfileBtn);
        btnLocation = view.findViewById(R.id.profileLocationBtn);
        btnLanguage = view.findViewById(R.id.profileSelectLanguageBtn);
        btnSettings = view.findViewById(R.id.profileSettingsBtn);
        btnAboutUs = view.findViewById(R.id.profileAboutUsBtn);
        btnPrivacyPolicy = view.findViewById(R.id.profilePrivacyPolicyBtn);

        manager = getActivity().getSupportFragmentManager();

        mAuth = FirebaseAuth.getInstance();
        firestore = FirebaseFirestore.getInstance();

        userId = mAuth.getCurrentUser().getUid();

        btnLogout = view.findViewById(R.id.userLogout);

        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logout();
            }
        });

        txtEditProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toEditProfile();
            }
        });
        btnEditProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toEditProfile();
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

    public void toEditProfile(){
        Intent intent = new Intent(getContext(), EditProfileActivity.class);
        startActivity(intent);
    }
}
