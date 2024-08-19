package com.example.shoesapp;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

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

public class OrderProductDetailActivity extends AppCompatActivity {
    ImageView img, imgi;
    String key, cur_image, strDate;
    FirebaseFirestore db;
    FirebaseAuth mAuth;
    TextView pname, pprice, psize, pgender, pdesc, email, number, address, date, arrivingDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_product_detail);

        key = getIntent().getStringExtra("key");

        pname = findViewById(R.id.productName);
        pprice = findViewById(R.id.productPrice);
        psize = findViewById(R.id.productSize);
        pgender = findViewById(R.id.productPFor);
        pdesc = findViewById(R.id.productOrderDescription);
        email = findViewById(R.id.productOrderEmail);
        number = findViewById(R.id.productOrderNumber);
        address = findViewById(R.id.productOrderAddress);
        date = findViewById(R.id.productOrderDate);
        arrivingDate = findViewById(R.id.productArrivingDate);

        img = findViewById(R.id.productImage);  // Initialize the ImageView
        imgi = img;

        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();

        db.collection("Orders").document(key)
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

                                // Parse the date and add 6 days
                                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
                                try {
                                    Date parsedDate = dateFormat.parse(date.getText().toString());
                                    if (parsedDate != null) {
                                        Calendar calendar = Calendar.getInstance();
                                        calendar.setTime(parsedDate);
                                        calendar.add(Calendar.DAY_OF_MONTH, 6);
                                        String newDate = dateFormat.format(calendar.getTime());
                                        arrivingDate.setText(newDate);
                                    }
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        } else {
                            // Handle the case where the document does not exist
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
