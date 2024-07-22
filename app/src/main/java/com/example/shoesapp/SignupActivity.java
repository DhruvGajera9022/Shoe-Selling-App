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
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class SignupActivity extends AppCompatActivity {
    ActivitySignupBinding binding;
    FirebaseAuth mAuth;
    FirebaseFirestore firestore;

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

            String uid = firestore.collection("Users").document().getId();

            Map<String, Object> map = new HashMap<>();
            map.put("name", binding.signupUsernameEdt.getText().toString());
            map.put("email", binding.signupEmailEdt.getText().toString());
            map.put("uid", uid);

            firestore.collection("Users")
                    .document(uid)
                    .set(map)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {

                            binding.signupUsernameEdt.setText("");
                            binding.signupEmailEdt.setText("");
                            binding.signupPasswordEdt.setText("");
                            binding.signupConfirmPassowrdEdt.setText("");
                        }
                    });

        }
    }
}