package com.example.shoesapp;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import com.example.shoesapp.models.ProductModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

public class ProductDetailsActivity extends AppCompatActivity {

    // UI Components
    private ImageView productImage;
    private TextView productName, productPrice, productDescription;
    private EditText productSizeInput;
    private AppCompatButton addToCartBtn, buyNowBtn;
    private TabLayout sizeTabLayout;

    // Firebase Components
    private FirebaseFirestore db;
    private FirebaseAuth mAuth;

    // Variables
    private String productId, currentImageUrl;
    private String currentUserId;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_details);

        // Initialize UI Components
        initUIComponents();

        // Initialize Firebase Components
        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();
        currentUserId = mAuth.getCurrentUser().getUid();

        // Get product ID from Intent
        productId = getIntent().getStringExtra("key");

        // Load product details from Firestore
        loadProductDetails(productId);

        // Set up tab layout listener for size selection
        setupTabLayoutListener();

        // Set button click listeners
        setupButtonListeners();


    }

    private void initUIComponents() {
        productImage = findViewById(R.id.productImage);
        productName = findViewById(R.id.productName);
        productPrice = findViewById(R.id.productPrice);
        productDescription = findViewById(R.id.productDescription);
        productSizeInput = findViewById(R.id.textSize);
        addToCartBtn = findViewById(R.id.productAddToCartButton);
        buyNowBtn = findViewById(R.id.productBuyNowButton);
        sizeTabLayout = findViewById(R.id.tabLayout);
    }

    private void loadProductDetails(String productId) {
        db.collection("Products").document(productId)
                .get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        if (documentSnapshot.exists()) {
                            ProductModel product = documentSnapshot.toObject(ProductModel.class);
                            if (product != null) {
                                displayProductDetails(product);
                            }
                        } else {
                            Toast.makeText(ProductDetailsActivity.this, "Product not found", Toast.LENGTH_SHORT).show();
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(ProductDetailsActivity.this, "Failed to load product details", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void displayProductDetails(ProductModel product) {
        Picasso.get().load(product.getImgurl()).into(productImage);
        currentImageUrl = product.getImgurl();
        productName.setText(product.getName());
        productPrice.setText("Rs. " + product.getPrice());
        productDescription.setText(product.getDescription());
    }

    private void setupTabLayoutListener() {
        sizeTabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                productSizeInput.setText(tab.getText());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {}

            @Override
            public void onTabReselected(TabLayout.Tab tab) {}
        });
    }

    private void setupButtonListeners() {
        addToCartBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addToCart();
            }
        });

        buyNowBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buyNow();
            }
        });
    }

    private void buyNow() {
        Intent intent = new Intent(ProductDetailsActivity.this, OrderConfirmAddressActivity.class);
        intent.putExtra("keyProduct", productId);
        intent.putExtra("productSize", productSizeInput.getText().toString());
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
        startActivity(intent);
        finish();
    }

    private void addToCart() {
        Calendar calForDate = Calendar.getInstance();

        SimpleDateFormat currentDate = new SimpleDateFormat("MM/dd/yyyy");
        SimpleDateFormat currentTime = new SimpleDateFormat("HH:mm:ss a");
        String saveCurrentDate = currentDate.format(calForDate.getTime());
        String saveCurrentTime = currentTime.format(calForDate.getTime());

        HashMap<String, Object> cartMap = new HashMap<>();
        cartMap.put("productImage", currentImageUrl);
        cartMap.put("productName", productName.getText().toString());
        cartMap.put("productPrice", productPrice.getText().toString());
        cartMap.put("productDescription", productDescription.getText().toString());
        cartMap.put("productSize", productSizeInput.getText().toString());
        cartMap.put("currentDate", saveCurrentDate);
        cartMap.put("currentTime", saveCurrentTime);
        cartMap.put("uid", currentUserId);

        String orderId = db.collection("AddToCart").document().getId();
        cartMap.put("oid", orderId);

        db.collection("AddToCart")
                .document(orderId)
                .set(cartMap)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(ProductDetailsActivity.this, "Added To Cart", Toast.LENGTH_SHORT).show();
                            finish();
                        } else {
                            Toast.makeText(ProductDetailsActivity.this, "Failed to add to cart", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}
