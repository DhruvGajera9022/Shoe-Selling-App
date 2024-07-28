package com.example.shoesapp.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.shoesapp.R;
import com.example.shoesapp.adapters.AdminOrderListAdapter;
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

public class AdminOrdersFragment extends Fragment {
    FirebaseFirestore firestore;
    FirebaseAuth mAuth;

    RecyclerView recyclerView;
    AdminOrderListAdapter adapter;
    List<OrderModel> list;

    public AdminOrdersFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_admin_orders, container, false);

        firestore = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();

        recyclerView = view.findViewById(R.id.orderAdminRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        list = new ArrayList<>();
        adapter = new AdminOrderListAdapter(getActivity(), list);
        recyclerView.setAdapter(adapter);

        firestore.collection("Orders").document(mAuth.getCurrentUser().getUid())
                .collection("CurrentUser")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()){
                            for (DocumentSnapshot documentSnapshot : task.getResult().getDocuments()){
                                OrderModel orderModel = documentSnapshot.toObject(OrderModel.class);
                                list.add(orderModel);
                                adapter.notifyDataSetChanged();
                            }
                        }
                    }
                });

        return view;
    }
}