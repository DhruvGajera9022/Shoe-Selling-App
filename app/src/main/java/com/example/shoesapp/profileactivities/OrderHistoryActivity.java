package com.example.shoesapp.profileactivities;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.shoesapp.R;
import com.example.shoesapp.adapters.OrderHistoryAdapter;
import com.example.shoesapp.adapters.OrderListAdapter;
import com.example.shoesapp.models.OrderModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class OrderHistoryActivity extends AppCompatActivity {
    FirebaseFirestore firestore;
    FirebaseAuth mAuth;
    TextView tt;
    RecyclerView recyclerView;
    OrderHistoryAdapter adapter;
    List<OrderModel> list;
    ImageView cart3;
    ProgressBar progressBar;
    ImageView toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_history);

        firestore = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();

        tt = findViewById(R.id.txt);

        toolbar=findViewById(R.id.toolbar);
        toolbar.setOnClickListener(v -> {
            onBackPressed();
        });


        recyclerView = findViewById(R.id.orderConfirmRecyclerView);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setReverseLayout(true);
        layoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(layoutManager);

        list = new ArrayList<>();
        adapter = new OrderHistoryAdapter(this, list);
        recyclerView.setAdapter(adapter);

        String uid = mAuth.getCurrentUser().getUid();

        firestore.collection("ConfirmOrder")
                .whereEqualTo("uid",uid)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful())
                        {
                            for (DocumentSnapshot documentSnapshot : task.getResult().getDocuments())
                            {
                                OrderModel orderModel = documentSnapshot.toObject(OrderModel.class);
                                list.add(orderModel);
                            }
                            if (list.isEmpty())
                            {
                                tt.setVisibility(View.VISIBLE);
//                                progressBar.setVisibility(View.GONE);
                            }
                            else
                            {
                                tt.setVisibility(View.GONE);
//                                progressBar.setVisibility(View.GONE);
                            }
                            adapter = new OrderHistoryAdapter(OrderHistoryActivity.this,list);
                            recyclerView.setAdapter(adapter);
                        }
                    }
                });

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}