package com.example.shoesapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.shoesapp.profileactivities.EditProfileActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

public class OrderConfirmAddressActivity extends AppCompatActivity {
    TextView name, address, number, edit;
    String key, uid, size;
    FirebaseFirestore db;
    FirebaseAuth mAuth;
    Button btnDeliever;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_confirm_address);

//        textView = findViewById(R.id.TEXTVIEW);

        name = findViewById(R.id.recipient_name);
        address = findViewById(R.id.address_details);
        number = findViewById(R.id.phone_number);
        edit = findViewById(R.id.edit_address);
        btnDeliever = findViewById(R.id.deliver_button);

        key = getIntent().getStringExtra("keyProduct");
        size = getIntent().getStringExtra("productSize");

        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();

        uid = mAuth.getCurrentUser().getUid();

        DocumentReference reference = db.collection("Users").document(uid);

        reference.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                if (value != null && value.exists()){
                    name.setText(value.getString("FirstName") + " " + value.getString("LastName"));
                    address.setText(value.getString("Address"));
                    number.setText(value.getString("Mobile"));
                }
            }
        });

        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(OrderConfirmAddressActivity.this, EditProfileActivity.class));
            }
        });

        btnDeliever.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(OrderConfirmAddressActivity.this, OrderPaymentScreenActivity.class);
                intent.putExtra("keyOrder", key);
                intent.putExtra("size", size);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                finish();
            }
        });

    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(OrderConfirmAddressActivity.this, ProductDetailsActivity.class);
        intent.putExtra("key", key); // Passing the product ID back
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
        startActivity(intent);
        finish(); // Finish the current activity to prevent stacking
    }
}
