package com.example.shoesapp;

import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import com.example.shoesapp.databinding.ActivityLoginBinding;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity {
    ActivityLoginBinding binding;
    FirebaseAuth mAuth;
    boolean passwordShowing = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        mAuth = FirebaseAuth.getInstance();

        if (mAuth.getCurrentUser() != null){
            startActivity(new Intent(LoginActivity.this, MainActivity.class));
            finish();
        }

        // For login user
        binding.loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkValidLogin();
            }
        });

        // For signup activity
        binding.signUpText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this, SignupActivity.class));
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
            }
        });

        // For forgot password activity
        binding.loginForgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this, ForgotPasswordActivity.class));
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
            }
        });
        binding.passwordToggle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (passwordShowing){
                    passwordShowing = false;

                    binding.loginPasswordEdt.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                    binding.passwordToggle.setImageResource(R.drawable.show_password);
                }else{
                    passwordShowing = true;

                    binding.loginPasswordEdt.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                    binding.passwordToggle.setImageResource(R.drawable.hide_password);
                }
            }
        });

    }

    public void checkValidLogin() {
        if (binding.loginEmailEdt.getText().toString().isEmpty()){
            binding.loginEmailEdt.setError("Email is required");
        } else if (binding.loginPasswordEdt.getText().toString().isEmpty()){
            binding.loginPasswordEdt.setError("Password is required");
        }
        else {
            progress();
            mAuth.signInWithEmailAndPassword(binding.loginEmailEdt.getText().toString(), binding.loginPasswordEdt.getText().toString())
                    .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                        @Override
                        public void onSuccess(AuthResult authResult) {
                            startActivity(new Intent(LoginActivity.this, MainActivity.class));
                            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                            finish();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            binding.loginButton.setVisibility(View.VISIBLE);
                            binding.progressbar.setVisibility(View.GONE);
                            Toast.makeText(LoginActivity.this, "Login Failed...", Toast.LENGTH_SHORT).show();
                        }
                    });
        }
    }

    public void progress(){
        if (binding.loginButton.isPressed()){
            binding.loginButton.setVisibility(View.GONE);
            binding.progressbar.setVisibility(View.VISIBLE);
        }else {
            binding.loginButton.setVisibility(View.VISIBLE);
            binding.progressbar.setVisibility(View.GONE);
        }
    }

}