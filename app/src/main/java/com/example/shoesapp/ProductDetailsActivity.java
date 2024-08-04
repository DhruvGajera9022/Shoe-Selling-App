package com.example.shoesapp;


import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.shoesapp.models.ProductModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

public class ProductDetailsActivity extends AppCompatActivity {
    ImageView imgi, img;
    String key, productDetailsKey,cur_image, txtSize, txtName, txtPrice, txtGender, currentUser;
    FirebaseFirestore db;
    FirebaseAuth mAuth;
    MaterialButton buyNowBtn;
    TextView name, price, desc, gender;
    EditText edtSize;
    TabLayout tabSize;
    ArrayList<ProductModel> datalist;
    ImageButton addToCartBtn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_details);

        key = getIntent().getStringExtra("key");

        img = findViewById(R.id.productImage);
        name = findViewById(R.id.productName);
        price = findViewById(R.id.productPrice);
        desc = findViewById(R.id.productDescription);
        gender = findViewById(R.id.productFor);
        edtSize = findViewById(R.id.textSize);
        tabSize = findViewById(R.id.tabLayout);
        addToCartBtn = findViewById(R.id.productAddToCartButton);
        buyNowBtn = findViewById(R.id.productBuyNowButton);

        imgi = img;

        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();

        tabSize.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                switch (tab.getPosition()){
                    case 0:
                        edtSize.setText("6");
                        return;
                    case 1:
                        edtSize.setText("7");
                        return;
                    case 2:
                        edtSize.setText("8");
                        return;
                    case 3:
                        edtSize.setText("9");
                        return;
                    case 4:
                        edtSize.setText("10");
                        return;
                    case 5:
                        edtSize.setText("11");
                        return;
                    case 6:
                        edtSize.setText("12");
                        return;
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        db.collection("Products").document(key)
                .get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        ProductModel singledata = documentSnapshot.toObject(ProductModel.class);

                        Picasso.get().load(singledata.getImgurl()).into(imgi);
                        cur_image = singledata.getImgurl();
                        name.setText(singledata.getName());
                        price.append("Rs. " + singledata.getPrice());
                        gender.setText(singledata.getCategoryGender()+"'s Shoe");
                        desc.setText(singledata.getDescription());
                    }
                });
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

    public void addToCart(){
        String saveCurrentData, saveCurrentTime;
        Calendar calForDate = Calendar.getInstance();

        SimpleDateFormat currentDate = new SimpleDateFormat("MM/dd/yyyy");
        saveCurrentData = currentDate.format(calForDate.getTime());

        SimpleDateFormat currentTime = new SimpleDateFormat("HH:mm:ss a");
        saveCurrentTime = currentTime.format(calForDate.getTime());

        currentUser = mAuth.getCurrentUser().getUid();

        HashMap<String, Object> cartMap = new HashMap<>();

        cartMap.put("productImage", cur_image);
        cartMap.put("productName", name.getText().toString());
        cartMap.put("productPrice", price.getText().toString());
        cartMap.put("productDescription", desc.getText().toString());
        cartMap.put("productSize", edtSize.getText().toString());
        cartMap.put("currentData", saveCurrentData);
        cartMap.put("currentTime", saveCurrentTime);
        cartMap.put("uid", currentUser);

        String oid = db.collection("AddToCart").document().getId();
        cartMap.put("oid",oid);

        db.collection("AddToCart")
                .document(oid)
                .set(cartMap)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        Toast.makeText(ProductDetailsActivity.this, "Added To Cart", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                });


    }

    public void buyNow(){
        txtName = name.getText().toString();
        txtPrice = price.getText().toString();
        txtSize = edtSize.getText().toString();
        txtGender = gender.getText().toString();
        Intent intent = new Intent(ProductDetailsActivity.this, OrderDetailsActivity.class);
        intent.putExtra("pname", txtName);
        intent.putExtra("pprice", txtPrice);
        intent.putExtra("psize", txtSize);
        intent.putExtra("gender", txtGender);
        intent.putExtra("imgUrl", cur_image);
        startActivity(intent);
        finish();
    }

//    private void getamount(int q) {
//        int pr = Integer.parseInt(price.getText().toString());
//        int am = q * pr;
//        String sam = String.valueOf(am);
//        cartamount.setText(sam);
//        map.put("amount",sam);
//    }

}