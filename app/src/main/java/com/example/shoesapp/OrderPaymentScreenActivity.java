package com.example.shoesapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
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
    TextView txtPrice, productData, txtSubTotal;
    Button btnOrder;
    FirebaseFirestore db;
    FirebaseAuth mAuth;
    DocumentReference reference;
    String uid, key, size;
    ArrayList<MyCartModel> list = new ArrayList<>();
    String fname, lname, email, mobile, dob, address, pname, pprice, psize, pimage, pdescription, pcompany, pgender, strPaymentMethod;
    RadioButton rbDebitCreditCard, rbGPay, rbPhonePay, rbNetBanking, rbWallets, rbCashOnDelivery;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_payment_screen);

        txtSubTotal = findViewById(R.id.txtSubTotal);
        btnOrder = findViewById(R.id.OrderButton);

        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();

        uid = mAuth.getCurrentUser().getUid();
        reference = db.collection("Users").document(uid);

        key = getIntent().getStringExtra("keyOrder");
        psize = getIntent().getStringExtra("size");

        rbDebitCreditCard = findViewById(R.id.rbDebitCreditCard);
        rbGPay = findViewById(R.id.rbGPay);
        rbPhonePay = findViewById(R.id.rbPhonePay);
        rbNetBanking = findViewById(R.id.rbNetBanking);
        rbWallets = findViewById(R.id.rbWallets);
        rbCashOnDelivery = findViewById(R.id.rbCashOnDelivery);

        // First fetch user and product data before setting the button click
        fetchDataAndPlaceOrder();
    }

    private void fetchDataAndPlaceOrder() {
        getUserData(new DataCallback() {
            @Override
            public void onDataFetched() {
                getProductData(new DataCallback() {
                    @Override
                    public void onDataFetched() {
                        // After both user and product data are fetched, set the button click listener
                        btnOrder.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                getPaymentMethod();  // Get payment method selected
                                placeOrder();  // Place order
                            }
                        });
                    }
                });
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
                    callback.onDataFetched();  // Notify when data is fetched
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
                        pcompany = value.getString("categoryCompany");
                        pgender = value.getString("categoryGender");
                        pimage = value.getString("imgurl");

                        txtSubTotal.append(" Rs." + pprice);  // Update UI with price
                    }
                    callback.onDataFetched();  // Notify when data is fetched
                }
            }
        });
    }

    public void getPaymentMethod() {
        if (rbDebitCreditCard.isChecked()) {
            strPaymentMethod = rbDebitCreditCard.getText().toString();
        } else if (rbGPay.isChecked()) {
            strPaymentMethod = rbGPay.getText().toString();
        } else if (rbPhonePay.isChecked()) {
            strPaymentMethod = rbPhonePay.getText().toString();
        } else if (rbNetBanking.isChecked()) {
            strPaymentMethod = rbNetBanking.getText().toString();
        } else if (rbWallets.isChecked()) {
            strPaymentMethod = rbWallets.getText().toString();
        } else {
            strPaymentMethod = rbCashOnDelivery.getText().toString();
        }
    }

    public void placeOrder() {
        if (fname == null || pname == null || pprice == null) {
            Toast.makeText(OrderPaymentScreenActivity.this, "Data not loaded", Toast.LENGTH_SHORT).show();
            return;
        }

        String oid = db.collection("Orders").document().getId();
        Map<String, Object> mapOrder = new HashMap<>();

        mapOrder.put("productName", pname);
        mapOrder.put("productCompanyName", pcompany);
        mapOrder.put("productPrice", pprice);
        mapOrder.put("productSize", psize);
        mapOrder.put("productImage", pimage);
        mapOrder.put("oid", oid);
        mapOrder.put("productDescription", pdescription);
        mapOrder.put("uid", uid);
        mapOrder.put("userName", fname);
        mapOrder.put("email", email);
        mapOrder.put("number", mobile);
        mapOrder.put("address", address);
        mapOrder.put("paymentMethod", strPaymentMethod);

        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd");
        LocalDateTime now = LocalDateTime.now();
        String date = dtf.format(now);
        mapOrder.put("date", date);

        db.collection("Orders")
                .document(oid)  // Using 'oid' as the document ID
                .set(mapOrder)   // Use set() when you have a specific ID for the document
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
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

    interface DataCallback {
        void onDataFetched();
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
}
