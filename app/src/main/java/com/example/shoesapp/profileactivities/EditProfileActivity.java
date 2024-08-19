package com.example.shoesapp.profileactivities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.icu.text.MessageFormat;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import com.example.shoesapp.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.datepicker.MaterialPickerOnPositiveButtonClickListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class EditProfileActivity extends AppCompatActivity {
    EditText txtFName, txtLName, txtEmail, txtMobile, txtDOB, txtAddress;
    ImageView imgProfile;
    FirebaseAuth mAuth;
    FirebaseFirestore firestore;
    StorageReference updateStorageReference;
    AppCompatButton btnEdit;
    String uid, date;
    Uri imgUpdateUri;
    ProgressDialog progressDialog;
    boolean isImageSelected = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        firestore = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();

        progressDialog = new ProgressDialog(this);

        txtFName = findViewById(R.id.txtEditProfileName);
        txtLName = findViewById(R.id.txtEditProfileLastName);
        txtEmail = findViewById(R.id.txtEditProfileEmail);
        txtMobile = findViewById(R.id.txtEditProfileNumber);
        txtDOB = findViewById(R.id.txtEditProfileDOB);
        txtAddress = findViewById(R.id.txtEditProfileAddress);
        imgProfile = findViewById(R.id.editProfileImage);
        btnEdit = findViewById(R.id.editProfileBtn);

        uid = mAuth.getCurrentUser().getUid();

        DocumentReference reference = firestore.collection("Users").document(uid);

        reference.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                if (value != null && value.exists()) {
                    txtFName.setText(value.getString("FirstName"));
                    txtLName.setText(value.getString("LastName"));
                    txtEmail.setText(value.getString("Email"));
                    txtMobile.setText(value.getString("Mobile"));
                    txtDOB.setText(value.getString("DOB"));
                    txtAddress.setText(value.getString("Address"));
                    Picasso.get().load(value.getString("ProfileImage")).into(imgProfile);
                }
            }
        });

        imgProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(intent, 100);
            }
        });

        btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateUserData();
            }
        });

        txtDOB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Create a MaterialDatePicker instance
                MaterialDatePicker<Long> datePicker = MaterialDatePicker.Builder.datePicker()
                        .setTitleText("Select Date of Birth")
                        .setSelection(MaterialDatePicker.todayInUtcMilliseconds())
                        .build();

                // Set a listener for the positive button click event
                datePicker.addOnPositiveButtonClickListener(new MaterialPickerOnPositiveButtonClickListener<Long>() {
                    @Override
                    public void onPositiveButtonClick(Long selection) {
                        // Format the selected date and set it in the TextView
                        date = new SimpleDateFormat("MM-dd-yyyy", Locale.getDefault()).format(new Date(selection));
                        txtDOB.setText(MessageFormat.format("{0}", date));
                    }
                });

                // Show the date picker
                datePicker.show(getSupportFragmentManager(), "tag");
            }
        });

    }

    public void updateUserData() {
        progressDialog.setMessage("Updating Profile...");
        progressDialog.show();

        if (isImageSelected) {
            uploadImageAndSaveData();
        } else {
            saveUserData(null);
        }
    }

    private void uploadImageAndSaveData() {
        SimpleDateFormat format = new SimpleDateFormat("yyyy_MM_dd_HH_mm_ss", Locale.UK);
        Date now = new Date();
        String filename = format.format(now);

        updateStorageReference = FirebaseStorage.getInstance().getReference(filename);

        updateStorageReference.putFile(imgUpdateUri)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        updateStorageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                saveUserData(uri.toString());
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                progressDialog.dismiss();
                                Toast.makeText(EditProfileActivity.this, "Failed to get image URL", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        progressDialog.dismiss();
                        Toast.makeText(EditProfileActivity.this, "Image upload failed", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void saveUserData(@Nullable String imageUrl) {
        String mfname = txtFName.getText().toString();
        String mlname = txtLName.getText().toString();
        String memail = txtEmail.getText().toString();
        String mnumber = txtMobile.getText().toString();
        String mdob = txtDOB.getText().toString();
        String maddress = txtAddress.getText().toString();

        Map<String, Object> map = new HashMap<>();
        map.put("FirstName", mfname);
        map.put("LastName", mlname);
        map.put("Email", memail);
        map.put("Mobile", mnumber);
        map.put("DOB", mdob);
        map.put("Address", maddress);
        if (imageUrl != null) {
            map.put("ProfileImage", imageUrl);
        }

        firestore.collection("Users")
                .document(uid)
                .update(map)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        progressDialog.dismiss();
                        Toast.makeText(EditProfileActivity.this, "Profile updated", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        progressDialog.dismiss();
                        Toast.makeText(EditProfileActivity.this, "Profile update failed", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 100 && resultCode == RESULT_OK && data != null && data.getData() != null) {
            imgUpdateUri = data.getData();
            imgProfile.setImageURI(imgUpdateUri);
            isImageSelected = true;
        }
    }
}
