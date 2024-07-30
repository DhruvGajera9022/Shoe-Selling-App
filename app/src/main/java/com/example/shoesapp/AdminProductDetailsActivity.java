package com.example.shoesapp;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.shoesapp.models.ProductModel;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class AdminProductDetailsActivity extends AppCompatActivity {
    ImageView img;
    FirebaseFirestore db;
    boolean flag = true;
    String key, cur_image;
    Uri img_upd_uri;
    StorageReference imageName, updstorageref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_admin_product_details);

        ProgressDialog progressDialog = new ProgressDialog(AdminProductDetailsActivity.this);
        progressDialog.setTitle("Wait While Updating Details...");
        progressDialog.setMessage("Updating...");
        progressDialog.setMax(100);
        progressDialog.create();

        key = getIntent().getStringExtra("key");

        ImageView updimg = findViewById(R.id.ProductDImage);
        EditText updname = findViewById(R.id.ProductDName);
        EditText updprice = findViewById(R.id.ProductDPrice);
        EditText upddesc = findViewById(R.id.ProductDDescription);
        Button update = findViewById(R.id.UpdateDButton);
        Button cancel = findViewById(R.id.CancelDButton);

        img = updimg;

        db = FirebaseFirestore.getInstance();

        db.collection("Products").document(key)
                .get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        ProductModel singledata = documentSnapshot.toObject(ProductModel.class);

                        Picasso.get().load(singledata.getImgurl()).into(updimg);
                        cur_image = singledata.getImgurl();
                        updname.setText(singledata.getName());
                        updprice.setText(singledata.getPrice());
                        upddesc.setText(singledata.getDescription());
                    }
                });

        updimg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(intent, 10);
            }
        });

        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                progressDialog.show();

                SimpleDateFormat format = new SimpleDateFormat("yyyy_MM_dd_HH_mm_ss", Locale.UK);
                Date now = new Date();
                String filename;
                filename = format.format(now);

                updstorageref = FirebaseStorage.getInstance().getReference(filename);
                imageName = updstorageref;

                if (flag) {
                    String mname = updname.getText().toString();
                    String mprice = updprice.getText().toString();
                    String mdesc = upddesc.getText().toString();

                    Map<String, Object> map = new HashMap<>();
                    map.put("name", mname);
                    map.put("price", mprice);
                    map.put("description", mdesc);

                    db.collection("Products")
                            .document(key)
                            .update(map)
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void unused) {

                                    progressDialog.dismiss();

                                    updname.setText("");
                                    updprice.setText("");
                                    upddesc.setText("");
                                    updimg.setImageDrawable(null);

                                    finish();
                                }
                            });

                } else {

                    updstorageref.putFile(img_upd_uri)
                            .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                @Override
                                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                                    imageName.getDownloadUrl()
                                            .addOnSuccessListener(new OnSuccessListener<Uri>() {
                                                @Override
                                                public void onSuccess(Uri uri) {

                                                    if (!uri.toString().equals(cur_image)) {
                                                        String fiuri = uri.toString();
                                                        String mname = updname.getText().toString();
                                                        String mprice = updprice.getText().toString();
                                                        String mdesc = upddesc.getText().toString();

                                                        Map<String, Object> map = new HashMap<>();
                                                        map.put("name", mname);
                                                        map.put("price", mprice);
                                                        map.put("description", mdesc);
                                                        map.put("imgurl", fiuri);

                                                        db.collection("Products")
                                                                .document(key)
                                                                .update(map)
                                                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                    @Override
                                                                    public void onSuccess(Void unused) {

                                                                        progressDialog.dismiss();

                                                                        updname.setText("");
                                                                        updprice.setText("");
                                                                        upddesc.setText("");
                                                                        updimg.setImageDrawable(null);

                                                                        finish();
                                                                    }
                                                                });

                                                    }


                                                }
                                            });

                                }
                            });
                }
            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 10 && data != null && data.getData() != null) {
            img_upd_uri = data.getData();
            img.setImageURI(img_upd_uri);
            flag = false;
        }
    }
}