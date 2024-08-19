package com.example.shoesapp.profileactivities;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.shoesapp.R;
import com.example.shoesapp.adapters.OrderListAdapter;
import com.example.shoesapp.models.OrderModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class OrdersActivity extends AppCompatActivity {
    FirebaseFirestore firestore;
    FirebaseAuth mAuth;

    RecyclerView recyclerView;
    OrderListAdapter adapter;
    List<OrderModel> list;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_orders);

        firestore = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();

        recyclerView = findViewById(R.id.ordersRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        list = new ArrayList<>();
        adapter = new OrderListAdapter(this, list);
        recyclerView.setAdapter(adapter);

        String uid = mAuth.getCurrentUser().getUid();

        firestore.collection("Orders")
                .whereEqualTo("uid", uid)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()){
                            if (task.isSuccessful()) {
                                List<OrderModel> orderModelList = task.getResult().toObjects(OrderModel.class);
                                list.addAll(orderModelList);
                                adapter.notifyDataSetChanged();
                            }
                        }
                    }
                });

    }
}