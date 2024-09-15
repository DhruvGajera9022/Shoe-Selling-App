package com.example.shoesapp.fragments;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.example.shoesapp.CartActivity;
import com.example.shoesapp.LoginActivity;
import com.example.shoesapp.R;
import com.example.shoesapp.profileactivities.AboutUsActivity;
import com.example.shoesapp.profileactivities.EditProfileActivity;
import com.example.shoesapp.profileactivities.OrderHistoryActivity;
import com.example.shoesapp.profileactivities.OrdersActivity;
import com.example.shoesapp.profileactivities.PrivacyPolicyActivity;
import com.example.shoesapp.profileactivities.RateUsActivity;
import com.example.shoesapp.profileactivities.SettingsActivity;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class ProfileFragment extends Fragment {
    TextView  txtLanguage, txtSettings, txtAboutUs, txtPrivacyPolicy;
    ImageButton btnEditProfile, btnOrders, btnLanguage, btnOrderHistory, btnSettings, btnAboutUs, btnPrivacyPolicy, btnDeleteUser, profileRateUsBtn;
    AppCompatButton btnLogout;
    FirebaseAuth mAuth;
    FirebaseFirestore firestore;
    String userId;
    ImageView cart4;
    FragmentManager manager;
    Context context;
    LinearLayout llSettings, llEditProfile, llOrderHistory, llOrders, llAboutUs, llPrivacyPolicy,llLanguage, llDeleteUser, llRateUs;

    ProgressBar progressBar;

    public ProfileFragment() {
        // Required empty public constructor
    }

    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        txtLanguage = view.findViewById(R.id.profileSelectLanguage);
        txtSettings = view.findViewById(R.id.profileSettings);
        txtAboutUs = view.findViewById(R.id.profileAboutUs);
        txtPrivacyPolicy = view.findViewById(R.id.profilePrivacyPolicy);
        cart4 = view.findViewById(R.id.cart4);
        llSettings = view.findViewById(R.id.llSettings);
        llEditProfile = view.findViewById(R.id.llEditProfile);
        llOrderHistory = view.findViewById(R.id.llOrderHistory);
        llAboutUs = view.findViewById(R.id.llAboutUs);
        llPrivacyPolicy = view.findViewById(R.id.llPrivacyPolicy);
        llLanguage = view.findViewById(R.id.llLanguage);
        llDeleteUser = view.findViewById(R.id.llDeleteUser);
        llRateUs = view.findViewById(R.id.llRateUs);

        btnEditProfile = view.findViewById(R.id.profileEditProfileBtn);
        btnLanguage = view.findViewById(R.id.profileSelectLanguageBtn);
        btnOrderHistory = view.findViewById(R.id.profileOrderHistoryBtn);
        btnSettings = view.findViewById(R.id.profileSettingsBtn);
        btnAboutUs = view.findViewById(R.id.profileAboutUsBtn);
        btnPrivacyPolicy = view.findViewById(R.id.profilePrivacyPolicyBtn);
        btnDeleteUser = view.findViewById(R.id.profileDeleteUserBtn);
        profileRateUsBtn = view.findViewById(R.id.profileRateUsBtn);

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
        ToAboutUs();
        ToPrivacyPolicy();
        ToDeleteAccount();
        ToRateUs();

        cart4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i= new Intent(getActivity(), CartActivity.class);
                startActivity(i);
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

    public void ToDeleteAccount(){
        llDeleteUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog();
            }
        });

        btnDeleteUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog();
            }
        });
    }

    public void showDialog(){
        new AlertDialog.Builder(getContext())
                .setCancelable(false)
                .setTitle("Delete?")
                .setMessage("Are you sure to delete your account?")
                .setIcon(R.drawable.delete_icon)
                .setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        deleteUserAccount();
                        dialog.dismiss();
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .show();
    }

    public void deleteUserAccount(){
        firestore.collection("Users")
                .document(userId)
                .delete()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        mAuth.signOut();
                        if (getContext() != null) {
                            Intent intent = new Intent(getContext(), LoginActivity.class);
                            startActivity(intent);
                        }
                    }
                });
    }

    public void ToRateUs() {
        // Initialize the context
        context = getActivity();

        // Set up click listeners, but check if user has already submitted a rating
        llRateUs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkIfRatingSubmitted();
            }
        });

        profileRateUsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkIfRatingSubmitted();
            }
        });
    }

    private void checkIfRatingSubmitted() {
        DocumentReference reference = firestore.collection("Rating").document(userId);

        reference.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                DocumentSnapshot document = task.getResult();
                if (document.exists()) {
                    // Rating already submitted
                    Toast.makeText(context, "You have already submitted a rating!", Toast.LENGTH_SHORT).show();
                } else {
                    // Allow the user to rate if no rating has been submitted
                    Intent intent = new Intent(getContext(), RateUsActivity.class);
                    startActivity(intent);
                }
            } else {
                // Handle the error if necessary
                Toast.makeText(context, "Failed to check rating submission. Try again.", Toast.LENGTH_SHORT).show();
            }
        });
    }

}
