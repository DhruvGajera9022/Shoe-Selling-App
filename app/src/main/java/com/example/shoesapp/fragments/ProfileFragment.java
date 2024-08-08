package com.example.shoesapp.fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.shoesapp.LoginActivity;
import com.example.shoesapp.R;
import com.example.shoesapp.profileactivities.AboutUsActivity;
import com.example.shoesapp.profileactivities.EditProfileActivity;
import com.example.shoesapp.profileactivities.OrderHistoryActivity;
import com.example.shoesapp.profileactivities.OrdersActivity;
import com.example.shoesapp.profileactivities.PrivacyPolicyActivity;
import com.example.shoesapp.profileactivities.SettingsActivity;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

public class ProfileFragment extends Fragment {
    TextView txtLocation, txtLanguage, txtSettings, txtAboutUs, txtPrivacyPolicy;
    ImageButton btnEditProfile, btnOrders, btnLanguage, btnOrderHistory, btnSettings, btnAboutUs, btnPrivacyPolicy;
    MaterialButton btnLogout;
    FirebaseAuth mAuth;
    FirebaseFirestore firestore;
    String userId;
    FragmentManager manager;
    LinearLayout llSettings, llEditProfile, llOrderHistory, llOrders, llAboutUs, llPrivacyPolicy;
    ProgressBar progressBar;

    public ProfileFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        txtLocation = view.findViewById(R.id.profileOrders);
        txtLanguage = view.findViewById(R.id.profileSelectLanguage);
        txtSettings = view.findViewById(R.id.profileSettings);
        txtAboutUs = view.findViewById(R.id.profileAboutUs);
        txtPrivacyPolicy = view.findViewById(R.id.profilePrivacyPolicy);

        llSettings = view.findViewById(R.id.llSettings);
        llEditProfile = view.findViewById(R.id.llEditProfile);
        llOrderHistory = view.findViewById(R.id.llOrderHistory);
        llOrders = view.findViewById(R.id.llSavedLocation);
        llAboutUs = view.findViewById(R.id.llAboutUs);
        llPrivacyPolicy = view.findViewById(R.id.llPrivacyPolicy);

        btnEditProfile = view.findViewById(R.id.profileEditProfileBtn);
        btnOrders = view.findViewById(R.id.profileOrdersBtn);
        btnLanguage = view.findViewById(R.id.profileSelectLanguageBtn);
        btnOrderHistory = view.findViewById(R.id.profileOrderHistoryBtn);
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

        ToEditProfile();
        ToSettings();
        ToOrderHistory();
        ToOrders();
        ToAboutUs();
        ToPrivacyPolicy();

        return view;
    }

    public void logout() {
        mAuth.signOut();
        if (getContext() != null) {
            startActivity(new Intent(getContext(), LoginActivity.class));
        }
    }

    public void ToEditProfile() {
        llEditProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), EditProfileActivity.class);
                startActivity(intent);
            }
        });
        btnEditProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), EditProfileActivity.class);
                startActivity(intent);
            }
        });
    }

    public void ToSettings() {
        llSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), SettingsActivity.class);
                startActivity(intent);
            }
        });
        btnSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), SettingsActivity.class);
                startActivity(intent);
            }
        });
    }

    public void ToOrderHistory(){
        llOrderHistory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), OrderHistoryActivity.class);
                startActivity(intent);
            }
        });
        btnOrderHistory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), OrderHistoryActivity.class);
                startActivity(intent);
            }
        });
    }

    public void ToOrders(){

        llOrders.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), OrdersActivity.class);
                startActivity(intent);
            }
        });

        btnOrders.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), OrdersActivity.class);
                startActivity(intent);
            }
        });

    }

    public void ToAboutUs(){
        llAboutUs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(), AboutUsActivity.class));
            }
        });
        btnAboutUs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(), AboutUsActivity.class));
            }
        });
    }

    public void ToPrivacyPolicy(){
        llPrivacyPolicy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(), PrivacyPolicyActivity.class));
            }
        });
        btnPrivacyPolicy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(), PrivacyPolicyActivity.class));
            }
        });
    }

}
