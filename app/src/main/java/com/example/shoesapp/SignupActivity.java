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

public class SignupActivity extends AppCompatActivity {
    ActivitySignupBinding binding;
    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySignupBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        mAuth = FirebaseAuth.getInstance();

        binding.signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkValidSignup();
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
        if (binding.signupUsernameEdt.getText().toString().isEmpty()){
            binding.signupUsernameEdt.setError("Username is required");
        }
        else if(binding.signupEmailEdt.getText().toString().isEmpty()){
            binding.signupEmailEdt.setError("Email is required");
        } else if (binding.signupPasswordEdt.getText().toString().isEmpty()){
            binding.signupPasswordEdt.setError("Password is required");
        }else if (binding.signupConfirmPassowrdEdt.getText().toString().isEmpty()){
            binding.signupConfirmPassowrdEdt.setError("Confirm Password is required");
        }
        else {
            mAuth.createUserWithEmailAndPassword(binding.signupEmailEdt.getText().toString(), binding.signupPasswordEdt.getText().toString())
                    .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                        @Override
                        public void onSuccess(AuthResult authResult) {
                            startActivity(new Intent(SignupActivity.this, MainActivity.class));
                            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
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