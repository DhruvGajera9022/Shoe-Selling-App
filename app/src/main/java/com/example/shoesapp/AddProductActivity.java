package com.example.shoesapp;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.shoesapp.databinding.ActivityAddProductBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class AddProductActivity extends AppCompatActivity {
    ActivityAddProductBinding binding;
    Uri imguri;
    String selectedCategory;
    FirebaseFirestore firestore;
    StorageReference storageReference,imageName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAddProductBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        firestore = FirebaseFirestore.getInstance();

        String[] category = {"Male","Female","Kids"};
        ArrayAdapter<String> arrayAdapter;
        arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item,category);
        binding.addProductCategory.setAdapter(arrayAdapter);

        binding.addProductImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(intent,100);
            }
        });

        binding.addProductCategory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedCategory = parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        binding.productSaveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ProgressDialog progressDialog = new ProgressDialog(AddProductActivity.this);
                progressDialog.setTitle("Wait While Data Inserting...");
                progressDialog.show();

                SimpleDateFormat format = new SimpleDateFormat("yyyy_MM_dd_HH_mm_ss", Locale.UK);
                Date now = new Date();
                String filename;
                filename = format.format(now);

                storageReference = FirebaseStorage.getInstance().getReference(filename);
                imageName = storageReference;

                storageReference.putFile(imguri)
                        .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                imageName.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                    @Override
                                    public void onSuccess(Uri uri) {
                                        String fiuri = uri.toString();
                                        String pName = binding.addNewProductName.getText().toString();
                                        String pPrice = binding.addNewProductPrice.getText().toString();
                                        String pDesc = binding.addNewProductDescription.getText().toString();
                                        String pCategory = selectedCategory;
                                        String uid = firestore.collection("Products").document().getId();

                                        Map<String,Object> map = new HashMap<>();
                                        map.put("name",pName);
                                        map.put("price",pPrice);
                                        map.put("description",pDesc);
                                        map.put("category",pCategory);
                                        map.put("imgurl",fiuri);
                                        map.put("pid",uid);

                                        firestore.collection("Products")
                                                .document(uid)
                                                .set(map)
                                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                    @Override
                                                    public void onSuccess(Void unused) {
                                                        progressDialog.dismiss();

                                                        binding.addNewProductName.setText("");
                                                        binding.addNewProductPrice.setText("");
                                                        binding.addNewProductDescription.setText("");
                                                        binding.addProductImage.setImageResource(R.drawable.image_icon);

                                                        Intent intent = new Intent(AddProductActivity.this, AdminHomeFragment.class);
                                                        startActivity(intent);
                                                    }
                                                });

                                    }
                                });
                            }
                        });
            }
        });

        binding.productCancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                binding.addNewProductName.setText("");
                binding.addNewProductPrice.setText("");
                binding.addNewProductDescription.setText("");
                binding.addProductImage.setImageResource(R.drawable.image_icon);
            }
        });

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 100 && data != null && data.getData() != null) {
            imguri = data.getData();
            binding.addProductImage.setImageURI(imguri);
        }
    }

}
