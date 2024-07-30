package com.example.shoesapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.shoesapp.databinding.ActivitySignupBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class SignupActivity extends AppCompatActivity {
    ActivitySignupBinding binding;
    FirebaseAuth mAuth;
    FirebaseFirestore firestore;
    String name, lname, email, userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySignupBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        mAuth = FirebaseAuth.getInstance();
        firestore = FirebaseFirestore.getInstance();

        binding.signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkValidSignup();
            }
        });

        binding.loginText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SignupActivity.this, LoginActivity.class));
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            }
        });

    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(SignupActivity.this, LoginActivity.class));
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
        super.onBackPressed();
    }

    public void checkValidSignup() {
        if (binding.signupFNameEdt.getText().toString().isEmpty()){
            binding.signupFNameEdt.setError("Username is required");
        } else if (binding.signupLNameEdt.getText().toString().isEmpty()) {
            binding.signupLNameEdt.setError("Last Name is required");
        } else if(binding.signupEmailEdt.getText().toString().isEmpty()){
            binding.signupEmailEdt.setError("Email is required");
        } else if (binding.signupPasswordEdt.getText().toString().isEmpty()){
            binding.signupPasswordEdt.setError("Password is required");
        }else if (binding.signupConfirmPasswordEdt.getText().toString().isEmpty()){
            binding.signupConfirmPasswordEdt.setError("Confirm Password is required");
        }
        else {
            mAuth.createUserWithEmailAndPassword(binding.signupEmailEdt.getText().toString(), binding.signupPasswordEdt.getText().toString())
                    .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                        @Override
                        public void onSuccess(AuthResult authResult) {

                            userId = mAuth.getCurrentUser().getUid();
                            DocumentReference documentReference = firestore.collection("Users").document(userId);

                            name = binding.signupFNameEdt.getText().toString();
                            email = binding.signupEmailEdt.getText().toString();
                            lname = binding.signupLNameEdt.getText().toString();

                            Map<String, Object> user = new HashMap<>();
                            user.put("FirstName", name);
                            user.put("LastName", lname);
                            user.put("Email", email);
                            user.put("UserId", userId);
                            documentReference.set(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void unused) {
                                    startActivity(new Intent(SignupActivity.this, MainActivity.class));
                                    overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                                }
                            });


                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(SignupActivity.this, "Signup Failed...", Toast.LENGTH_SHORT).show();
                        }
                    });






        }
    }
}