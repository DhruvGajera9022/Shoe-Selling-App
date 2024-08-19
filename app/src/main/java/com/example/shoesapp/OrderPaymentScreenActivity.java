package com.example.shoesapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.shoesapp.models.MyCartModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class OrderPaymentScreenActivity extends AppCompatActivity {
    TextView txtPrice, productData;
    Button btnOrder;
    FirebaseFirestore db;
    FirebaseAuth mAuth;
    DocumentReference reference;
    String uid, key, size;
    ArrayList<MyCartModel> list = new ArrayList<>();
    String fname, lname, email, mobile, dob, address, pname, pprice, psize, pimage, pdescription, pcompany, pgender;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_payment_screen);

        txtPrice = findViewById(R.id.OrderProductTotal);
        btnOrder = findViewById(R.id.OrderButton);

        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();

        uid = mAuth.getCurrentUser().getUid();
        reference = db.collection("Users").document(uid);

        key = getIntent().getStringExtra("keyOrder");
        size = getIntent().getStringExtra("size");

        // Fetch user and product data
        fetchDataAndPlaceOrder();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(OrderPaymentScreenActivity.this, OrderConfirmAddressActivity.class);
        intent.putExtra("keyProduct", key);
        intent.putExtra("productSize", size);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
        finish();
    }

    private void fetchDataAndPlaceOrder() {
        getUserData(new DataCallback() {
            @Override
            public void onDataFetched() {
                getProductData(new DataCallback() {
                    @Override
                    public void onDataFetched() {
                        btnOrder.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                placeOrder();
                            }
                        });
                    }
                });
            }
        });
    }

    public void placeOrder() {
        String orderId = db.collection("Orders").document().getId(); // Generate order ID using Orders collection

        Map<String, Object> map = new HashMap<>();
        map.put("productName", pname != null ? pname : "Unknown");
        map.put("productCompanyName", pcompany != null ? pcompany : "Unknown");
        map.put("productPrice", pprice != null ? pprice : "Unknown");
        map.put("productSize", size != null ? size : "Unknown");
        map.put("productImage", pimage != null ? pimage : "Unknown");
        map.put("productDescription", pdescription != null ? pdescription : "Unknown");
        map.put("oid", orderId);
        map.put("uid", uid);
        map.put("userName", fname != null ? fname : "Unknown");
        map.put("email", email != null ? email : "Unknown");
        map.put("number", mobile != null ? mobile : "Unknown");
        map.put("address", address != null ? address : "Unknown");

        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd");
        LocalDateTime now = LocalDateTime.now();
        String date = dtf.format(now);
        map.put("date", date);

        db.collection("Orders")
                .add(map)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Intent intent = new Intent(OrderPaymentScreenActivity.this, OrderConfirmActivity.class);
                        startActivity(intent);
                        finish();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(OrderPaymentScreenActivity.this, "Order placement failed", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    public void getUserData(final DataCallback callback) {
        reference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot value = task.getResult();
                    if (value != null && value.exists()) {
                        fname = value.getString("FirstName");
                        lname = value.getString("LastName");
                        email = value.getString("Email");
                        mobile = value.getString("Mobile");
                        dob = value.getString("DOB");
                        address = value.getString("Address");
                    }
                    callback.onDataFetched();
                }
            }
        });
    }

    public void getProductData(final DataCallback callback) {
        DocumentReference documentReference = db.collection("Products").document(key);

        documentReference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot value = task.getResult();
                    if (value != null && value.exists()) {
                        pname = value.getString("name");
                        pprice = value.getString("price");
                        pdescription = value.getString("description");
                        psize = value.getString("size");
                        pcompany = value.getString("categoryCompany");
                        pgender = value.getString("categoryGender");
                        pimage = value.getString("imgurl");
                    }
                    callback.onDataFetched();
                }
            }
        });
    }

    interface DataCallback {
        void onDataFetched();
    }
}
