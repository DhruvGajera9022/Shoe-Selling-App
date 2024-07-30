package com.example.shoesapp.profileactivities;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.shoesapp.MainActivity;
import com.example.shoesapp.R;
import com.example.shoesapp.SignupActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

import java.util.HashMap;
import java.util.Map;

public class EditProfileActivity extends AppCompatActivity {
    EditText txtFName, txtLName, txtEmail, txtMobile;
    ImageView imgProfile;
    FirebaseAuth mAuth;
    FirebaseFirestore firestore;
    MaterialButton btnEdit;
    String strfname, strlname, strnumber, stremail, uid;
    Boolean flag = true;
    Uri imguri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        firestore = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();

        txtFName = findViewById(R.id.txtEditProfileName);
        txtLName = findViewById(R.id.txtEditProfileLastName);
        txtEmail = findViewById(R.id.txtEditProfileEmail);
        txtMobile = findViewById(R.id.txtEditProfileNumber);
        imgProfile = findViewById(R.id.editProfileImage);
        btnEdit = findViewById(R.id.editProfileBtn);

        strfname = txtFName.getText().toString();
        strlname = txtLName.getText().toString();
        strnumber = txtMobile.getText().toString();
        stremail = txtEmail.getText().toString();

        uid = mAuth.getCurrentUser().getUid();

        DocumentReference reference = firestore.collection("Users").document(uid);

        reference.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                txtFName.setText(value.getString("FirstName"));
                txtEmail.setText(value.getString("Email"));
                txtLName.setText(value.getString("LastName"));
            }
        });

        imgProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(intent,100);
            }
        });

        btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                updateUserData();
            }
        });

    }

    public void updateUserData(){

            if (flag){
                DocumentReference documentReference = firestore.collection("Users").document(uid);

                Map<String, Object> user = new HashMap<>();
                user.put("FirstName", strfname);
                user.put("LastName", strlname);
                user.put("Email", stremail);
                user.put("Mobile", strnumber);
                user.put("UserId", uid);
                documentReference.update(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Toast.makeText(EditProfileActivity.this, "Updated Successfully", Toast.LENGTH_SHORT).show();
                    }
                });
            }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 100 && data != null && data.getData() != null) {
            imguri = data.getData();
            imgProfile.setImageURI(imguri);
        }
    }

}