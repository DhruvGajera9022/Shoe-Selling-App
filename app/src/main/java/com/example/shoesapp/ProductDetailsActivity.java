package com.example.shoesapp;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.shoesapp.models.ProductModel;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Picasso;

public class ProductDetailsActivity extends AppCompatActivity {
    ImageView imgi;
    String key, cur_image;
    FirebaseFirestore db;
    MaterialButton addToCartBtn, buyNowBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_details);

        key = getIntent().getStringExtra("key");

        ImageView img = findViewById(R.id.productImage);
        TextView name = findViewById(R.id.productName);
        TextView price = findViewById(R.id.productPrice);
        TextView desc = findViewById(R.id.productDescription);
        addToCartBtn = findViewById(R.id.productAddToCartButton);
        buyNowBtn = findViewById(R.id.productBuyNowButton);

        imgi = img;

        db = FirebaseFirestore.getInstance();

        db.collection("Products").document(key)
                .get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        ProductModel singledata = documentSnapshot.toObject(ProductModel.class);

                        Picasso.get().load(singledata.getImgurl()).into(imgi);
                        cur_image = singledata.getImgurl();
                        name.setText(singledata.getName());
                        price.setText(singledata.getPrice());
                        desc.setText(singledata.getDescription());
                    }
                });
        addToCartBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(ProductDetailsActivity.this, "Added to Cart", Toast.LENGTH_SHORT).show();
            }
        });
        
        buyNowBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(ProductDetailsActivity.this, "Buy Now Processing...", Toast.LENGTH_SHORT).show();
            }
        });
    

    }
}