package com.example.shoesapp.profileactivities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import com.example.shoesapp.R;
import com.example.shoesapp.fragments.ProfileFragment;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

public class RateUsActivity extends AppCompatActivity {
    ImageView toolbar;
    AppCompatButton btnSubmit;
    RatingBar ratingBar;
    FirebaseAuth mAuth;
    FirebaseFirestore firestore;
    String uid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rate_us);

        toolbar = findViewById(R.id.toolbar);
        toolbar.setOnClickListener(v -> onBackPressed());

        mAuth = FirebaseAuth.getInstance();
        firestore = FirebaseFirestore.getInstance();

        // Check if user is logged in
        if (mAuth.getCurrentUser() != null) {
            uid = mAuth.getCurrentUser().getUid();
        } else {
            Toast.makeText(this, "User not logged in", Toast.LENGTH_SHORT).show();
            finish(); // Close activity if user is not logged in
            return;
        }

        btnSubmit = findViewById(R.id.btnSubmit);
        ratingBar = findViewById(R.id.ratingBar);

        // Listener for the RatingBar
        ratingBar.setOnRatingBarChangeListener((ratingBar, rating, fromUser) -> {
            // You can update the UI or give feedback when the rating changes
        });

        btnSubmit.setOnClickListener(v -> {
            int rating = (int) ratingBar.getRating(); // Get rating as integer

            Map<String, Object> ratingMap = new HashMap<>();
            ratingMap.put("Rating", rating);
            ratingMap.put("UserId", uid);

            // Set the current date
            DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd");
            LocalDateTime now = LocalDateTime.now();
            String date = dtf.format(now);
            ratingMap.put("date", date);

            // Add the rating to Firestore
            firestore.collection("Rating")
                    .document(uid)
                    .set(ratingMap)
                    .addOnSuccessListener(aVoid -> {
                        Toast.makeText(RateUsActivity.this, "Rating submitted successfully", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(RateUsActivity.this, ProfileFragment.class);
                        startActivity(intent);
                        finish();
                    })
                    .addOnFailureListener(e -> {
                        Toast.makeText(RateUsActivity.this, "Error submitting rating: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    });
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
