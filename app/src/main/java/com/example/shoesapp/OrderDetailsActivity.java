package com.example.shoesapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

public class OrderDetailsActivity extends AppCompatActivity {
    AppCompatButton btnBuy, btnCancel;
    TextView pName, pPrice, pSize, pGender, pFor;
    String strName, strPrice, strSize, strGender, imgUrl, currentUser;
    FirebaseFirestore db;
    FirebaseAuth mAuth;
    ImageView img, imgi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_details);

        btnBuy = findViewById(R.id.orderProductBuyButton);
        btnCancel = findViewById(R.id.orderProductCancelButton);
        pName = findViewById(R.id.orderedProductName);
        pPrice = findViewById(R.id.orderedProductPrice);
        pGender = findViewById(R.id.orderedProductGender);
        pSize = findViewById(R.id.orderedProductSize);
        img = findViewById(R.id.orderProductImage);

        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();

        imgi = img;

        strName = getIntent().getStringExtra("pname");
        strPrice = getIntent().getStringExtra("pprice");
        strSize = getIntent().getStringExtra("psize");
        strGender = getIntent().getStringExtra("gender");
        imgUrl = getIntent().getStringExtra("imgUrl");

        Picasso.get().load(imgUrl).into(imgi);
        pName.setText(strName);
        pPrice.setText(strPrice);
        pSize.setText(strSize);
        pGender.setText(strGender);

        btnBuy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buyNow();
            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(OrderDetailsActivity.this, MainActivity.class));
            }
        });

    }

    public void buyNow(){
        String saveCurrentDate, saveCurrentTime;
        Calendar calForDate = Calendar.getInstance();

        SimpleDateFormat currentDate = new SimpleDateFormat("MM/dd/yyyy");
        saveCurrentDate = currentDate.format(calForDate.getTime());

        SimpleDateFormat currentTime = new SimpleDateFormat("HH:mm:ss a");
        saveCurrentTime = currentTime.format(calForDate.getTime());

        currentUser = mAuth.getCurrentUser().getUid();

        HashMap<String, Object> cartMap = new HashMap<>();

        cartMap.put("orderProductName", pName.getText().toString());
        cartMap.put("orderProductPrice", pPrice.getText().toString());
        cartMap.put("orderProductSize", pSize.getText().toString());
        cartMap.put("orderProductImage", imgUrl.toString());
        cartMap.put("currentOrderDate", saveCurrentDate);
        cartMap.put("currentOrderTime", saveCurrentTime);
        cartMap.put("uid", currentUser);

        String oid = db.collection("Orders").document().getId();
        cartMap.put("oid",oid);


        db.collection("Orders")
                .document(oid)
                .set(cartMap)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                startActivity(new Intent(OrderDetailsActivity.this, OrderConfirmActivity.class));
                                finish();
                            }
                        });


    }

}