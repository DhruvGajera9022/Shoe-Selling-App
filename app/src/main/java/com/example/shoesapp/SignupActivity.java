package com.example.shoesapp;

import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.shoesapp.databinding.ActivitySignupBinding;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class SignupActivity extends AppCompatActivity {
    ActivitySignupBinding binding;
    FirebaseAuth mAuth;
    FirebaseFirestore firestore;
    String name, lname, email, userId;
    GoogleSignInOptions gso;
    GoogleSignInClient gsc;
    RelativeLayout googleButton;
    boolean passwordShowing = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySignupBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        mAuth = FirebaseAuth.getInstance();
        firestore = FirebaseFirestore.getInstance();

        gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().build();
        gsc = GoogleSignIn.getClient(this, gso);

        googleButton = findViewById(R.id.googleButton);

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

        googleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                googleSignin();
            }
        });

        binding.passwordToggle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (passwordShowing){
                    passwordShowing = false;

                    binding.signupPasswordEdt.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                    binding.passwordToggle.setImageResource(R.drawable.show_password);
                }else{
                    passwordShowing = true;

                    binding.signupPasswordEdt.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                    binding.passwordToggle.setImageResource(R.drawable.hide_password);
                }
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
            progress();
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
                                    startActivity(new Intent(SignupActivity.this, LoginActivity.class));
                                    overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                                }
                            });


                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            binding.signupButton.setVisibility(View.VISIBLE);
                            binding.progressbar.setVisibility(View.GONE);
                            Toast.makeText(SignupActivity.this, "Signup Failed...", Toast.LENGTH_SHORT).show();
                        }
                    });

        }
    }

    public void progress(){
        if (binding.signupButton.isPressed()){
            binding.signupButton.setVisibility(View.GONE);
            binding.progressbar.setVisibility(View.VISIBLE);
        }else {
            binding.signupButton.setVisibility(View.VISIBLE);
            binding.progressbar.setVisibility(View.GONE);
        }
    }

    public void googleSignin(){
        Intent signInIntent = gsc.getSignInIntent();
        startActivityForResult(signInIntent, 1000);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1000){
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);

            try {
                task.getResult(ApiException.class);
                navigateToSecondActivity();
            }catch (ApiException e){
                Toast.makeText(this, "Something went wrong", Toast.LENGTH_SHORT).show();
            }
        }
    }

    void navigateToSecondActivity(){
        finish();
        Intent intent = new Intent(SignupActivity.this, MainActivity.class);
        startActivity(intent);
    }

}