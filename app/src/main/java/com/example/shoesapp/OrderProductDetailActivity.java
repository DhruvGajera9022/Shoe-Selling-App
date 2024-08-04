package com.example.shoesapp;

import android.os.Bundle;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.shoesapp.models.OrderModel;
import com.example.shoesapp.models.ProductModel;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class OrderProductDetailActivity extends AppCompatActivity {
    ImageView img, imgi;
    String key, cur_image;
    FirebaseFirestore db;
    FirebaseAuth mAuth;
    TextView pname, pprice, psize, pgender, pdesc, email, number, address, date;
    ArrayList<OrderModel> datalist;

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
                                psize.setText("Size. "+singledata.getProductSize());
                                email.setText(singledata.getEmail());
                                number.setText(singledata.getNumber());
                                address.setText(singledata.getAddress());
                                date.setText(singledata.getdate());
                            }
                        } else {
                            // Handle the case where the document does not exist
                            // e.g., show a toast or a default message
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(Exception e) {
                        // Handle any errors
                        // e.g., show a toast or log the error
                    }
                });
    }
}
