package com.example.shoesapp;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.shoesapp.models.OrderModel;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class ConfirmOrderDetailsActivity extends AppCompatActivity {
    ImageView img, imgi;
    String key, cur_image, strDate;
    FirebaseFirestore db;
    FirebaseAuth mAuth;
    TextView pname, pprice, psize, pgender, pdesc, email, number, address, date, deliveredDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm_order_details);

        key = getIntent().getStringExtra("key");

        pname = findViewById(R.id.confirmOrderProductName);
        pprice = findViewById(R.id.confirmOrderProductPrice);
        psize = findViewById(R.id.confirmOrderProductSize);
        pgender = findViewById(R.id.confirmOrderProductPFor);
        pdesc = findViewById(R.id.confirmOrderProductOrderDescription);
        email = findViewById(R.id.confirmOrderProductOrderEmail);
        number = findViewById(R.id.confirmOrderProductOrderNumber);
        address = findViewById(R.id.confirmOrderProductOrderAddress);
        date = findViewById(R.id.confirmOrderProductOrderDate);
        deliveredDate = findViewById(R.id.confirmOrderProductArrivingDate);

        img = findViewById(R.id.productImage);  // Initialize the ImageView
        imgi = img;

        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();

        db.collection("ConfirmOrder").document(key)
                .get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        if (documentSnapshot.exists()) {
                            OrderModel singledata = documentSnapshot.toObject(OrderModel.class);

                            if (singledata != null) {
                                Picasso.get().load(singledata.getProductImage()).into(imgi);
                                cur_image = singledata.getProductImage();
                                pname.setText(singledata.getProductName());
                                pprice.setText(singledata.getProductPrice());
                                pdesc.setText(singledata.getProductDescription());
                                psize.setText("Size. " + singledata.getProductSize());
                                email.setText(singledata.getEmail());
                                number.setText(singledata.getNumber());
                                address.setText(singledata.getAddress());
                                date.setText(singledata.getdate());
                                deliveredDate.append(singledata.getDeliveryDate());
                            }
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(Exception e) {
                        // Handle the error
                    }
                });

    }
}